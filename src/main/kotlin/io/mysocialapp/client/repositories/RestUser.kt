package io.mysocialapp.client.repositories

import io.mysocialapp.client.models.User
import io.mysocialapp.client.models.Users
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap
import rx.Observable

/**
 * Created by evoxmusic on 10/01/15.
 */
interface RestUser {

    @GET("user")
    fun list(@Query("page") page: Int,
             @Query("limited") limited: Boolean): Observable<Users>

    @GET("user")
    fun list(@Query("page") page: Int,
             @Query("size") size: Int): Observable<Users>

    @GET("user")
    fun list(@Query("latitude") latitude: Double,
             @Query("longitude") longitude: Double,
             @Query("page") page: Int,
             @Query("limited") limited: Boolean): Observable<Users>

    @GET("user")
    fun list(@Query("lower_latitude") southWestLatitude: Double,
             @Query("lower_longitude") southWestLongitude: Double,
             @Query("upper_latitude") northEastLatitude: Double,
             @Query("upper_longitude") northEastLongitude: Double,
             @QueryMap params: Map<String, String>): Observable<Users>

    @GET("user")
    fun list(@Query("page") page: Int, @QueryMap params: Map<String, String>): Observable<Users>

    @GET("user")
    fun list(@Query("page") page: Int, @Query("size") size: Int, @QueryMap params: Map<String, String>): Observable<Users>

    @GET("user")
    fun list(@Query("latitude") latitude: Double, @Query("longitude") longitude: Double, @Query("page") page: Int,
             @Query("size") size: Int, @QueryMap params: Map<String, String>): Observable<Users>

    @GET("user")
    fun list(@QueryMap params: Map<String, String>): Observable<Users>

    @GET("user/{id}")
    fun get(@Path("id") id: Long?): Observable<User>

}
