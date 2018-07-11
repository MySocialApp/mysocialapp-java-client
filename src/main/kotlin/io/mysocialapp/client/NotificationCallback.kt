package io.mysocialapp.client

import io.mysocialapp.client.models.*

/**
 * Created by evoxmusic on 10/07/2018.
 */
interface NotificationCallback {

    fun onNewsFeed(feed: Feed) {

    }

    fun onComment(comment: Comment) {

    }

    fun onLike(like: Like) {

    }

    fun onMention(feed: Feed) {

    }

    fun onMention(comment: Comment) {

    }

    fun onConversationMessage(conversationMessage: ConversationMessage) {

    }

    fun onFriendRequest(user: User) {

    }

    fun onFriend(user: User) {

    }

    fun onEvent(event: Event) {

    }

    fun onUserJoinMyEvent(event: Event) {

    }

    fun onJoinEventConfirmation(event: Event) {

    }

}