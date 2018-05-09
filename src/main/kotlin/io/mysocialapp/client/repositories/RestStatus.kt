package io.mysocialapp.client.repositories

import io.mysocialapp.client.models.Status
import retrofit2.http.*
import rx.Observable

/**
 * Created by evoxmusic on 07/02/15.
 */
interface RestStatus {

    @GET("status")
    fun list(@Query("page") page: Int): Observable<List<Status>>

    @GET("status/{id}")
    operator fun get(@Path("id") id: Long?): Observable<Status>

    @POST("status")
    fun post(@Body status: Status): Observable<Status>

    @PUT("status/{id}")
    fun update(@Path("id") id: Long?, @Body status: Status): Observable<Status>

    @DELETE("status/{id}")
    fun delete(@Path("id") id: Long?): Observable<Void>

}
