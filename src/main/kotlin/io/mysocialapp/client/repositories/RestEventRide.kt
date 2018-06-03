package io.mysocialapp.client.repositories

import io.mysocialapp.client.models.Ride
import retrofit2.http.*
import rx.Observable

/**
 * Created by evoxmusic on 04/03/17.
 */
interface RestEventRide {

    @GET("event/{eventId}/ride")
    fun list(@Path("eventId") eventId: Long?, @Query("page") page: Int): Observable<List<Ride>>

    @GET("event/{eventId}/ride")
    fun list(@Path("eventId") eventId: Long?, @Query("page") page: Int, @Query("size") size: Int = 10): Observable<List<Ride>>

    @POST("event/{eventId}/ride/{rideId}")
    fun post(@Path("eventId") eventId: Long?, @Path("rideId") rideId: Long?): Observable<Ride>

    @DELETE("event/{eventId}/ride/{rideId}")
    fun delete(@Path("eventId") eventId: Long?, @Path("rideId") rideId: Long?): Observable<Void?>

}