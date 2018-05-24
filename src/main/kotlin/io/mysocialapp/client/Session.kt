package io.mysocialapp.client

import io.mysocialapp.client.models.AuthenticationToken
import rx.Observable

/**
 * Created by evoxmusic on 27/04/2018.
 */
class Session(private val configuration: Configuration,
              private val clientConfiguration: ClientConfiguration,
              val authenticationToken: AuthenticationToken) {

    val clientService by lazy { ClientService(configuration, clientConfiguration, this) }

    val account by lazy { FluentAccount(this) }
    val newsFeed by lazy { FluentFeed(this) }
    val user by lazy { FluentUser(this) }
    val friend by lazy { FluentFriend(this) }
    val notification by lazy { FluentNotification(this) }
    val conversation by lazy { FluentConversation(this) }

    fun disconnect(): Observable<Void> = clientService.logout.post()

}