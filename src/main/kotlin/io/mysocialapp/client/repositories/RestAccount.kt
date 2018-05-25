package io.mysocialapp.client.repositories

import io.mysocialapp.client.models.LoginCredentials
import io.mysocialapp.client.models.User
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import rx.Observable

/**
 * Created by evoxmusic on 07/01/15.
 */
interface RestAccount {

    @GET("account")
    fun get(): Observable<User>

    @PUT("account")
    fun put(@Body user: User): Observable<User>

    @DELETE("account")
    fun delete(@Body loginCredentials: LoginCredentials): Observable<Void>

}
