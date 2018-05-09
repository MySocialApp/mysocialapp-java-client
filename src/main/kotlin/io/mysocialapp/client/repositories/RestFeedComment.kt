package io.mysocialapp.client.repositories

import io.mysocialapp.client.models.Comment
import okhttp3.RequestBody
import retrofit2.http.*
import rx.Observable

/**
 * Created by evoxmusic on 17/01/15.
 */
interface RestFeedComment {

    @GET("feed/{id}/comment")
    fun list(@Path("id") id: Long?): Observable<List<Comment>>

    @POST("feed/{id}/comment")
    fun post(@Path("id") id: Long?, @Body comment: Comment): Observable<Comment>

    @PUT("feed/{id}/comment/{commentId}")
    fun put(@Path("id") id: Long?, @Path("commentId") commentId: Long?, @Body comment: Comment): Observable<Comment>

    @Multipart
    @POST("feed/{id}/comment/photo")
    fun post(@Path("id") id: Long?, @Part("file\"; filename=\"image\"") photo: RequestBody, @Part("name") message: RequestBody): Observable<Comment>

    @Multipart
    @POST("feed/{id}/comment/photo")
    fun post(@Path("id") id: Long?,
             @Part("file\"; filename=\"image\"") photo: RequestBody,
             @Part("name") message: RequestBody,
             @Part("tag_entities") tagEntities: RequestBody): Observable<Comment>

    @Multipart
    @POST("feed/{id}/comment/photo")
    fun post(@Path("id") id: Long?, @Part("file\"; filename=\"image\"") photo: RequestBody): Observable<Comment>

}
