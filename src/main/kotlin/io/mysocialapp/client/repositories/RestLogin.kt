package io.mysocialapp.client.repositories

import io.mysocialapp.client.models.AuthenticationToken
import io.mysocialapp.client.models.LoginCredentials
import retrofit2.http.Body
import retrofit2.http.POST
import rx.Observable

/**
 * Created by evoxmusic on 07/01/15.
 */
interface RestLogin {

    @POST("login")
    fun post(@Body loginCredentials: LoginCredentials): Observable<AuthenticationToken>

}
