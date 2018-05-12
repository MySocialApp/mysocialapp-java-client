package io.mysocialapp.client.repositories

import io.mysocialapp.client.models.FriendRequests
import retrofit2.http.GET
import rx.Observable

/**
 * Created by evoxmusic on 31/01/15.
 */
interface RestFriendRequest {

    @GET("friend/request")
    fun list(): Observable<FriendRequests>

}
