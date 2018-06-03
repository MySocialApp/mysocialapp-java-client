package io.mysocialapp.client.repositories

import io.mysocialapp.client.models.Group
import retrofit2.http.*
import rx.Observable

/**
 * Created by evoxmusic on 01/06/18.
 */
interface RestGroup {

    @GET("group")
    fun list(@Query("page") page: Int,
             @Query("limited") limited: Boolean): Observable<List<Group>>

    @GET("group")
    fun list(@Query("page") page: Int,
             @Query("size") size: Int): Observable<List<Group>>

    @GET("group")
    fun list(@Query("page") page: Int,
             @Query("size") size: Int,
             @Query("limited") limited: Boolean): Observable<List<Group>>

    @GET("group")
    fun list(@Query("latitude") latitude: Double,
             @Query("longitude") longitude: Double,
             @Query("page") page: Int,
             @Query("limited") limited: Boolean): Observable<List<Group>>

    @GET("group")
    fun list(@Query("lower_latitude") southWestLatitude: Double,
             @Query("lower_longitude") southWestLongitude: Double,
             @Query("upper_latitude") northEastLatitude: Double,
             @Query("upper_longitude") northEastLongitude: Double,
             @QueryMap params: Map<String, String>): Observable<List<Group>>

    @GET("group")
    fun list(@Query("page") page: Int, @QueryMap params: Map<String, String>): Observable<List<Group>>

    @GET("group")
    fun list(@Query("page") page: Int, @Query("size") size: Int, @QueryMap params: Map<String, String>): Observable<List<Group>>

    @GET("group")
    fun list(@Query("latitude") latitude: Double, @Query("longitude") longitude: Double, @Query("page") page: Int,
             @Query("size") size: Int, @QueryMap params: Map<String, String>): Observable<List<Group>>

    @GET("group")
    fun list(@QueryMap params: Map<String, String>): Observable<List<Group>>

    @GET("group/{id}")
    fun get(@Path("id") id: Long?): Observable<Group>

    @POST("group")
    fun post(@Body group: Group): Observable<Group>

    @PUT("group/{id}")
    fun put(@Path("id") id: Long?, @Body group: Group): Observable<Group>

}
