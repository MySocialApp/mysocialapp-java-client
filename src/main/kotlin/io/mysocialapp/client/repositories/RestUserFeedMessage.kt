package io.mysocialapp.client.repositories

import io.mysocialapp.client.models.Feed
import io.mysocialapp.client.models.TextWallMessage
import retrofit2.http.*
import rx.Observable

/**
 * Created by evoxmusic on 28/01/15.
 */
interface RestUserFeedMessage {

    @GET("user/{userId}/wall/message")
    fun list(@Path("userId") userId: Long?, @Query("page") page: Int): Observable<List<Feed>>

    @POST("user/{userId}/wall/message")
    fun post(@Path("userId") userId: Long?, @Body message: TextWallMessage): Observable<Feed>

    @PUT("user/{userId}/wall/message/{messageId}")
    fun update(@Path("userId") userId: Long?, @Path("messageId") messageId: Long?, @Body message: TextWallMessage): Observable<Feed>

    @DELETE("user/{userId}/wall/message/{messageId}")
    fun delete(@Path("userId") userId: Long?, @Path("messageId") messageId: Long?): Observable<Void>


}
