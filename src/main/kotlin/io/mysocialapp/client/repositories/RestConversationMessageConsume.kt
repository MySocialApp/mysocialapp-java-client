package io.mysocialapp.client.repositories

import io.mysocialapp.client.models.ConversationMessage
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import rx.Observable

/**
 * Created by evoxmusic on 10/12/17.
 */
interface RestConversationMessageConsume {

    @GET("conversation/{id}/message/consume")
    fun list(@Path("id") id: Long?, @Query("page") page: Int, @Query("size") size: Int): Observable<List<ConversationMessage>>

}
