package io.mysocialapp.client.models

import com.fasterxml.jackson.annotation.JsonIgnore
import io.mysocialapp.client.extensions.PaginationResource
import io.mysocialapp.client.extensions.stream
import rx.Observable
import java.io.File

/**
 * Created by evoxmusic on 03/08/15.
 */
class Group : BaseWall(), WallTextable {

    var name: String? = null
    var description: String? = null
    var open: Boolean = false
    var location: Location? = null
    var profilePhoto: Photo? = null
    var profileCoverPhoto: Photo? = null
    var members: List<GroupMember>? = null
    var member: Boolean = false
    var approvable: Boolean = false
    var distanceInMeters: Int = 0
    var totalMembers: Int = 0
    var groupMemberAccessControl: GroupMemberAccessControl? = null
    var customFields: List<CustomField>? = null

    @JsonIgnore
    var profileImageFile: File? = null
    @JsonIgnore
    var profileCoverImageFile: File? = null

    override val bodyImageURL: String?
        get() = if (profilePhoto != null) profilePhoto!!.bodyImageURL else null

    fun blockingStreamNewsFeed(limit: Int = Int.MAX_VALUE): Iterable<Feed> = streamNewsFeed(limit).toBlocking().toIterable()

    fun streamNewsFeed(limit: Int = Int.MAX_VALUE): Observable<Feed> = listNewsFeed(0, limit)

    fun blockingListNewsFeed(page: Int = 0, size: Int = 10): Iterable<Feed> =
            listNewsFeed(page, size).toBlocking()?.toIterable() ?: emptyList()

    fun listNewsFeed(page: Int = 0, size: Int = 10): Observable<Feed> {
        return stream(page, size, object : PaginationResource<Feed> {
            override fun onNext(page: Int, size: Int): List<Feed> {
                return session?.clientService?.groupWall?.list(id, page, size)?.toBlocking()?.first() ?: emptyList()
            }
        }).map { it.session = session; it }
    }

    fun blockingSendWallPost(feedPost: FeedPost): Feed? = sendWallPost(feedPost).toBlocking().first()

    fun sendWallPost(feedPost: FeedPost): Observable<Feed> {
        if (feedPost.multipartPhoto == null) {
            return feedPost.textWallMessage?.let { session?.clientService?.groupWallMessage?.post(idStr?.toLong(), it) }?.map {
                it.session = session; it
            } ?: Observable.empty()
        }

        val obs = when {
            feedPost.multipartPhoto.message == null -> session?.clientService?.groupPhoto?.post(idStr?.toLong(), feedPost.multipartPhoto.photo,
                    feedPost.multipartPhoto.accessControl!!)
            feedPost.multipartPhoto.tagEntities == null -> session?.clientService?.groupPhoto?.post(idStr?.toLong(), feedPost.multipartPhoto.photo,
                    feedPost.multipartPhoto.message, feedPost.multipartPhoto.accessControl!!)
            else -> session?.clientService?.groupPhoto?.post(idStr?.toLong(), feedPost.multipartPhoto.photo,
                    feedPost.multipartPhoto.message, feedPost.multipartPhoto.accessControl!!, feedPost.multipartPhoto.tagEntities)
        }

        return obs?.map { it.session = session; it } ?: Observable.empty()
    }

    override fun save(): Observable<Group> {
        return session?.clientService?.group?.put(idStr?.toLong(), this)?.map { it.session = session; it } ?: Observable.empty()
    }

    fun join(): Observable<GroupMember> {
        return session?.clientService?.groupMember?.post(idStr?.toLong()) ?: Observable.empty()
    }

    fun unJoin(): Observable<GroupMember> {
        return session?.clientService?.groupMember?.delete(idStr?.toLong()) ?: Observable.empty()
    }

    class Builder {
        private var mName: String? = null
        private var mDescription: String? = null
        private var mLocation: SimpleLocation? = null
        private var mMemberAccessControl = GroupMemberAccessControl.PUBLIC
        private var mImage: File? = null
        private var mCoverImage: File? = null

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
            }
        }
    }

}