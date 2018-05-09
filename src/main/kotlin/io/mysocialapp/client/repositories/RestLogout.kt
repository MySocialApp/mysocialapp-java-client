package io.mysocialapp.client.repositories

import retrofit2.http.POST
import rx.Observable

/**
 * Created by evoxmusic on 12/08/15.
 */
interface RestLogout {

    @POST("logout")
    fun post(): Observable<Void>

}
