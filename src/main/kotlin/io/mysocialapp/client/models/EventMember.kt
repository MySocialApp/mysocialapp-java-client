package io.mysocialapp.client.models

import java.io.Serializable
import java.util.*

/**
 * Created by evoxmusic on 10/09/2017.
 */
data class EventMember(val createdDate: Date? = null,
                       val updatedDate: Date? = null,
                       val event: Event? = null,
                       val user: User? = null,
                       val previousStatus: EventStatus? = null,
                       val status: EventStatus? = null) : Serializable