package io.mysocialapp.client.repositories

import io.mysocialapp.client.models.Feed
import io.mysocialapp.client.models.TextWallMessage
import retrofit2.http.*
import rx.Observable

/**
 * Created by evoxmusic on 17/08/18.
 */
interface RestShadowEntityFeedMessage {

    @GET("shadow/entity/{id}/wall/message")
    fun list(@Path("id") id: Long?, @Query("page") page: Int): Observable<List<Feed>>

    @POST("shadow/entity/{id}/wall/message")
    fun post(@Path("id") id: Long?, @Body message: TextWallMessage): Observable<Feed>

    @PUT("shadow/entity/{id}/wall/message/{messageId}")
    fun update(@Path("id") id: Long?, @Path("messageId") messageId: Long?, @Body message: TextWallMessage): Observable<Feed>

    @DELETE("shadow/entity/{id}/wall/message/{messageId}")
    fun delete(@Path("id") id: Long?, @Path("messageId") messageId: Long?): Observable<Void>

}
