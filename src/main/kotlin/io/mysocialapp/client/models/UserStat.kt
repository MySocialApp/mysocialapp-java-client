package io.mysocialapp.client.models

import java.io.Serializable
import java.util.*

/**
 * Created by evoxmusic on 01/09/2017.
 */
data class UserStat(var status: Status? = null,
                    var ride: Ride? = null,
                    var bike: Bike? = null,
                    var friend: Friend? = null) : Serializable {

    data class Status(val lastConnectionDate: Date? = null, val state: State? = null) : Serializable {

        enum class State {
            DISABLED, RIDING, UNKNOWN, CONNECTED, AWAY, NOT_CONNECTED
        }

        enum class Size { NORMAL, MEDIUM, SMALL }
    }

    data class Ride(val totalCreated: Int) : Serializable

    data class Bike(val totalOwned: Int) : Serializable

    data class Friend(val total: Int) : Serializable

}