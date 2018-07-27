package io.mysocialapp.client.repositories

import io.mysocialapp.client.models.User
import retrofit2.http.GET
import retrofit2.http.Query
import rx.Observable

/**
 * Created by evoxmusic on 26/07/18.
 */
interface RestFriend {

    @GET("friend")
    fun list(@Query("page") page: Int, @Query("size") size: Int): Observable<List<User>>

}
