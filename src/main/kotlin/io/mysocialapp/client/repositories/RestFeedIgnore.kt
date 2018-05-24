package io.mysocialapp.client.repositories

import retrofit2.http.POST
import retrofit2.http.Path
import rx.Observable

/**
 * Created by evoxmusic on 30/11/17.
 */
interface RestFeedIgnore {

    @POST("feed/{id}/ignore")
    fun post(@Path("id") id: Long?): Observable<Void>

}
