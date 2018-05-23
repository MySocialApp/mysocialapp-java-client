package io.mysocialapp.client.repositories

import io.mysocialapp.client.models.SearchResults
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap
import rx.Observable

/**
 * Created by evoxmusic on 23/05/18.
 */
interface RestSearch {

    @GET("search")
    fun get(@Query("page") page: Int, @Query("size") size: Int, @QueryMap params: Map<String, String>): Observable<SearchResults>

}
