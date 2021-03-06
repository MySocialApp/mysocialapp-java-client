package io.mysocialapp.client.models

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import io.mysocialapp.client.extensions.PaginationResource
import io.mysocialapp.client.extensions.stream
import io.mysocialapp.client.extensions.toRequestBody
import rx.Observable
import java.io.File

/**
 * Created by evoxmusic on 03/08/15.
 */
data class Group(var name: String? = null,
                 var description: String? = null,
                 var open: Boolean = false,
                 var location: Location? = null,
                 var profilePhoto: Photo? = null,
                 var profileCoverPhoto: Photo? = null,
                 var members: List<GroupMember>? = null,
                 var distanceInMeters: Int = 0,
                 var totalMembers: Int = 0,
                 @get:JsonProperty("is_member")
                 var isMember: Boolean? = false,
                 var groupMemberAccessControl: GroupMemberAccessControl? = null,
                 var customFields: List<CustomField>? = null) : BaseWall(), WallTextable {

    @JsonIgnore
    var profileImageFile: File? = null

    @JsonIgnore
    var profileCoverImageFile: File? = null

    override val bodyImageURL: String?
        get() = if (profilePhoto != null) profilePhoto!!.bodyImageURL else null

    val image: Photo?
        @JsonIgnore
        get() = profilePhoto

    fun blockingChangeImage(image: File): Photo? = changeImage(image).toBlocking()?.first()

    fun changeImage(image: File): Observable<Photo> {
        return session?.clientService?.groupProfilePhoto?.post(id, image.toRequestBody())?.map { it.session = session; it }
                ?: Observable.empty()
    }

    val coverImage: Photo?
        @JsonIgnore
        get() = profileCoverPhoto

    fun blockingChangeCoverImage(image: File): Photo? = changeCoverImage(image).toBlocking()?.first()

    fun changeCoverImage(image: File): Observable<Photo> {
        return session?.clientService?.groupProfileCoverPhoto?.post(id, image.toRequestBody())?.map { it.session = session; it }
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
                return session?.clientService?.groupFeed?.list(id, page, size)?.toBlocking()?.first() ?: emptyList()
            }
        }).map { it.session = session; it }
    }

    fun blockingCreateNewsFeed(feedPost: FeedPost): Feed? = createNewsFeed(feedPost).toBlocking().first()

    fun createNewsFeed(feedPost: FeedPost): Observable<Feed> {
        if (feedPost.multipartPhoto == null) {
            return feedPost.textWallMessage?.let { session?.clientService?.groupFeedMessage?.post(idStr?.toLong(), it) }?.map {
                it.session = session; it
            } ?: Observable.empty()
        }

        return session?.clientService?.groupPhoto?.post(idStr?.toLong(), feedPost.multipartPhoto.photo, feedPost.multipartPhoto.payload,
                feedPost.multipartPhoto.externalId, feedPost.multipartPhoto.message, feedPost.multipartPhoto.accessControl,
                feedPost.multipartPhoto.tagEntities)?.map { it.session = session; it } ?: Observable.empty()
    }

    override fun save(): Observable<Group> {
        return session?.clientService?.group?.put(idStr?.toLong(), this)?.map { it.session = session; it } ?: Observable.empty()
    }

    fun blockingJoin(): GroupMember? = join().toBlocking()?.first()

    fun join(): Observable<GroupMember> {
        return session?.clientService?.groupMember?.post(idStr?.toLong()) ?: Observable.empty()
    }

    fun blockingQuit(): GroupMember? = quit().toBlocking()?.first()

    fun quit(): Observable<GroupMember> {
        return session?.clientService?.groupMember?.delete(idStr?.toLong()) ?: Observable.empty()
    }

    class Builder {
        private var mName: String? = null
        private var mDescription: String? = null
        private var mLocation: SimpleLocation? = null
        private var mMemberAccessControl = GroupMemberAccessControl.PUBLIC
        private var mImage: File? = null
        private var mCoverImage: File? = null
        private var mCustomFields: List<CustomField>? = null
        private var mPayload: Map<String, Any?>? = null

        fun setName(name: String): Builder {
            this.mName = name
            return this
        }

        fun setDescription(description: String): Builder {
            this.mDescription = description
            return this
        }

        fun setLocation(location: SimpleLocation): Builder {
            this.mLocation = location
            return this
        }

        fun setMemberAccessControl(memberAccessControl: GroupMemberAccessControl): Builder {
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

        fun setPayload(payload: Map<String, Any?>): Builder {
            this.mPayload = payload
            return this
        }

        fun build(): Group {
            if (mName.isNullOrBlank()) {
                IllegalArgumentException("Name cannot be null or empty")
            }

            if (mDescription.isNullOrBlank()) {
                IllegalArgumentException("Description cannot be null or empty")
            }

            if (mLocation == null) {
                IllegalArgumentException("Meeting location cannot be null or empty")
            }

            return Group().apply {
                name = mName
                description = mDescription
                location = Location(mLocation)
                groupMemberAccessControl = mMemberAccessControl
                profileImageFile = mImage
                profileCoverImageFile = mCoverImage
                customFields = mCustomFields
                payload = mPayload
            }
        }
    }

}