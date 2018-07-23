package io.mysocialapp.client.repositories

import io.mysocialapp.client.models.Feed
import io.mysocialapp.client.models.Photo
import okhttp3.RequestBody
import retrofit2.http.*
import rx.Observable

/**
 * Created by evoxmusic on 30/01/15.
 */
interface RestPhoto {

    @GET("photo")
    fun list(@Query("page") page: Int): Observable<List<Photo>>

    @GET("photo/{id}")
    operator fun get(@Path("id") id: Long?): Observable<Photo>

    @DELETE("photo/{id}")
    fun delete(@Path("id") id: Long?): Observable<Void>

    @Multipart
    @POST("photo")
    fun post(@Part("file\"; filename=\"image\"") photo: RequestBody): Observable<Feed>

    @Multipart
    @POST("photo")
    fun post(@Part("file\"; filename=\"image\"") photo: RequestBody,
             @Part("access_control") accessControl: RequestBody): Observable<Feed>

    @Multipart
    @POST("photo")
    fun post(@Part("file\"; filename=\"image\"") photo: RequestBody,
             @Part("message") message: RequestBody,
             @Part("access_control") accessControl: RequestBody): Observable<Feed>

    @Multipart
    @POST("photo")
    fun post(@Part("file\"; filename=\"image\"") photo: RequestBody,
             @Part("message") message: RequestBody,
             @Part("access_control") accessControl: RequestBody,
             @Part("tag_entities") tagEntities: RequestBody): Observable<Feed>

    @PUT("photo/{id}")
    fun put(@Path("id") id: Long?, @Body photo: Photo): Observable<Photo>

}
