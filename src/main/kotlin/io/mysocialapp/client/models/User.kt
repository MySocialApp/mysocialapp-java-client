package io.mysocialapp.client.models

import com.fasterxml.jackson.annotation.JsonProperty
import io.mysocialapp.client.extensions.PaginationResource
import io.mysocialapp.client.extensions.prepareAsync
import io.mysocialapp.client.extensions.stream
import io.mysocialapp.client.extensions.toISO8601
import rx.Observable
import java.util.*

/**
 * Created by evoxmusic on 27/04/2018.
 */
open class User(open val updatedDate: Date? = null,
                open val profilePhoto: Photo? = null,
                open val profileCoverPhoto: Photo? = null,
                open var livingLocation: Location? = null,
                open val currentStatus: Status? = null,
                @Deprecated("please do not use username anymore, only e-mail is valid as username for registration") open val username: String? = null,
                open val firstName: String? = null,
                open val lastName: String? = null,
                open val fullName: String? = null,
                open val password: String? = null,
                open val email: String? = null,
                open val validatedEmail: Boolean? = null,
                open val gender: Gender? = null,
                open val dateOfBirth: Date? = null,
                open val presentation: String? = null,
                open val authorities: Set<String>? = null,
                open val accountEnabled: Boolean? = null,
                open val accountExpired: Boolean? = null,
                open val facebookId: String? = null,
                open val facebookAccessToken: String? = null,
                open val flag: Flag? = null,
                open val userSettings: UserSettings? = null,
                open val userStat: UserStat? = null,
                @get:JsonProperty("is_friend") open val isFriend: Boolean? = null,
                @get:JsonProperty("is_requested_as_friend") open val isRequestedAsFriend: Boolean? = null,
                open val externalId: String? = null,
                open val customFields: List<CustomField>? = null) : Base() {

    fun blockingRemoveFriend() {
        cancelFriendRequest().toBlocking()?.first()
    }

    fun removeFriend(): Observable<Void> {
        return session?.clientService?.userFriend?.delete(idStr?.toLong()) ?: Observable.empty()
    }

    fun blockingRequestAsFriend(): User? = requestAsFriend().toBlocking()?.first()

    fun requestAsFriend(): Observable<User> {
        return session?.clientService?.userFriend?.post(idStr?.toLong())?.map { it.session = session; it } ?: Observable.empty()
    }

    fun blockingCancelFriendRequest() {
        cancelFriendRequest().toBlocking()?.first()
    }

    fun cancelFriendRequest(): Observable<Void> {
        return session?.clientService?.userFriend?.delete(idStr?.toLong()) ?: Observable.empty()
    }

    fun blockingAcceptFriendRequest(): User? = acceptFriendRequest().toBlocking()?.first()

    fun acceptFriendRequest(): Observable<User> {
        return session?.clientService?.userFriend?.post(idStr?.toLong())?.map { it.session = session; it } ?: Observable.empty()
    }

    fun blockingRefuseFriendRequest() {
        refuseFriendRequest().toBlocking()?.first()
    }

    fun refuseFriendRequest(): Observable<Void> {
        return session?.clientService?.userFriend?.delete(idStr?.toLong()) ?: Observable.empty()
    }

    @JvmOverloads
    fun blockingListFriend(page: Int = 0, size: Int = 10): Iterable<User> =
            listFriend(page, size).toBlocking()?.toIterable() ?: emptyList()

    @JvmOverloads
    fun listFriend(page: Int = 0, size: Int = 10): Observable<User> {
        return stream(page, size, object : PaginationResource<User> {
            override fun getRealResultObject(response: List<User>): List<Any>? = response

            override fun onNext(page: Int, size: Int): List<User> {
                return session?.clientService?.userFriend?.list(idStr?.toLong(), page, size)?.toBlocking()?.first() ?: emptyList()
            }
        }).map { it.session = session; it }
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
                return session?.clientService?.userFeed?.list(id, page, size)?.toBlocking()?.first() ?: emptyList()
            }
        }).map { it.session = session; it }
    }

    fun blockingCreateNewsFeed(feedPost: FeedPost): Feed? = createNewsFeed(feedPost).toBlocking().first()

    fun createNewsFeed(feedPost: FeedPost): Observable<Feed> {
        if (feedPost.multipartPhoto == null) {
            return feedPost.textWallMessage?.let { session?.clientService?.userFeedMessage?.post(idStr?.toLong(), it) }?.map {
                it.session = session; it
            } ?: Observable.empty()
        }

        return session?.clientService?.photo?.post(feedPost.multipartPhoto.photo, feedPost.multipartPhoto.accessControl,
                feedPost.multipartPhoto.payload, feedPost.multipartPhoto.message, feedPost.multipartPhoto.tagEntities)
                ?.map { it.session = session; it } ?: Observable.empty()
    }

    fun blockingSendPrivateMessage(conversationMessagePost: ConversationMessagePost): ConversationMessage? {
        return sendPrivateMessage(conversationMessagePost).toBlocking()?.first()
    }

    fun sendPrivateMessage(conversationMessagePost: ConversationMessagePost): Observable<ConversationMessage> {
        return session?.conversation?.create(Conversation.Builder().addMember(this).build())?.flatMap { conversation ->
            conversation.sendMessage(conversationMessagePost).prepareAsync()
        } ?: Observable.empty()
    }

    @JvmOverloads
    fun blockingStreamGroup(limit: Int = Int.MAX_VALUE): Iterable<Group> = streamGroup(limit).toBlocking().toIterable()

    @JvmOverloads
    fun streamGroup(limit: Int = Int.MAX_VALUE): Observable<Group> = listGroup(0, limit)

    @JvmOverloads
    fun blockingListGroup(page: Int = 0, size: Int = 10): Iterable<Group> =
            listGroup(page, size).toBlocking()?.toIterable() ?: emptyList()

    @JvmOverloads
    fun listGroup(page: Int = 0, size: Int = 10): Observable<Group> {
        return stream(page, size, object : PaginationResource<Group> {
            override fun getRealResultObject(response: List<Group>): List<Any>? = response

            override fun onNext(page: Int, size: Int): List<Group> {
                return session?.clientService?.userGroup?.list(id, page, size)?.toBlocking()?.first() ?: emptyList()
            }
        }).map { it.session = session; it }
    }

    @JvmOverloads
    fun blockingStreamEvent(limit: Int = Int.MAX_VALUE, fromDate: Date = Date()): Iterable<Event> = streamEvent(limit, fromDate).toBlocking().toIterable()

    @JvmOverloads
    fun streamEvent(limit: Int = Int.MAX_VALUE, fromDate: Date = Date()): Observable<Event> = listEvent(0, limit, fromDate)

    @JvmOverloads
    fun blockingListEvent(page: Int = 0, size: Int = 10, fromDate: Date = Date()): Iterable<Event> =
            listEvent(page, size, fromDate).toBlocking()?.toIterable() ?: emptyList()

    @JvmOverloads
    fun listEvent(page: Int = 0, size: Int = 10, fromDate: Date = Date()): Observable<Event> {
        val queryMap = mapOf("sort_field" to "start_date", "date_field" to "start_date", "limited" to "false", "from_date" to fromDate.toISO8601())

        return stream(page, size, object : PaginationResource<Event> {
            override fun getRealResultObject(response: List<Event>): List<Any>? = response

            override fun onNext(page: Int, size: Int): List<Event> {
                return session?.clientService?.userEvent?.list(id, page, size, queryMap)?.toBlocking()?.first() ?: emptyList()
            }
        }).map { it.session = session; it }
    }

    @JvmOverloads
    fun blockingStreamPhotoAlbum(limit: Int = Int.MAX_VALUE): Iterable<PhotoAlbum> = streamPhotoAlbum(limit).toBlocking().toIterable()

    @JvmOverloads
    fun streamPhotoAlbum(limit: Int = Int.MAX_VALUE): Observable<PhotoAlbum> = listPhotoAlbum(0, limit)

    @JvmOverloads
    fun blockingListPhotoAlbum(page: Int = 0, size: Int = 10): Iterable<PhotoAlbum> =
            listPhotoAlbum(page, size).toBlocking()?.toIterable() ?: emptyList()

    @JvmOverloads
    fun listPhotoAlbum(page: Int = 0, size: Int = 10): Observable<PhotoAlbum> {
        return stream(page, size, object : PaginationResource<PhotoAlbum> {
            override fun getRealResultObject(response: List<PhotoAlbum>): List<Any>? = response

            override fun onNext(page: Int, size: Int): List<PhotoAlbum> {
                return session?.clientService?.userPhotoAlbum?.list(id, page, size)?.toBlocking()?.first() ?: emptyList()
            }
        }).map { it.session = session; it }
    }

}