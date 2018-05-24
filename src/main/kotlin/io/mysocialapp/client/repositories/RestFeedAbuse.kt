package io.mysocialapp.client.repositories

import retrofit2.http.POST
import retrofit2.http.Path
import rx.Observable

/**
 * Created by evoxmusic on 30/11/17.
 */
interface RestFeedAbuse {

    @POST("feed/{id}/abuse")
    fun post(@Path("id") id: Long?): Observable<Void>

}
