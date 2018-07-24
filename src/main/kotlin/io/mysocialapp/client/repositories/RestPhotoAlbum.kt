package io.mysocialapp.client.repositories

import io.mysocialapp.client.models.PhotoAlbum
import retrofit2.http.*
import rx.Observable

/**
 * Created by evoxmusic on 24/07/18.
 */
interface RestPhotoAlbum {

    @GET("photo/album")
    fun list(@Query("page") page: Int, @Query("size") size: Int): Observable<List<PhotoAlbum>>

    @GET("photo/album/{id}")
    fun get(@Path("id") id: Long?): Observable<PhotoAlbum>

    @GET("photo/album/{id}")
    fun get(@Path("id") id: Long?, @QueryMap params: Map<String, String>): Observable<PhotoAlbum>

    @POST("photo/album")
    fun post(@Body photoAlbum: PhotoAlbum): Observable<PhotoAlbum>

    @DELETE("photo/album/{id}")
    fun delete(@Path("id") id: Long?): Observable<Void>

    @PUT("photo/album/{id}")
    fun update(@Path("id") id: Long?, @Body photoAlbum: PhotoAlbum): Observable<PhotoAlbum>

}
