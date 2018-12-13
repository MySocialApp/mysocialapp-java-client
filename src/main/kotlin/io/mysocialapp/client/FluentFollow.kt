package io.mysocialapp.client

import io.mysocialapp.client.models.User
import rx.Observable

/**
 * Created by evoxmusic on 13/12/2018.
 */
class FluentFollow(private val session: Session) {

    @JvmOverloads
    fun blockingListFollowing(page: Int = 0, size: Int = 10): Iterable<User> =
            listFollowing(page, size).toBlocking()?.toIterable() ?: emptyList()

    @JvmOverloads
    fun listFollowing(page: Int = 0, size: Int = 10): Observable<User> {
        return session.account.get().flatMap { it.listFollowing(page, size) }
    }

    @JvmOverloads
    fun blockingListFollower(page: Int = 0, size: Int = 10): Iterable<User> =
            listFollower(page, size).toBlocking()?.toIterable() ?: emptyList()

    @JvmOverloads
    fun listFollower(page: Int = 0, size: Int = 10): Observable<User> {
        return session.account.get().flatMap { it.listFollower(page, size) }
    }

}