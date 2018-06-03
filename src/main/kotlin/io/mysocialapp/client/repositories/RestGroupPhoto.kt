package io.mysocialapp.client.repositories

import io.mysocialapp.client.models.Feed
import io.mysocialapp.client.models.Photo
import okhttp3.RequestBody
import retrofit2.http.*
import rx.Observable

/**
 * Created by evoxmusic on 14/04/15.
 */
interface RestGroupPhoto {

    @GET("group/{id}/photo")
    fun list(@Path("id") id: Long?, @Query("page") page: Int): Observable<List<Photo>>

    @Multipart
    @POST("group/{id}/photo")
    fun post(@Path("id") id: Long?,
             @Part("file\"; filename=\"image\"") photo: RequestBody,
             @Part("access_control") accessControl: RequestBody): Observable<Feed>

    @Multipart
    @POST("group/{id}/photo")
    fun post(@Path("id") id: Long?,
             @Part("file\"; filename=\"image\"") photo: RequestBody,
             @Part("message") message: RequestBody,
             @Part("access_control") accessControl: RequestBody): Observable<Feed>

    @Multipart
    @POST("group/{id}/photo")
    fun post(@Path("id") id: Long?,
             @Part("file\"; filename=\"image\"") photo: RequestBody,
             @Part("message") message: RequestBody,
             @Part("access_control") accessControl: RequestBody,
             @Part("tag_entities") tagEntities: RequestBody): Observable<Feed>

    @Multipart
    @POST("group/{id}/photo")
    fun post(@Path("id") id: Long?, @Part("file\"; filename=\"image\"") photo: RequestBody): Observable<Feed>

}
