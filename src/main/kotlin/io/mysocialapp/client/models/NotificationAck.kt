package io.mysocialapp.client.models

/**
 * Created by evoxmusic on 04/12/2017.
 */
data class NotificationAck(val deviceId: String? = null,
                           val appPlatform: AppPlatform? = null,
                           val appVersion: String? = null,
                           val advertisingId: String? = null,
                           val notificationKey: String? = null,
                           val notificationAction: String? = null,
                           val location: BaseLocation? = null) {

    enum class AppPlatform {
        ANDROID, JAVA_SDK
    }

}