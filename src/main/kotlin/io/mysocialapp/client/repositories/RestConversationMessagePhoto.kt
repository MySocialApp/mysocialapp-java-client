package io.mysocialapp.client.repositories

import io.mysocialapp.client.models.ConversationMessage
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import rx.Observable

/**
 * Created by evoxmusic on 30/10/15.
 */
interface RestConversationMessagePhoto {

    @Multipart
    @POST("conversation/{id}/message/photo")
    fun post(@Path("id") id: Long?, @Part("file\"; filename=\"image\"") photo: RequestBody): Observable<ConversationMessage>

    @Multipart
    @POST("conversation/{id}/message/photo")
    fun post(@Path("id") id: Long?, @Part("file\"; filename=\"image\"") photo: RequestBody, @Part("message") message: RequestBody): Observable<ConversationMessage>

    @Multipart
    @POST("conversation/{id}/message/photo")
    fun post(@Path("id") id: Long?,
             @Part("file\"; filename=\"image\"") photo: RequestBody,
             @Part("message") message: RequestBody,
             @Part("tag_entities") tagEntities: RequestBody): Observable<ConversationMessage>

}
