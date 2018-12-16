package io.mysocialapp.client.repositories

import io.mysocialapp.client.models.Feed
import retrofit2.http.GET
import retrofit2.http.Path
import rx.Observable

/**
 * Created by evoxmusic on 16/12/18.
 */
interface RestFeedExternal {

    @GET("feed/external/{id}")
    fun get(@Path("id") id: String?): Observable<Feed>

}
