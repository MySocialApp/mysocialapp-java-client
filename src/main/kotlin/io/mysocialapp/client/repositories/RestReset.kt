package io.mysocialapp.client.repositories

import io.mysocialapp.client.models.ResetIdentifier
import retrofit2.http.Body
import retrofit2.http.POST
import rx.Observable

/**
 * Created by evoxmusic on 02/04/15.
 */
interface RestReset {

    @POST("reset")
    fun post(@Body resetIdentifier: ResetIdentifier): Observable<Void>

}
