package io.mysocialapp.client.repositories

import io.mysocialapp.client.models.Account
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import rx.Observable

/**
 * Created by evoxmusic on 07/01/15.
 */
interface RestAccount {

    @GET("account")
    fun get(): Observable<Account>

    @PUT("account")
    fun put(@Body account: Account): Observable<Account>

}
