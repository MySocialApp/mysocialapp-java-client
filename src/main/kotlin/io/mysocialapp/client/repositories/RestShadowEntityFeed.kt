package io.mysocialapp.client.repositories

import io.mysocialapp.client.models.Feed
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import rx.Observable

/**
 * Created by evoxmusic on 17/08/18.
 */
interface RestShadowEntityFeed {

    @GET("shadow/entity/{id}/wall")
    fun list(@Path("id") id: Long?, @Query("page") page: Int, @Query("size") size: Int): Observable<List<Feed>>

    @GET("shadow/entity/{id}")
    fun get(@Path("id") id: Long?): Observable<Feed>

    @DELETE("shadow/entity/{id}")
    fun delete(@Path("id") id: Long?): Observable<Void>

}
