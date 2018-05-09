package io.mysocialapp.client.repositories

import io.mysocialapp.client.models.User
import retrofit2.http.Body
import retrofit2.http.POST
import rx.Observable

/**
 * Created by evoxmusic on 22/02/15.
 */
interface RestRegister {

    @POST("register")
    fun post(@Body user: User): Observable<User>

}
