package io.mysocialapp.client.repositories

import io.mysocialapp.client.models.Feed
import io.mysocialapp.client.models.TextWallMessage
import retrofit2.http.*
import rx.Observable

/**
 * Created by evoxmusic on 06/04/15.
 */
interface RestGroupWallMessage {

    @GET("group/{groupId}/wall/message")
    fun list(@Path("groupId") groupId: Long?, @Query("page") page: Int): Observable<List<Feed>>

    @POST("group/{groupId}/wall/message")
    fun post(@Path("groupId") groupId: Long?, @Body message: TextWallMessage): Observable<Feed>

    @PUT("group/{groupId}/wall/message/{messageId}")
    fun update(@Path("groupId") groupId: Long?, @Path("messageId") messageId: Long?, @Body message: TextWallMessage): Observable<Feed>

    @DELETE("group/{groupId}/wall/message/{messageId}")
    fun delete(@Path("groupId") groupId: Long?, @Path("messageId") messageId: Long?): Observable<Void>

}
