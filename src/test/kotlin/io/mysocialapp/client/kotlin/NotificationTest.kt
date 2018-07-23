package io.mysocialapp.client.kotlin

import io.mysocialapp.client.MySocialApp
import io.mysocialapp.client.NotificationCallback
import io.mysocialapp.client.Session
import io.mysocialapp.client.extensions.subscribeAsync
import io.mysocialapp.client.models.*
import org.junit.Test
import java.io.File

/**
 * Created by evoxmusic on 09/05/2018.
 */
class NotificationTest {

    companion object {
        const val APP_ID = "u470584465854a728453"
    }

    private fun getSession(): Session? = MySocialApp.Builder().setAppId(APP_ID).build().blockingConnect("AliceX", "myverysecretpassw0rd")

    @Test
    fun `list unread notifications`() {
        val s = getSession()
        assert(s?.notification?.unread?.blockingStream(100)?.toList() != null)
    }

    @Test
    fun `list read notifications`() {
        val s = getSession()
        assert(s?.notification?.read?.blockingStream(100)?.toList() != null)
    }

    @Test
    fun `consume last unread notification`() {
        val s = getSession()
        val lastNotification = s?.notification?.unread?.blockingStream(1)?.firstOrNull() ?: return
        assert(lastNotification.blockingConsume() != null)
    }

    @Test
    fun `get new event notifications`() {
        val s = getSession()
        val newEventNotifications = s?.notification?.unread?.blockingStream(100)?.toList()
        assert(newEventNotifications != null)
    }

    @Test
    fun `listen for notifications`() {
        val s = getSession()

        s?.notification?.addNotificationListener(object : NotificationCallback {

            override fun onConversationMessage(conversationMessage: ConversationMessage) {
                val message = ConversationMessagePost.Builder().setMessage(conversationMessage.message).build()
                conversationMessage.replyBack(message).subscribeAsync()
            }

            override fun onComment(comment: Comment) {
                val message = CommentPost.Builder().setMessage(comment.message).setImage(File("/tmp/my_profile.png")).build()
                comment.replyBack(message).subscribeAsync()
            }

            override fun onLike(like: Like) {
                val message = CommentPost.Builder().setMessage("Hey [[user:${like.owner?.id}]] thanks for liking this post").build()
                s.newsFeed.blockingGet(like.id!!)?.blockingAddComment(message)
            }

            override fun onNewsFeed(feed: Feed) {
                super.onNewsFeed(feed)
            }

            override fun onMention(feed: Feed) {
                super.onMention(feed)
            }

            override fun onMention(comment: Comment) {
                super.onMention(comment)
            }

            override fun onFriendRequest(user: User) {
                super.onFriendRequest(user)
            }

            override fun onFriend(user: User) {
                super.onFriend(user)
            }

            override fun onEvent(event: Event) {
                super.onEvent(event)
            }
        })


        Thread.sleep(10000000L)
    }

}