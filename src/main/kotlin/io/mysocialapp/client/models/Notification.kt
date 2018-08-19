package io.mysocialapp.client.models

import com.fasterxml.jackson.annotation.JsonProperty
import io.mysocialapp.client.Session
import io.mysocialapp.client.extensions.convertToBase
import io.mysocialapp.client.utils.MyObjectMapper
import rx.Observable
import java.net.URI
import java.util.*

/**
 * Created by evoxmusic on 25/01/15.
 */
data class Notification(val configId: String? = null,
                        val topic: String? = null,
                        val type: String? = null,
                        val createdDate: Date? = null,
                        val title: String? = null,
                        val description: String? = null,
                        val url: String? = null,
                        val imageURL: String? = null,
                        val notificationKey: String? = null,
                        val requestAck: Boolean? = null,
                        val showNotification: Boolean? = null,
                        val forceNotificationSound: Boolean? = null,
                        @get:JsonProperty("payload") val rawPayload: Map<String, Any?>? = null) {

    var session: Session? = null

    val rootUrl: String?
        get() = url?.let { URI(it) }?.let { "${it.scheme}://${it.host}" }

    val id: Long? by lazy { url?.replace(rootUrl ?: "", "", true)?.split("/")?.getOrNull(2)?.toLong() }

    val owner: User? by lazy {
        rawPayload?.get("owner")?.let { MyObjectMapper.objectMapper.convertValue(it, User::class.java) }?.also { it.session = session }
    }

    val recipientUserId: String?
        get() = rawPayload?.get("recipient_user_id")?.toString()

    val recipientDeviceId: String?
        get() = rawPayload?.get("recipient_device_id")?.toString()

    val payload: Base? by lazy { rawPayload?.convertToBase()?.also { it.session = session } }

    fun blockingAck(notificationAck: NotificationAck): NotificationAck? = ack(notificationAck).toBlocking()?.first()

    fun ack(notificationAck: NotificationAck): Observable<NotificationAck> {
        val mNotificationAck = if (notificationAck.notificationKey == null) {
            notificationAck.copy(notificationKey = notificationKey)
        } else {
            notificationAck
        }

        return session?.clientService?.notificationAck?.post(mNotificationAck) ?: Observable.empty()
    }

}