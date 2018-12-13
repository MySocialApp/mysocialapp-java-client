package io.mysocialapp.client.repositories

import io.mysocialapp.client.models.User
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap
import rx.Observable

/**
 * Created by evoxmusic on 13/12/18.
 */
interface RestUserFollower {

    @GET("user/{userId}/follower")
    fun list(@Path("userId") userId: Long?, @Query("page") page: Int): Observable<List<User>>

    @GET("user/{userId}/follower")
    fun list(@Path("userId") userId: Long?, @Query("page") page: Int, @Query("size") size: Int): Observable<List<User>>

    @GET("user/{userId}/follower")
    fun list(@Path("userId") userId: Long?, @Query("page") page: Int, @Query("size") size: Int, @QueryMap params: Map<String, String>): Observable<List<User>>

}
