package io.mysocialapp.client.models

/**
 * Created by evoxmusic on 15/08/2018.
 */
data class AccountEvent(val notification: Notification? = null,
                        val friendRequest: FriendRequest? = null,
                        val conversation: Conversation? = null) {

    data class Notification(val totalUnreads: Int = 0)

    data class FriendRequest(val totalIncomingRequests: Int = 0)

    data class Conversation(val totalUnreads: Int = 0)

}