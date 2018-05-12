package io.mysocialapp.client

import io.mysocialapp.client.models.FriendRequests
import io.mysocialapp.client.models.User
import rx.Observable

/**
 * Created by evoxmusic on 12/05/2018.
 */
class FluentFriend(private val session: Session) {

    fun blockingListIncomingFriendRequests(): Iterable<User> = listIncomingFriendRequests().toBlocking().toIterable()

    fun listIncomingFriendRequests(): Observable<User> {
        return listFriendRequests().map { it.incoming }.flatMapIterable { it }
    }

    fun blockingListOutgoingFriendRequests(): Iterable<User> = listOutgoingFriendRequests().toBlocking().toIterable()

    fun listOutgoingFriendRequests(): Observable<User> {
        return listFriendRequests().map { it.outgoing }.flatMapIterable { it }
    }

    fun blockingListFriendRequests(): FriendRequests = listFriendRequests().toBlocking().first()

    fun listFriendRequests(): Observable<FriendRequests> {
        return session.clientService.friendRequest.list().map {
            it.incoming?.forEach { it.session = session }
            it.outgoing?.forEach { it.session = session }
            it
        }
    }

    fun blockingList(): Iterable<User> = list().toBlocking()?.toIterable() ?: emptyList()

    fun list(): Observable<User> = session.account.get().flatMap { it.listFriends() }

}