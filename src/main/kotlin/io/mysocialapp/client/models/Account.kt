package io.mysocialapp.client.models

import com.fasterxml.jackson.annotation.JsonProperty
import io.mysocialapp.client.extensions.toRequestBody
import rx.Observable
import java.io.File
import java.util.*

/**
 * Created by evoxmusic on 24/07/2018.
 */
data class Account(override val updatedDate: Date? = null,
                   override val profilePhoto: Photo? = null,
                   override val profileCoverPhoto: Photo? = null,
                   override var livingLocation: Location? = null,
                   override val currentStatus: Status? = null,
                   @Deprecated("please do not use username anymore, only e-mail is valid as username for registration") override val username: String? = null,
                   override var firstName: String? = null,
                   override var lastName: String? = null,
                   override var password: String? = null,
                   override var email: String? = null,
                   override val validatedEmail: Boolean? = null,
                   override var gender: Gender? = null,
                   override var dateOfBirth: Date? = null,
                   override var presentation: String? = null,
                   override val authorities: Set<String>? = null,
                   override val accountEnabled: Boolean? = null,
                   override val accountExpired: Boolean? = null,
                   override val facebookId: String? = null,
                   override val facebookAccessToken: String? = null,
                   override val flag: Flag? = null,
                   override val userSettings: UserSettings? = null,
                   override val userStat: UserStat? = null,
                   @get:JsonProperty("is_friend") override val isFriend: Boolean? = null,
                   @get:JsonProperty("is_requested_as_friend") override val isRequestedAsFriend: Boolean? = null,
                   override var externalId: String? = null,
                   override var customFields: List<CustomField>? = null) : User(updatedDate, profilePhoto, profileCoverPhoto,
        livingLocation, currentStatus, username, firstName, lastName, password, email, validatedEmail, gender, dateOfBirth, presentation,
        authorities, accountEnabled, accountExpired, facebookId, facebookAccessToken, flag, userSettings, userStat, isFriend,
        isRequestedAsFriend, externalId, customFields) {

    override fun blockingSave(): Account? = save().toBlocking()?.first()

    override fun save(): Observable<Account> {
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

}