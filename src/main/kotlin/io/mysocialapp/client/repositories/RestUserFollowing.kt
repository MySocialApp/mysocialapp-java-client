package io.mysocialapp.client.repositories

import io.mysocialapp.client.models.User
import retrofit2.http.*
import rx.Observable

/**
 * Created by evoxmusic on 13/12/18.
 */
interface RestUserFollowing {

    @GET("user/{userId}/following")
    fun list(@Path("userId") userId: Long?, @Query("page") page: Int): Observable<List<User>>

    @GET("user/{userId}/following")
    fun list(@Path("userId") userId: Long?, @Query("page") page: Int, @Query("size") size: Int): Observable<List<User>>

    @GET("user/{userId}/following")
    fun list(@Path("userId") userId: Long?, @Query("page") page: Int, @Query("size") size: Int, @QueryMap params: Map<String, String>): Observable<List<User>>

    @POST("user/{userId}/following")
    fun post(@Path("userId") userId: Long?): Observable<User>

    @DELETE("user/{userId}/following")
    fun delete(@Path("userId") userId: Long?): Observable<Void>

}
