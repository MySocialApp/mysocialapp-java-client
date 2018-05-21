package io.mysocialapp.client.repositories

import io.mysocialapp.client.models.PreviewNotification
import retrofit2.http.GET
import retrofit2.http.Path
import rx.Observable

/**
 * Created by evoxmusic on 16/01/15.
 */
interface RestNotificationUnreadConsume {

    @GET("notification/unread/consume")
    fun list(): Observable<List<PreviewNotification>>

    @GET("notification/{id}/unread/consume")
    operator fun get(@Path("id") id: Long?): Observable<PreviewNotification>

}
