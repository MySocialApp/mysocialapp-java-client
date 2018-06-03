package io.mysocialapp.client.repositories

import io.mysocialapp.client.models.Feed
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import rx.Observable

/**
 * Created by evoxmusic on 04/04/15.
 */
interface RestEventWall {

    @GET("event/{id}/wall")
    fun list(@Path("id") id: Long?, @Query("page") page: Int, @Query("size") size: Int): Observable<List<Feed>>

}
