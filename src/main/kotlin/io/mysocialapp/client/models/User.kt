package io.mysocialapp.client.models

import io.mysocialapp.client.extensions.*
import rx.Observable
import java.io.File
import java.util.*

/**
 * Created by evoxmusic on 27/04/2018.
 */
data class User(var updatedDate: Date? = null,
                var profilePhoto: Photo? = null,
                var profileCoverPhoto: Photo? = null,
                var livingLocation: Location? = null,
                var currentStatus: Status? = null,
                @Deprecated("please do not use username anymore, only e-mail is valid as username for registration") var username: String? = null,
                var firstName: String? = null,
                var lastName: String? = null,
                var password: String? = null,
                var email: String? = null,
                var validatedEmail: Boolean? = null,
                var gender: Gender? = null,
                var dateOfBirth: Date? = null,
                var presentation: String? = null,
                var authorities: Set<String>? = null,
                var accountEnabled: Boolean? = null,
                var accountExpired: Boolean? = null,
                var facebookId: String? = null,
                var facebookAccessToken: String? = null,
                var flag: Flag? = null,
                var userSettings: UserSettings? = null,
                var userStat: UserStat? = null,
                var isFriend: Boolean? = null,
                var isRequestAsFriend: Boolean? = null,
                var externalId: String? = null,
                var customFields: List<CustomField>? = null) : Base() {


    override fun blockingSave(): User? = save().toBlocking()?.first()

    override fun save(): Observable<User> {
        return session?.clientService?.account?.put(this)?.map { it.session = session; it } ?: Observable.empty()
    }

    fun blockingChangeProfilePhoto(image: File): Photo? = changeProfilePhoto(image).toBlocking()?.first()

    fun changeProfilePhoto(image: File): Observable<Photo> {
        return session?.clientService?.accountProfilePhoto?.post(image.toRequestBody())?.map { it.session = session; it }
                ?: Observable.empty()
    }

    fun blockingChangeProfileCoverPhoto(image: File): Photo? = changeProfileCoverPhoto(image).toBlocking()?.first()

    fun changeProfileCoverPhoto(image: File): Observable<Photo> {
        return session?.clientService?.accountProfileCoverPhoto?.post(image.toRequestBody())?.map { it.session = session; it }
                ?: Observable.empty()
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

    fun blockingListFriends(page: Int = 0, size: Int = 10): Iterable<User> =
            listFriends(page, size).toBlocking()?.toIterable() ?: emptyList()

    fun listFriends(page: Int = 0, size: Int = 10): Observable<User> {
        return stream(page, size, object : PaginationResource<User> {
            override fun onNext(page: Int, size: Int): List<User> {
                return session?.clientService?.userFriend?.list(idStr?.toLong(), page, size)?.toBlocking()?.first() ?: emptyList()
            }
        }).map { it.session = session; it }
    }

    fun blockingStreamNewsFeed(limit: Int = Int.MAX_VALUE): Iterable<Feed> = streamNewsFeed(limit).toBlocking().toIterable()

    fun streamNewsFeed(limit: Int = Int.MAX_VALUE): Observable<Feed> = listNewsFeed(0, limit)

    fun blockingListNewsFeed(page: Int = 0, size: Int = 10): Iterable<Feed> =
            listNewsFeed(page, size).toBlocking()?.toIterable() ?: emptyList()

    fun listNewsFeed(page: Int = 0, size: Int = 10): Observable<Feed> {
        return stream(page, size, object : PaginationResource<Feed> {
            override fun onNext(page: Int, size: Int): List<Feed> {
                return session?.clientService?.userWall?.list(id, page, size)?.toBlocking()?.first() ?: emptyList()
            }
        }).map { it.session = session; it }
    }

    fun blockingSendWallPost(feedPost: FeedPost): Feed? = sendWallPost(feedPost).toBlocking().first()

    fun sendWallPost(feedPost: FeedPost): Observable<Feed> {
        if (feedPost.multipartPhoto == null) {
            return feedPost.textWallMessage?.let { session?.clientService?.userWallMessage?.post(idStr?.toLong(), it) }?.map {
                it.session = session; it
            } ?: Observable.empty()
        }

        val obs = when {
            feedPost.multipartPhoto.message == null -> session?.clientService?.photo?.post(feedPost.multipartPhoto.photo,
                    feedPost.multipartPhoto.accessControl!!)
            feedPost.multipartPhoto.tagEntities == null -> session?.clientService?.photo?.post(feedPost.multipartPhoto.photo,
                    feedPost.multipartPhoto.message, feedPost.multipartPhoto.accessControl!!)
            else -> session?.clientService?.photo?.post(feedPost.multipartPhoto.photo,
                    feedPost.multipartPhoto.message, feedPost.multipartPhoto.accessControl!!, feedPost.multipartPhoto.tagEntities)
        }

        return obs?.map { it.session = session; it } ?: Observable.empty()
    }

    fun blockingSendPrivateMessage(conversationMessagePost: ConversationMessagePost): ConversationMessage? {
        return sendPrivateMessage(conversationMessagePost).toBlocking()?.first()
    }

    fun sendPrivateMessage(conversationMessagePost: ConversationMessagePost): Observable<ConversationMessage> {
        return session?.conversation?.create(Conversation.Builder().addMember(this).build())?.flatMap { conversation ->
            conversation.sendMessage(conversationMessagePost).prepareAsync()
        } ?: Observable.empty()
    }

    fun blockingStreamGroup(limit: Int = Int.MAX_VALUE): Iterable<Group> = streamGroup(limit).toBlocking().toIterable()

    fun streamGroup(limit: Int = Int.MAX_VALUE): Observable<Group> = listGroup(0, limit)

    fun blockingListGroup(page: Int = 0, size: Int = 10): Iterable<Group> =
            listGroup(page, size).toBlocking()?.toIterable() ?: emptyList()

    fun listGroup(page: Int = 0, size: Int = 10): Observable<Group> {
        return stream(page, size, object : PaginationResource<Group> {
            override fun onNext(page: Int, size: Int): List<Group> {
                return session?.clientService?.userGroup?.list(id, page, size)?.toBlocking()?.first() ?: emptyList()
            }
        }).map { it.session = session; it }
    }

    fun blockingStreamEvent(limit: Int = Int.MAX_VALUE, fromDate: Date = Date()): Iterable<Event> = streamEvent(limit, fromDate).toBlocking().toIterable()

    fun streamEvent(limit: Int = Int.MAX_VALUE, fromDate: Date = Date()): Observable<Event> = listEvent(0, limit, fromDate)

    fun blockingListEvent(page: Int = 0, size: Int = 10, fromDate: Date = Date()): Iterable<Event> =
            listEvent(page, size, fromDate).toBlocking()?.toIterable() ?: emptyList()

    fun listEvent(page: Int = 0, size: Int = 10, fromDate: Date = Date()): Observable<Event> {
        val queryMap = mapOf("sort_field" to "start_date", "date_field" to "start_date", "limited" to "false", "from_date" to fromDate.toISO8601())

        return stream(page, size, object : PaginationResource<Event> {
            override fun onNext(page: Int, size: Int): List<Event> {
                return session?.clientService?.userEvent?.list(id, page, size, queryMap)?.toBlocking()?.first() ?: emptyList()
            }
        }).map { it.session = session; it }
    }

}