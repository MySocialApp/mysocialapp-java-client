package io.mysocialapp.client.repositories

import io.mysocialapp.client.models.EventMember
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import rx.Observable

/**
 * Created by evoxmusic on 04/04/15.
 */
interface RestEventMember {

    @GET("event/{id}/member")
    fun list(@Path("id") id: Long?): Observable<List<EventMember>>

    @POST("event/{id}/member")
    fun post(@Path("id") id: Long?): Observable<EventMember>

    @DELETE("event/{id}/member")
    fun delete(@Path("id") id: Long?): Observable<EventMember>

}
