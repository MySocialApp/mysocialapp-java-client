package io.mysocialapp.client.repositories

import io.mysocialapp.client.models.Like
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import rx.Observable

/**
 * Created by evoxmusic on 18/06/15.
 */
interface RestStatusLike {

    @GET("status/{id}/like")
    fun list(@Path("id") id: Long?): Observable<List<Like>>

    @POST("status/{id}/like")
    fun post(@Path("id") id: Long?): Observable<Like>

    @DELETE("status/{id}/like")
    fun delete(@Path("id") id: Long?): Observable<Void>

}
