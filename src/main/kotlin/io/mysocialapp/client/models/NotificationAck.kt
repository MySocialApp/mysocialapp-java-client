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
        ANDROID,
        SDK
    }

    class Builder {
        private var mDeviceId: String? = null
        private var mAppPlatform: AppPlatform? = null
        private var mAppVersion: String? = null
        private var mAdvertisingId: String? = null
        private var mNotificationKey: String? = null
        private var mNotificationAction: String? = null
        private var mLocation: BaseLocation? = null

        fun setDeviceId(deviceId: String): Builder {
            this.mDeviceId = deviceId
            return this
        }

        fun setAppPlatform(appPlatform: AppPlatform): Builder {
            this.mAppPlatform = appPlatform
            return this
        }

        fun setAppVersion(appVersion: String): Builder {
            this.mAppVersion = appVersion
            return this
        }

        fun setAdvertisingId(advertisingId: String): Builder {
            this.mAdvertisingId = advertisingId
            return this
        }

        fun setNotificationKey(notificationKey: String): Builder {
            this.mNotificationKey = notificationKey
            return this
        }

        fun setNotificationAction(notificationAction: String): Builder {
            this.mNotificationAction = notificationAction
            return this
        }

        fun setLocation(location: BaseLocation): Builder {
            this.mLocation = location
            return this
        }

        fun build(): NotificationAck = NotificationAck(mDeviceId, mAppPlatform, mAppVersion, mAdvertisingId,
                mNotificationKey, mNotificationAction, mLocation)
    }

}