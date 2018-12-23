package io.mysocialapp.client.repositories

import io.mysocialapp.client.models.UserStat
import retrofit2.http.GET
import retrofit2.http.Path
import rx.Observable

/**
 * Created by evoxmusic on 23/12/18.
 */
interface RestUserStat {

    @GET("user/{userId}/stat")
    fun get(@Path("userId") userId: Long?): Observable<UserStat>

}
