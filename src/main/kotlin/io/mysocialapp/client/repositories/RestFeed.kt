package io.mysocialapp.client.repositories

import io.mysocialapp.client.models.Feed
import io.mysocialapp.client.models.FeedAlgorithm
import retrofit2.http.*
import rx.Observable

/**
 * Created by evoxmusic on 07/01/15.
 */
interface RestFeed {

    @GET("feed")
    fun list(@Query("page") page: Int, @Query("size") size: Int): Observable<List<Feed>>

    @POST("feed")
    fun list(@Query("page") page: Int, @Query("size") size: Int, @Body algorithm: FeedAlgorithm): Observable<List<Feed>>

    @GET("feed/{id}")
    fun get(@Path("id") id: Long?): Observable<Feed>

    @DELETE("feed/{id}")
    fun delete(@Path("id") id: Long?): Observable<Void>

}
