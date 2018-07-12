package io.mysocialapp.client

import io.mysocialapp.client.extensions.prepareAsyncBackground
import io.mysocialapp.client.models.Comment
import io.mysocialapp.client.models.ConversationMessage
import io.mysocialapp.client.models.Like
import io.mysocialapp.client.models.Notification
import io.mysocialapp.client.utils.MyObjectMapper
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import rx.Observable
import java.lang.Exception
import java.net.URI
import java.nio.channels.NotYetConnectedException
import java.util.concurrent.TimeUnit

/**
 * Created by evoxmusic on 10/07/2018.
 */
class WebSocketService(private val configuration: Configuration,
                       private val clientConfiguration: ClientConfiguration,
                       private val session: Session? = null) {

    private fun <T> convert(map: Map<String, Any?>?, clazz: Class<T>): T? {
        if (map == null) {
            return null
        }

        return try {
            MyObjectMapper.objectMapper.convertValue(map, clazz)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            null
        }
    }

    private val notificationListeners = mutableListOf<NotificationCallback>()

    private val notificationWebSocketClient by lazy {
        object : WebSocketClient(URI(configuration.completeWebSocketEndpointURL + "/notification"),
                session?.authenticationToken?.accessToken?.let { mapOf("Authorization" to it) } ?: emptyMap()) {

            override fun onOpen(handshakedata: ServerHandshake?) {
            }

            override fun onClose(code: Int, reason: String?, remote: Boolean) {
            }

            override fun onMessage(message: String?) {
                if (notificationListeners.isEmpty()) {
                    return
                }

                val notification = try {
                    MyObjectMapper.objectMapper.readValue(message, Notification::class.java)
                } catch (e: IllegalArgumentException) {
                    e.printStackTrace()
                    null
                } ?: return

                when (notification.type) {

                    "COMMENT" -> convert(notification.payload, Comment::class.java)?.let { v ->
                        v.also { it.session = session }
                        notificationListeners.forEach { it.onComment(v) }
                    }

                    "LIKE" -> convert(notification.payload, Like::class.java)?.let { v ->
                        v.also { it.session = session }
                        notificationListeners.forEach { it.onLike(v) }
                    }

                    "CONVERSATION_MESSAGE" -> convert(notification.payload, ConversationMessage::class.java)?.let { v ->
                        v.also { it.session = session }
                        notificationListeners.forEach { it.onConversationMessage(v) }
                    }

                    "FEED" -> notification.id?.let {
                        val feed = session?.newsFeed?.blockingGet(it) ?: return@let
                        notificationListeners.forEach { it.onNewsFeed(feed) }
                    }

                    "USER_MENTION_TAG" -> {
                        if (notification.payload?.get("type")?.toString()?.toLowerCase() == "comment") {
                            convert(notification.payload, Comment::class.java)?.let { v ->
                                v.also { it.session = session }
                                notificationListeners.forEach { it.onMention(v) }
                            }
                            return
                        }

                        notification.id?.let {
                            val feed = session?.newsFeed?.blockingGet(it) ?: return@let
                            notificationListeners.forEach { it.onMention(feed) }
                        }
                    }

                    "NEW_EVENT" -> notification.id?.let {
                        val event = session?.event?.blockingGet(it) ?: return@let
                        notificationListeners.forEach { it.onEvent(event) }
                    }

                    "FRIEND_REQUEST" -> notification.id?.let {
                        val user = session?.user?.blockingGet(it) ?: return@let
                        notificationListeners.forEach { it.onFriendRequest(user) }
                    }

                    "FRIEND" -> notification.id?.let {
                        val user = session?.user?.blockingGet(it) ?: return@let
                        notificationListeners.forEach { it.onFriendRequest(user) }
                    }

                    "OFFER" -> {

                    }

                    else -> Unit
                }
            }

            override fun onError(ex: Exception?) {
                ex?.printStackTrace()
            }
        }
    }


    private val keepConnectionAlive by lazy {
        Observable.interval(10, TimeUnit.SECONDS).map {
            try {
                notificationWebSocketClient.sendPing()
            } catch (e: NotYetConnectedException) {
                notificationWebSocketClient.reconnect()
            }
        }.prepareAsyncBackground().cache()
    }

    fun addNotificationListener(notificationCallback: NotificationCallback) {
        notificationListeners.add(notificationCallback)

        if (!notificationWebSocketClient.isOpen) {
            notificationWebSocketClient.connect()
        }

        keepConnectionAlive.subscribe()
    }

}