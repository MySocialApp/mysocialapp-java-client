package io.mysocialapp.client.repositories

import io.mysocialapp.client.models.Event
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap
import rx.Observable

/**
 * Created by evoxmusic on 01/06/18.
 */
interface RestUserEvent {

    @GET("user/{id}/event")
    fun list(@Path("id") id: Long?, @Query("page") page: Int): Observable<List<Event>>

    @GET("user/{id}/event")
    fun list(@Path("id") id: Long?, @Query("page") page: Int, @QueryMap params: Map<String, String>): Observable<List<Event>>

    @GET("user/{id}/event")
    fun list(@Path("id") id: Long?, @Query("page") page: Int, @Query("size") size: Int): Observable<List<Event>>

    @GET("user/{id}/event")
    fun list(@Path("id") id: Long?, @Query("page") page: Int, @Query("size") size: Int, @QueryMap params: Map<String, String>): Observable<List<Event>>

}
