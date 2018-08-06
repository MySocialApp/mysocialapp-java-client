package io.mysocialapp.client.models

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import io.mysocialapp.client.extensions.PaginationResource
import io.mysocialapp.client.extensions.stream
import io.mysocialapp.client.extensions.toRequestBody
import rx.Observable
import java.io.File
import java.util.*

/**
 * Created by evoxmusic on 31/03/15.
 */
data class Event(var name: String? = null,
                 var description: String? = null,
                 @get:JsonProperty("is_cancelled")
                 var isCancelled: Boolean = false,
                 var maxSeats: Int = 0,
                 val freeSeats: Int = 0, // can be used, but only when in query param or direct /event/:id limited=false
                 val takenSeats: Int = 0, // can be used, but only when in query param or direct /event/:id limited=false
                 val totalMembers: Int? = null,
                 var startDate: Date? = null,
                 var endDate: Date? = null,
                 val members: List<EventMember>? = null,
                 var eventMemberAccessControl: EventMemberAccessControl? = null,
                 var location: Location? = null,
                 @get:JsonProperty("is_member")
                 var isMember: Boolean? = false,
                 @get:JsonProperty("static_maps_url")
                 val staticMapsURL: String? = null,
                 val distanceInMeters: Int? = null,
                 val profilePhoto: Photo? = null,
                 val profileCoverPhoto: Photo? = null,
                 @get:JsonProperty("is_available")
                 var isAvailable: Boolean = true,
                 val remainingSecondsBeforeStart: Long = 0,
                 var customFields: List<CustomField>? = null) : BaseWall(), WallTextable, Localizable {

    @JsonIgnore
    var profileImageFile: File? = null
    @JsonIgnore
    var profileCoverImageFile: File? = null

    override val bodyImageURL: String?
        get() = displayedPhoto?.highURL ?: staticMapsURL

    override val bodyImageText: String?
        get() = displayedName

    override var bodyMessage: String?
        get() = displayedName
        set(value) = Unit

    override fun getLocality(): BaseLocation? {
        return location
    }

    @JsonIgnore
    val image: Photo? = profilePhoto

    fun blockingChangeImage(image: File): Photo? = changeImage(image).toBlocking()?.first()

    fun changeImage(image: File): Observable<Photo> {
        return session?.clientService?.eventProfilePhoto?.post(id, image.toRequestBody())?.map { it.session = session; it }
                ?: Observable.empty()
    }

    @JsonIgnore
    val coverImage: Photo? = profileCoverPhoto

    fun blockingChangeCoverImage(image: File): Photo? = changeCoverImage(image).toBlocking()?.first()

    fun changeCoverImage(image: File): Observable<Photo> {
        return session?.clientService?.eventProfileCoverPhoto?.post(id, image.toRequestBody())?.map { it.session = session; it }
                ?: Observable.empty()
    }

    @JvmOverloads
    fun blockingStreamNewsFeed(limit: Int = Int.MAX_VALUE): Iterable<Feed> = streamNewsFeed(limit).toBlocking().toIterable()

    @JvmOverloads
    fun streamNewsFeed(limit: Int = Int.MAX_VALUE): Observable<Feed> = listNewsFeed(0, limit)

    @JvmOverloads
    fun blockingListNewsFeed(page: Int = 0, size: Int = 10): Iterable<Feed> =
            listNewsFeed(page, size).toBlocking()?.toIterable() ?: emptyList()

    @JvmOverloads
    fun listNewsFeed(page: Int = 0, size: Int = 10): Observable<Feed> {
        return stream(page, size, object : PaginationResource<Feed> {
            override fun getRealResultObject(response: List<Feed>): List<Any>? = response

            override fun onNext(page: Int, size: Int): List<Feed> {
                return session?.clientService?.eventWall?.list(id, page, size)?.toBlocking()?.first() ?: emptyList()
            }
        }).map { it.session = session; it }
    }

    fun blockingCreateNewsFeed(feedPost: FeedPost): Feed? = createNewsFeed(feedPost).toBlocking().first()

    fun createNewsFeed(feedPost: FeedPost): Observable<Feed> {
        if (feedPost.multipartPhoto == null) {
            return feedPost.textWallMessage?.let { session?.clientService?.eventWallMessage?.post(idStr?.toLong(), it) }?.map {
                it.session = session; it
            } ?: Observable.empty()
        }

        val obs = when {
            feedPost.multipartPhoto.message == null -> session?.clientService?.eventPhoto?.post(idStr?.toLong(), feedPost.multipartPhoto.photo,
                    feedPost.multipartPhoto.accessControl!!)
            feedPost.multipartPhoto.tagEntities == null -> session?.clientService?.eventPhoto?.post(idStr?.toLong(), feedPost.multipartPhoto.photo,
                    feedPost.multipartPhoto.message, feedPost.multipartPhoto.accessControl!!)
            else -> session?.clientService?.eventPhoto?.post(idStr?.toLong(), feedPost.multipartPhoto.photo,
                    feedPost.multipartPhoto.message, feedPost.multipartPhoto.accessControl!!, feedPost.multipartPhoto.tagEntities)
        }

        return obs?.map { it.session = session; it } ?: Observable.empty()
    }

    override fun save(): Observable<Event> {
        return session?.clientService?.event?.put(idStr?.toLong(), this)?.map { it.session = session; it } ?: Observable.empty()
    }

    fun blockingCancel() {
        cancel().toBlocking()?.first()
    }

    fun cancel(): Observable<Void> {
        return session?.clientService?.eventCancel?.post(idStr?.toLong()) ?: Observable.empty()
    }

    fun blockingConfirmParticipation() = blockingParticipate()

    fun confirmParticipation() = participate()

    fun blockingParticipate(): EventMember? = participate().toBlocking()?.first()

    fun participate(): Observable<EventMember> {
        return session?.clientService?.eventMember?.post(idStr?.toLong()) ?: Observable.empty()
    }

    fun blockingCancelParticipation() = blockingUnParticipate()

    fun cancelParticipation() = unParticipate()

    fun blockingUnParticipate(): EventMember? = unParticipate().toBlocking()?.first()

    fun unParticipate(): Observable<EventMember> {
        return session?.clientService?.eventMember?.delete(idStr?.toLong()) ?: Observable.empty()
    }

    class Builder {
        private var mName: String? = null
        private var mDescription: String? = null
        private var mStartDate: Date? = null
        private var mEndDate: Date? = null
        private var mLocation: SimpleLocation? = null
        private var mMaxSeats: Int = 10
        private var mMemberAccessControl = EventMemberAccessControl.PUBLIC
        private var mImage: File? = null
        private var mCoverImage: File? = null
        private var mCustomFields: List<CustomField>? = null

        fun setName(name: String): Builder {
            this.mName = name
            return this
        }

        fun setDescription(description: String): Builder {
            this.mDescription = description
            return this
        }

        fun setStartDate(date: Date): Builder {
            this.mStartDate = date
            return this
        }

        fun setEndDate(date: Date): Builder {
            this.mEndDate = date
            return this
        }

        fun setLocation(location: SimpleLocation): Builder {
            this.mLocation = location
            return this
        }

        fun setMaxSeats(maxSeats: Int): Builder {
            this.mMaxSeats = maxSeats
            return this
        }

        fun setMemberAccessControl(memberAccessControl: EventMemberAccessControl): Builder {
            this.mMemberAccessControl = memberAccessControl
            return this
        }

        fun setImage(image: File): Builder {
            this.mImage = image
            return this
        }

        fun setCoverImage(image: File): Builder {
            this.mCoverImage = image
            return this
        }

        fun setCustomFields(customFields: List<CustomField>?): Builder {
            this.mCustomFields = customFields
            return this
        }

        fun build(): Event {
            if (mName.isNullOrBlank()) {
                IllegalArgumentException("Name cannot be null or empty")
            }

            if (mDescription.isNullOrBlank()) {
                IllegalArgumentException("Description cannot be null or empty")
            }

            if (mStartDate == null || mEndDate == null) {
                IllegalArgumentException("Start date and end date cannot be null")
            }

            if (mStartDate!! < Date()) {
                IllegalArgumentException("Start date cannot be lower than now")
            }

            if (mStartDate!! > mEndDate!!) {
                IllegalArgumentException("Start date cannot be greater than end date")
            }

            if (mLocation == null) {
                IllegalArgumentException("Meeting location cannot be null or empty")
            }

            return Event().apply {
                name = mName
                description = mDescription
                startDate = mStartDate
                endDate = mEndDate
                location = Location(mLocation)
                maxSeats = mMaxSeats
                eventMemberAccessControl = mMemberAccessControl
                profileImageFile = mImage
                profileCoverImageFile = mCoverImage
                customFields = mCustomFields
            }
        }
    }

}
