package io.mysocialapp.client.models

import java.io.Serializable
import java.util.*

/**
 * Created by evoxmusic on 01/09/2017.
 */
data class UserStat(var status: Status? = null,
                    var ride: Ride? = null,
                    var friend: Friend? = null,
                    var follow: Follow? = null) : Serializable {

    data class Status(val lastConnectionDate: Date? = null, val state: State? = null) : Serializable {

        enum class State {
            DISABLED, RIDING, UNKNOWN, CONNECTED, AWAY, NOT_CONNECTED
        }

        enum class Size { NORMAL, MEDIUM, SMALL }
    }

    data class Ride(val totalCreated: Int) : Serializable

    data class Friend(val total: Int) : Serializable

    data class Follow(val totalFollowing: Int, val totalFollowers: Int) : Serializable

}