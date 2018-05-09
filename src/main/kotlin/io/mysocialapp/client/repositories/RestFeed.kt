package io.mysocialapp.client.repositories

import io.mysocialapp.client.models.Feed
import retrofit2.http.*
import rx.Observable

/**
 * Created by evoxmusic on 07/01/15.
 */
interface RestFeed {

    @GET("feed")
    fun list(@Query("page") page: Int): Observable<List<Feed>>

    @GET("feed")
    fun list(@Query("page") page: Int, @Query("size") size: Int): Observable<List<Feed>>

    @GET("feed")
    fun list(@Query("page") page: Int, @QueryMap params: Map<String, String>): Observable<List<Feed>>

    @GET("feed")
    fun list(@Query("page") page: Int, @Query("size") size: Int, @QueryMap params: Map<String, String>): Observable<List<Feed>>

    @GET("feed/{id}")
    operator fun get(@Path("id") id: Long?): Observable<Feed>

    @DELETE("feed/{id}")
    fun delete(@Path("id") id: Long?): Observable<Void>

}
