package io.mysocialapp.client.repositories

import io.mysocialapp.client.models.Like
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import rx.Observable

/**
 * Created by evoxmusic on 23/01/15.
 */
interface RestPhotoLike {

    @GET("photo/{id}/like")
    fun list(@Path("id") id: Long?): Observable<List<Like>>

    @POST("photo/{id}/like")
    fun post(@Path("id") id: Long?): Observable<Like>

    @DELETE("photo/{id}/like")
    fun delete(@Path("id") id: Long?): Observable<Void>

}
