package io.mysocialapp.client

import io.mysocialapp.client.models.CustomField
import io.mysocialapp.client.models.LoginCredentials
import io.mysocialapp.client.models.Photo
import io.mysocialapp.client.models.User
import rx.Observable
import java.io.File

/**
 * Created by evoxmusic on 04/05/2018.
 */
class FluentAccount(private val session: Session) {

    fun blockingGet(): User = get().toBlocking().first()

    fun get(): Observable<User> = session.clientService.account.get().map { it.session = session; it }

    fun blockingGetAvailableCustomFields(): Iterable<CustomField> = getAvailableCustomFields().toBlocking().toIterable()

    fun getAvailableCustomFields(): Observable<CustomField> = session.clientService.account.get().map { it.customFields }.flatMapIterable { it }

    fun blockingChangeProfilePhoto(image: File): Photo? = changeProfilePhoto(image).toBlocking()?.first()

    fun changeProfilePhoto(image: File): Observable<Photo> = get().flatMap { it.changeProfilePhoto(image) }

    fun blockingChangeProfileCoverPhoto(image: File): Photo? = changeProfileCoverPhoto(image).toBlocking()?.first()

    fun changeProfileCoverPhoto(image: File): Observable<Photo> = get().flatMap { it.changeProfileCoverPhoto(image) }

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