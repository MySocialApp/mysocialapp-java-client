package io.mysocialapp.client.repositories

import io.mysocialapp.client.models.AuthenticationToken
import io.mysocialapp.client.models.LoginCredentials
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path
import rx.Observable

/**
 * Created by evoxmusic on 12/12/18.
 */
interface RestLoginAs {

    @POST("login/as/{userId}")
    fun post(@Path("userId") userId: Long?, @Body loginCredentials: LoginCredentials): Observable<AuthenticationToken>

    @POST("login/as/{userId}")
    fun post(@Path("userId") userId: Long?): Observable<AuthenticationToken>

}
