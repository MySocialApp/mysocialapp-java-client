package io.mysocialapp.client.repositories

import io.mysocialapp.client.models.ConversationMessage
import retrofit2.http.*
import rx.Observable

/**
 * Created by evoxmusic on 07/01/15.
 */
interface RestConversationMessage {

    @GET("conversation/{id}/message")
    fun list(@Path("id") id: Long?, @Query("page") page: Int, @Query("size") size: Int): Observable<List<ConversationMessage>>

    @POST("conversation/{id}/message")
    fun post(@Path("id") id: Long?, @Body conversationMessage: ConversationMessage): Observable<ConversationMessage>

}
