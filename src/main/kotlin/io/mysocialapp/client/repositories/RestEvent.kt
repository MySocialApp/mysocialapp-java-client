package io.mysocialapp.client.repositories

import io.mysocialapp.client.models.Event
import retrofit2.http.*
import rx.Observable

/**
 * Created by evoxmusic on 02/06/18.
 */
interface RestEvent {

    @GET("event")
    fun list(@Query("page") page: Int,
             @Query("limited") limited: Boolean): Observable<List<Event>>

    @GET("event")
    fun list(@Query("page") page: Int,
             @Query("size") size: Int): Observable<List<Event>>

    @GET("event")
    fun list(@Query("page") page: Int,
             @Query("size") size: Int,
             @Query("limited") limited: Boolean): Observable<List<Event>>

    @GET("event")
    fun list(@Query("latitude") latitude: Double,
             @Query("longitude") longitude: Double,
             @Query("page") page: Int,
             @Query("limited") limited: Boolean): Observable<List<Event>>

    @GET("event")
    fun list(@Query("lower_latitude") southWestLatitude: Double,
             @Query("lower_longitude") southWestLongitude: Double,
             @Query("upper_latitude") northEastLatitude: Double,
             @Query("upper_longitude") northEastLongitude: Double,
             @QueryMap params: Map<String, String>): Observable<List<Event>>

    @GET("event")
    fun list(@Query("page") page: Int, @QueryMap params: Map<String, String>): Observable<List<Event>>

    @GET("event")
    fun list(@Query("page") page: Int, @Query("size") size: Int, @QueryMap params: Map<String, String>): Observable<List<Event>>

    @GET("event")
    fun list(@Query("latitude") latitude: Double, @Query("longitude") longitude: Double, @Query("page") page: Int,
             @Query("size") size: Int, @QueryMap params: Map<String, String>): Observable<List<Event>>

    @GET("event")
    fun list(@QueryMap params: Map<String, String>): Observable<List<Event>>

    @GET("event/{id}")
    fun get(@Path("id") id: Long?): Observable<Event>

    @POST("event")
    fun post(@Body event: Event): Observable<Event>

    @PUT("event/{id}")
    fun put(@Path("id") id: Long?, @Body event: Event): Observable<Event>

}
