package io.mysocialapp.client

import io.mysocialapp.client.models.User
import rx.Observable

/**
 * Created by evoxmusic on 04/05/2018.
 */
class FluentAccount(private val session: Session) {

    fun blockingGet(): User = get().toBlocking().first()

    fun get(): Observable<User> = session.clientService.account.get().map { it.session = session; it }

}