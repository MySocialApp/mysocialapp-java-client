package io.mysocialapp.client

import io.mysocialapp.client.models.LoginCredentials
import io.mysocialapp.client.models.User

/**
 * Created by evoxmusic on 27/04/2018.
 */
class MySocialApp(private val configuration: Configuration,
                  private val clientConfiguration: ClientConfiguration = ClientConfiguration()) {

    constructor(builder: Builder) : this(Configuration(builder.mAppId), builder.mClientConfiguration)

    private val clientService = ClientService(configuration, clientConfiguration)

    class Builder {
        var mAppId: String = ""

        var mClientConfiguration: ClientConfiguration = ClientConfiguration()
        fun setAppId(appId: String): Builder {
            this.mAppId = appId
            return this
        }

        fun setClientConfiguration(clientConfiguration: ClientConfiguration): Builder {
            this.mClientConfiguration = clientConfiguration
            return this
        }

        fun build() = MySocialApp(this)
    }

    fun createAccount(username: String, email: String, password: String, firstName: String = username): Session? {
        val user = User(username = username, firstName = firstName, email = email, password = password)

        return clientService.register.post(user).flatMap {
            clientService.login.post(LoginCredentials(username, password))
        }.toBlocking().first()?.let {
            Session(configuration, clientConfiguration, it)
        }
    }

    fun connectByEmail(email: String, password: String): Session? = connect(email, password)

    fun connect(username: String, password: String): Session? {
        return clientService.login.post(LoginCredentials(username, password)).toBlocking().first()?.let {
            Session(configuration, clientConfiguration, it)
        }
    }

}