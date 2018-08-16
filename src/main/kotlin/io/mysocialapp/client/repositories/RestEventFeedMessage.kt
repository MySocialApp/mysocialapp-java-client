package io.mysocialapp.client.repositories

import io.mysocialapp.client.models.Feed
import io.mysocialapp.client.models.TextWallMessage
import retrofit2.http.*
import rx.Observable

/**
 * Created by evoxmusic on 06/04/15.
 */
interface RestEventFeedMessage {

    @GET("event/{eventId}/wall/message")
    fun list(@Path("eventId") eventId: Long?, @Query("page") page: Int): Observable<List<Feed>>

    @POST("event/{eventId}/wall/message")
    fun post(@Path("eventId") eventId: Long?, @Body message: TextWallMessage): Observable<Feed>

    @PUT("event/{eventId}/wall/message/{messageId}")
    fun update(@Path("eventId") eventId: Long?, @Path("messageId") messageId: Long?, @Body message: TextWallMessage): Observable<Feed>

    @DELETE("event/{eventId}/wall/message/{messageId}")
    fun delete(@Path("eventId") eventId: Long?, @Path("messageId") messageId: Long?): Observable<Void>

}
