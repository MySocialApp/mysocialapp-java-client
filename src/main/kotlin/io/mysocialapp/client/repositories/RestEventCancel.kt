package io.mysocialapp.client.repositories

import retrofit2.http.POST
import retrofit2.http.Path
import rx.Observable

/**
 * Created by evoxmusic on 07/02/16.
 */
interface RestEventCancel {

    @POST("event/{id}/cancel")
    fun post(@Path("id") id: Long?): Observable<Void>

}
