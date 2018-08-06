package io.mysocialapp.client.models

import java.io.Serializable
import java.util.*

/**
 * Created by evoxmusic on 12/04/2018.
 */
data class GroupMember(val createdDate: Date? = null,
                       val updatedDate: Date? = null,
                       val group: Group? = null,
                       val user: User? = null,
                       val previousStatus: GroupStatus? = null,
                       val status: GroupStatus? = null) : Serializable