package io.mysocialapp.client.repositories

import io.mysocialapp.client.models.Group
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap
import rx.Observable

/**
 * Created by evoxmusic on 01/06/18.
 */
interface RestUserGroup {

    @GET("user/{userId}/group")
    fun list(@Path("userId") userId: Long?): Observable<List<Group>>

    @GET("user/{userId}/group")
    fun list(@Path("userId") userId: Long?, @QueryMap params: Map<String, String>): Observable<List<Group>>

    @GET("user/{userId}/group")
    fun list(@Path("userId") userId: Long?, @Query("page") page: Int, @Query("size") size: Int): Observable<List<Group>>

    @GET("user/{userId}/group")
    fun list(@Path("userId") userId: Long?, @Query("page") page: Int, @QueryMap params: Map<String, String>): Observable<List<Group>>

}
