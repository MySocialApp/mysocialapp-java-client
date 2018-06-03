package io.mysocialapp.client.repositories

import io.mysocialapp.client.models.GroupMember
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import rx.Observable

/**
 * Created by evoxmusic on 04/04/15.
 */
interface RestGroupMember {

    @GET("group/{id}/member")
    fun list(@Path("id") id: Long?): Observable<List<GroupMember>>

    @POST("group/{id}/member")
    fun post(@Path("id") id: Long?): Observable<GroupMember>

    @DELETE("group/{id}/member")
    fun delete(@Path("id") id: Long?): Observable<GroupMember>

}
