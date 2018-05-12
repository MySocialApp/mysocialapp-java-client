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

    val account: FluentAccount = FluentAccount(this)
    val newsFeed: FluentFeed = FluentFeed(this)
    val user: FluentUser = FluentUser(this)
    val friend: FluentFriend = FluentFriend(this)

    fun disconnect(): Observable<Void> = clientService.logout.post()

}