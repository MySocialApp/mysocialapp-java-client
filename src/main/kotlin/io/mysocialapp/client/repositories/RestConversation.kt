package io.mysocialapp.client.repositories

import io.mysocialapp.client.models.Conversation
import retrofit2.http.*
import rx.Observable

/**
 * Created by evoxmusic on 07/01/15.
 */
interface RestConversation {

    @GET("conversation?messageSamples=1")
    fun list(@Query("page") page: Int, @Query("size") size: Int): Observable<List<Conversation>>

    @GET("conversation/{id}")
    fun get(@Path("id") id: Long?): Observable<Conversation>

    @POST("conversation")
    fun post(@Body conversation: Conversation): Observable<Conversation>

    @PUT("conversation/{id}")
    fun update(@Path("id") id: Long?, @Body conversation: Conversation): Observable<Conversation>

    @DELETE("conversation/{id}")
    fun delete(@Path("id") id: Long?): Observable<Void>

    @POST("conversation/{id}/silent")
    fun doSilent(@Path("id") id: Long?): Observable<Conversation>

    @DELETE("conversation/{id}/silent")
    fun undoSilent(@Path("id") id: Long?): Observable<Void>
}
