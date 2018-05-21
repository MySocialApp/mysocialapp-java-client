package io.mysocialapp.client.models

import io.mysocialapp.client.utils.MyObjectMapper
import java.net.URI
import java.util.*

/**
 * Created by evoxmusic on 25/01/15.
 */
data class Notification(val configId: String? = null,
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
                        val payload: Map<String, Any?>? = null) {

    val rootUrl: String?
        get() = url?.let { URI(it) }?.let { "${it.scheme}://${it.host}" }

    val id: Long? by lazy { url?.replace(rootUrl ?: "", "", true)?.split("/")?.getOrNull(2)?.toLong() }

    val owner: User? by lazy { payload?.get("owner")?.let { MyObjectMapper.objectMapper.convertValue(it, User::class.java) } }

    val recipientUserId: String?
        get() = payload?.get("recipient_user_id")?.toString()

    val recipientDeviceId: String?
        get() = payload?.get("recipient_device_id")?.toString()

}