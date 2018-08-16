package io.mysocialapp.client.repositories

import io.mysocialapp.client.models.Photo
import okhttp3.RequestBody
import retrofit2.http.*
import rx.Observable

/**
 * Created by evoxmusic on 17/08/18.
 */
interface RestShadowEntityProfileCoverPhoto {

    @GET("shadow/entity/{id}/profile/cover/photo")
    operator fun get(@Path("id") id: Long?): Observable<Photo>

    @Multipart
    @POST("shadow/entity/{id}/profile/cover/photo")
    fun post(@Path("id") id: Long?, @Part("file\"; filename=\"image\"") photo: RequestBody): Observable<Photo>

}
