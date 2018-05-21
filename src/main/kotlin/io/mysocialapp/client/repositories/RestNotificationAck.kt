package io.mysocialapp.client.repositories

import io.mysocialapp.client.models.NotificationAck
import retrofit2.http.Body
import retrofit2.http.POST
import rx.Observable

/**
 * Created by evoxmusic on 04/12/2017.
 */
interface RestNotificationAck {

    @POST("notification/ack")
    fun post(@Body notificationAck: NotificationAck): Observable<NotificationAck>

}