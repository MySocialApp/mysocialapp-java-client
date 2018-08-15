package io.mysocialapp.client

import io.mysocialapp.client.models.FriendRequests
import io.mysocialapp.client.models.User
import rx.Observable

/**
 * Created by evoxmusic on 12/05/2018.
 */
class FluentFriend(private val session: Session) {

    fun blockingListActiveFriends(): Iterable<User> = listActiveFriends().toBlocking().toIterable()

    fun listActiveFriends(): Observable<User> {
        return session.clientService.userActive.list().flatMapIterable { it }.map { it.session = session; it }
    }

    fun blockingTotalIncomingFriendRequests(): Int = totalIncomingFriendRequests().toBlocking().first()

    fun totalIncomingFriendRequests(): Observable<Int> {
        return session.clientService.accountEvent.get().map { it.friendRequest?.totalIncomingRequests ?: 0 }
    }

    fun blockingListIncomingFriendRequests(): Iterable<User> = listIncomingFriendRequests().toBlocking().toIterable()

    fun listIncomingFriendRequests(): Observable<User> {
        return listFriendRequests().map { it.incoming ?: emptyList() }.flatMapIterable { it }
    }

    fun blockingListOutgoingFriendRequests(): Iterable<User> = listOutgoingFriendRequests().toBlocking().toIterable()

    fun listOutgoingFriendRequests(): Observable<User> {
        return listFriendRequests().map { it.outgoing ?: emptyList() }.flatMapIterable { it }
    }

    fun blockingListFriendRequests(): FriendRequests = listFriendRequests().toBlocking().first()

    fun listFriendRequests(): Observable<FriendRequests> {
        return session.clientService.friendRequest.list().map { v ->
            v.incoming?.forEach { it.session = session }
            v.outgoing?.forEach { it.session = session }
            v
        }
    }

    @JvmOverloads
    fun blockingStream(limit: Int = Int.MAX_VALUE): Iterable<User> = stream(limit).toBlocking().toIterable()

    @JvmOverloads
    fun stream(limit: Int = Int.MAX_VALUE): Observable<User> = list(0, limit)

    @JvmOverloads
    fun blockingList(page: Int = 0, size: Int = 10): Iterable<User> = list(page, size).toBlocking()?.toIterable() ?: emptyList()

    @JvmOverloads
    fun list(page: Int = 0, size: Int = 10): Observable<User> = session.friend.list(page, size).map { it.session = session; it }

}