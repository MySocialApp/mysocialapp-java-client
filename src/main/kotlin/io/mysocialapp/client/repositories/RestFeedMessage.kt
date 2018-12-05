package io.mysocialapp.client.repositories

import io.mysocialapp.client.models.Feed
import io.mysocialapp.client.models.TextWallMessage
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import rx.Observable

/**
 * Created by evoxmusic on 22/07/17.
 */
interface RestFeedMessage {

    @POST("feed/message")
    fun post(@Body message: TextWallMessage): Observable<Feed>

    @PUT("feed/message/{id}")
    fun put(@Path("id") id: Long?, @Body message: TextWallMessage): Observable<Feed>

}
