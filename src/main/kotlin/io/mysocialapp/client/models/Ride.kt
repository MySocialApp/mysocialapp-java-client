package io.mysocialapp.client.models

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by evoxmusic on 11/01/15.
 */
class Ride : BaseWall, WallTextable, Localizable {

    var name: String? = null
    var description: String? = null
    var estimatedRideTime: Long = 0
    var totalDistance: Long = 0
    var users: List<User>? = null
    var locations: MutableList<RideLocation>? = null
    var startLocation: RideLocation? = null
    var endLocation: RideLocation? = null
    @JsonProperty("has_ride")
    val isRided: Boolean = false
    @JsonProperty("static_maps_url")
    val staticMapsURL: String? = null
    private var selected: Boolean = false

    constructor() {
        // empty
    }

    constructor(name: String, description: String) {
        this.name = name
        this.description = description
    }

    override fun getLocality(): BaseLocation? {
        return startLocation
    }
}
