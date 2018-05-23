package io.mysocialapp.client.models

import io.mysocialapp.client.extensions.PaginationResource
import io.mysocialapp.client.extensions.stream
import rx.Observable
import java.util.*

/**
 * Created by evoxmusic on 27/04/2018.
 */
data class User(var updatedDate: Date? = null,
                var profilePhoto: Photo? = null,
                var profileCoverPhoto: Photo? = null,
                var livingLocation: Location? = null,
                var currentStatus: Status? = null,
                var username: String? = null,
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


    fun save(): Observable<User> {
        return session?.clientService?.account?.put(this)?.map { it.session = session; it } ?: Observable.empty()
    }

    fun blockingSave() {
        save().toBlocking().subscribe()
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

    fun blockingListFriends(): Iterable<User> = listFriends().toBlocking()?.toIterable() ?: emptyList()

    fun listFriends(): Observable<User> {
        return stream(0, Int.MAX_VALUE, object : PaginationResource<User> {
            override fun onNext(page: Int, size: Int): List<User> {
                return session?.clientService?.userFriend?.list(idStr?.toLong(), page, size)?.toBlocking()?.first() ?: emptyList()
            }
        }).map { it.session = session; it }
    }

    fun blockingStreamNewsFeed(limit: Int = Int.MAX_VALUE): Iterable<Feed> = streamNewsFeed(limit).toBlocking().toIterable()

    fun streamNewsFeed(limit: Int = Int.MAX_VALUE): Observable<Feed> = listNewsFeed(0, limit)

    fun blockingListNewsFeed(page: Int = 0, size: Int = 10): Iterable<Feed> = listNewsFeed().toBlocking()?.toIterable() ?: emptyList()

    fun listNewsFeed(page: Int = 0, size: Int = 10): Observable<Feed> {
        return stream(page, size, object : PaginationResource<Feed> {
            override fun onNext(page: Int, size: Int): List<Feed> {
                return session?.clientService?.feed?.list(page, size)?.toBlocking()?.first() ?: emptyList()
            }
        }).map { it.session = session; it }
    }

    fun blockingSendWallPost(textWallMessage: TextWallMessage): Feed = sendWallPost(textWallMessage).toBlocking().first()

    fun sendWallPost(textWallMessage: TextWallMessage): Observable<Feed> {
        return session?.clientService?.userWallMessage?.post(idStr?.toLong(), textWallMessage)?.map {
            it.session = session; it
        } ?: Observable.empty()
    }

    fun blockingSendWallPost(multipartPhoto: MultipartPhoto): Feed = sendWallPost(multipartPhoto).toBlocking().first()

    fun sendWallPost(multipartPhoto: MultipartPhoto): Observable<Feed> {
        return session?.clientService?.photo?.post(multipartPhoto.photo, null, multipartPhoto.message,
                multipartPhoto.accessControl, multipartPhoto.tagEntities)?.map {
            it.session = session; it
        } ?: Observable.empty()
    }

}