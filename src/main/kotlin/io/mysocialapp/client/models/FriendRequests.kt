package io.mysocialapp.client.models

/**
 * Created by evoxmusic on 12/05/2018.
 */
data class FriendRequests(val incoming: List<User>? = null,
                          val outgoing: List<User>? = null)