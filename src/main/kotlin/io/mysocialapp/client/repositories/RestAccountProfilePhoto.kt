package io.mysocialapp.client.repositories

import io.mysocialapp.client.models.Photo
import okhttp3.RequestBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import rx.Observable

/**
 * Created by evoxmusic on 10/02/15.
 */
interface RestAccountProfilePhoto {

    @GET("account/profile/photo")
    fun get(): Observable<Photo>

    @Multipart
    @POST("account/profile/photo")
    fun post(@Part("file\"; filename=\"image\"") photo: RequestBody): Observable<Photo>

}
