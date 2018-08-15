package io.mysocialapp.client.repositories

import io.mysocialapp.client.models.AccountEvent
import retrofit2.http.GET
import rx.Observable

/**
 * Created by evoxmusic on 15/08/18.
 */
interface RestAccountEvent {

    @GET("account/event")
    fun get(): Observable<AccountEvent>

}
