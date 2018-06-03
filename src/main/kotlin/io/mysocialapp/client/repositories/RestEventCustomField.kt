package io.mysocialapp.client.repositories

import io.mysocialapp.client.models.CustomField
import retrofit2.http.GET
import retrofit2.http.Path
import rx.Observable

/**
 * Created by evoxmusic on 27/03/18.
 */
interface RestEventCustomField {

    @GET("event/customfield")
    fun list(): Observable<List<CustomField>>

    @GET("event/{eventId}/customfield")
    fun list(@Path("eventId") eventId: Long?): Observable<List<CustomField>>

}