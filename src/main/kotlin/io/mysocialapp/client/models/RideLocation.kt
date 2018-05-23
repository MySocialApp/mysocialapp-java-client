package io.mysocialapp.client.models

import java.io.Serializable

/**
 * Created by evoxmusic on 15/12/16.
 */
data class RideLocation(var position: Int?,
                        override var latitude: Double?,
                        override var longitude: Double?,
                        val altitude: Double?,
                        var locationType: RideLocationType? = null,
                        var note: String? = null) : BaseLocation, Serializable