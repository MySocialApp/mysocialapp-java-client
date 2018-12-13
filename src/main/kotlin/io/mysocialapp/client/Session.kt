package io.mysocialapp.client

import io.mysocialapp.client.models.AuthenticationToken
import rx.Observable

/**
 * Created by evoxmusic on 27/04/2018.
 */
class Session(val configuration: Configuration,
              val clientConfiguration: ClientConfiguration,
              val authenticationToken: AuthenticationToken) {

    val clientService by lazy { ClientService(configuration, clientConfiguration, this) }
    val webSocketService by lazy { WebSocketService(configuration, clientConfiguration, this) }

    val account by lazy { FluentAccount(this) }
    val newsFeed by lazy { FluentNewsFeed(this) }
    val user by lazy { FluentUser(this) }
    val friend by lazy { FluentFriend(this) }
    val follow by lazy { FluentFollow(this) }
    val notification by lazy { FluentNotification(this) }
    val conversation by lazy { FluentConversation(this) }
    val group by lazy { FluentGroup(this) }
    val event by lazy { FluentEvent(this) }
    val photo by lazy { FluentPhoto(this) }
    val photoAlbum by lazy { FluentPhotoAlbum(this) }

    fun blockingDisconnect() {
        disconnect().toBlocking()?.first()
    }

    fun disconnect(): Observable<Void> = clientService.logout.post()

}