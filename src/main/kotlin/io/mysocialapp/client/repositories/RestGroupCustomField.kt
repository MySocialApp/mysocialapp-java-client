package io.mysocialapp.client.repositories

import io.mysocialapp.client.models.CustomField
import retrofit2.http.GET
import retrofit2.http.Path
import rx.Observable

/**
 * Created by evoxmusic on 27/03/18.
 */
interface RestGroupCustomField {

    @GET("group/customfield")
    fun list(): Observable<List<CustomField>>

    @GET("group/{groupId}/customfield")
    fun list(@Path("groupId") eventId: Long?): Observable<List<CustomField>>

}