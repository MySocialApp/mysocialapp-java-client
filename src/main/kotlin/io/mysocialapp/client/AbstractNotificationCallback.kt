package io.mysocialapp.client

import io.mysocialapp.client.models.*

/**
 * Created by evoxmusic on 11/07/2018.
 */
abstract class AbstractNotificationCallback : NotificationCallback {

    override fun onNewsFeed(feed: Feed) {
    }

    override fun onComment(comment: Comment) {
    }

    override fun onLike(like: Like) {
    }

    override fun onMention(feed: Feed) {
    }

    override fun onMention(comment: Comment) {
    }

    override fun onConversationMessage(conversationMessage: ConversationMessage) {
    }

    override fun onFriendRequest(user: User) {
    }

    override fun onFriend(user: User) {
    }

    override fun onEvent(event: Event) {
    }

    override fun onUserJoinMyEvent(user: User, event: Event) {
    }

    override fun onUserJoinMyEventConfirmation(user: User, event: Event) {
    }

    override fun onGroup(group: Group) {
    }

}