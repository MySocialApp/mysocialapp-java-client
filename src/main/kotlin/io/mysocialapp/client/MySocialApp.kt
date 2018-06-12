package io.mysocialapp.client

import io.mysocialapp.client.extensions.prepareAsync
import io.mysocialapp.client.models.AuthenticationToken
import io.mysocialapp.client.models.LoginCredentials
import io.mysocialapp.client.models.ResetIdentifier
import io.mysocialapp.client.models.User
import rx.Observable

/**
 * Created by evoxmusic on 27/04/2018.
 */
class MySocialApp(private val configuration: Configuration,
                  private val clientConfiguration: ClientConfiguration = ClientConfiguration()) {

    constructor(builder: Builder) : this(Configuration(builder.mAppId, builder.mAPIEndpointURL), builder.mClientConfiguration)

    private val clientService = ClientService(configuration, clientConfiguration)

    class Builder {
        var mAppId: String = ""
        var mAPIEndpointURL: String? = null

        var mClientConfiguration: ClientConfiguration = ClientConfiguration()

        fun setAppId(appId: String): Builder {
            this.mAppId = appId
            return this
        }

        fun setAPIEndpointURL(appURL: String): Builder {
            this.mAPIEndpointURL = appURL
            return this
        }

        fun setClientConfiguration(clientConfiguration: ClientConfiguration): Builder {
            this.mClientConfiguration = clientConfiguration
            return this
        }

        fun build() = MySocialApp(this)
    }

    fun blockingCreateAccount(email: String, password: String, firstName: String): Session? {
        return createAccount(email, password, firstName).toBlocking()?.first()
    }

    fun createAccount(email: String, password: String, firstName: String): Observable<Session> {
        val user = User(firstName = firstName, email = email, password = password)

        return clientService.register.post(user).flatMap {
            clientService.login.post(LoginCredentials(email, password)).prepareAsync()
        }.map {
            Session(configuration, clientConfiguration, it)
        }
    }

    fun blockingConnect(email: String, password: String): Session? {
        return connect(email, password).toBlocking()?.first()
    }

    fun connect(email: String, password: String): Observable<Session> {
        return clientService.login.post(LoginCredentials(email, password)).map { Session(configuration, clientConfiguration, it) }
    }

    fun blockingConnect(accessToken: String): Session? {
        return connect(accessToken).toBlocking()?.first()
    }

    fun connect(accessToken: String): Observable<Session> {
        return Observable.just(AuthenticationToken(accessToken = accessToken)).map { Session(configuration, clientConfiguration, it) }
    }

    fun blockingResetPasswordByEmail(email: String) = resetPasswordByEmail(email).toBlocking()?.first()

    fun resetPasswordByEmail(email: String): Observable<Void> = resetPassword(ResetIdentifier(email = email))

    fun blockingResetPassword(username: String) = resetPassword(username).toBlocking()?.first()

    fun resetPassword(username: String): Observable<Void> = resetPassword(ResetIdentifier(username = username))

    private fun resetPassword(resetIdentifier: ResetIdentifier): Observable<Void> {
        return clientService.reset.post(resetIdentifier)
    }

}