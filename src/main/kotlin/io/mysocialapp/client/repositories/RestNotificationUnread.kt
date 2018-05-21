package io.mysocialapp.client.repositories

import io.mysocialapp.client.models.PreviewNotification
import retrofit2.http.GET
import retrofit2.http.Query
import rx.Observable

/**
 * Created by evoxmusic on 16/01/15.
 */
interface RestNotificationUnread {

    @GET("notification/unread")
    fun list(@Query("page") page: Int, @Query("size") size: Int): Observable<List<PreviewNotification>>

}
