package io.mysocialapp.client.repositories

import io.mysocialapp.client.models.User
import retrofit2.http.GET
import rx.Observable

/**
 * Created by evoxmusic on 30/05/17.
 */
interface RestUserActive {

    @GET("user/active")
    fun list(): Observable<List<User>>

}