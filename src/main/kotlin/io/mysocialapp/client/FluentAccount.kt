package io.mysocialapp.client

import io.mysocialapp.client.models.LoginCredentials
import io.mysocialapp.client.models.User
import rx.Observable

/**
 * Created by evoxmusic on 04/05/2018.
 */
class FluentAccount(private val session: Session) {

    fun blockingGet(): User = get().toBlocking().first()

    fun get(): Observable<User> = session.clientService.account.get().map { it.session = session; it }

    /**
     * Caution: your account will be completely erased and no more available.
     * This method delete all your data that belongs to this account.!!
     */
    fun blockingRequestForDeleteAccount(password: String) {
        requestForDeleteAccount(password).toBlocking()?.first()
    }

    /**
     * Caution: your account will be completely erased and no more available.
     * This method delete all your data that belongs to this account.!!
     */
    fun requestForDeleteAccount(password: String): Observable<Void> {
        return session.clientService.account.delete(LoginCredentials(password = password, username = ""))
    }

}