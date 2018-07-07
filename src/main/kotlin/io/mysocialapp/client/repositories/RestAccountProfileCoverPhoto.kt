package io.mysocialapp.client.repositories

import io.mysocialapp.client.models.Photo
import okhttp3.RequestBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import rx.Observable

/**
 * Created by evoxmusic on 07/09/17.
 */
interface RestAccountProfileCoverPhoto {

    @GET("account/profile/cover/photo")
    fun get(): Observable<Photo>

    @Multipart
    @POST("account/profile/cover/photo")
    fun post(@Part("file\"; filename=\"image\"") photo: RequestBody): Observable<Photo>

}
