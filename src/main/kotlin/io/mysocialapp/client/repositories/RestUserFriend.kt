package io.mysocialapp.client.repositories

import io.mysocialapp.client.models.User
import retrofit2.http.*
import rx.Observable

/**
 * Created by evoxmusic on 10/01/15.
 */
interface RestUserFriend {

    @GET("user/{userId}/friend")
    fun list(@Path("userId") userId: Long?, @Query("page") page: Int): Observable<List<User>>

    @GET("user/{userId}/friend")
    fun list(@Path("userId") userId: Long?, @Query("page") page: Int, @Query("size") size: Int): Observable<List<User>>

    @GET("user/{userId}/friend")
    fun list(@Path("userId") userId: Long?, @Query("page") page: Int, @Query("size") size: Int, @QueryMap params: Map<String, String>): Observable<List<User>>

    @POST("user/{userId}/friend")
    fun post(@Path("userId") userId: Long?): Observable<User>

    @DELETE("user/{userId}/friend")
    fun delete(@Path("userId") userId: Long?): Observable<Void>

}
