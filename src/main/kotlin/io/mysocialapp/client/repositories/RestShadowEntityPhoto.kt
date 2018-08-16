package io.mysocialapp.client.repositories

import io.mysocialapp.client.models.Feed
import io.mysocialapp.client.models.Photo
import okhttp3.RequestBody
import retrofit2.http.*
import rx.Observable

/**
 * Created by evoxmusic on 17/08/18.
 */
interface RestShadowEntityPhoto {

    @GET("shadow/entity/{id}/photo")
    fun list(@Path("id") id: Long?, @Query("page") page: Int): Observable<List<Photo>>

    @Multipart
    @POST("shadow/entity/{id}/photo")
    fun post(@Path("id") id: Long?,
             @Part("file\"; filename=\"image\"") photo: RequestBody,
             @Part("access_control") accessControl: RequestBody): Observable<Feed>

    @Multipart
    @POST("shadow/entity/{id}/photo")
    fun post(@Path("id") id: Long?,
             @Part("file\"; filename=\"image\"") photo: RequestBody,
             @Part("message") message: RequestBody,
             @Part("access_control") accessControl: RequestBody): Observable<Feed>

    @Multipart
    @POST("shadow/entity/{id}/photo")
    fun post(@Path("id") id: Long?,
             @Part("file\"; filename=\"image\"") photo: RequestBody,
             @Part("message") message: RequestBody,
             @Part("access_control") accessControl: RequestBody,
             @Part("tag_entities") tagEntities: RequestBody): Observable<Feed>

    @Multipart
    @POST("shadow/entity/{id}/photo")
    fun post(@Path("id") id: Long?, @Part("file\"; filename=\"image\"") photo: RequestBody): Observable<Feed>

}
