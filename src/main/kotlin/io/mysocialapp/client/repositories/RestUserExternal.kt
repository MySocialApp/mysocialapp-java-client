package io.mysocialapp.client.repositories

import io.mysocialapp.client.models.User
import retrofit2.http.GET
import retrofit2.http.Path
import rx.Observable

/**
 * Created by evoxmusic on 23/05/18.
 */
interface RestUserExternal {

    @GET("user/external/{externalId}")
    fun get(@Path("externalId") externalId: String?): Observable<User>

}
