package io.mysocialapp.client.models

import java.io.Serializable

/**
 * Created by evoxmusic on 01/09/2017.
 */
class UserSettings(var statStatusEnabled: Boolean? = true,
                   var notification: Notification? = Notification(),
                   var languageZone: LanguageZone? = LanguageZone.FR,
                   var interfaceLanguage: InterfaceLanguage? = InterfaceLanguage.FR) : Serializable {

    data class Notification(var enabled: Boolean? = true,
                            var pushEnabled: Boolean? = true,
                            var mailEnabled: Boolean? = true,
                            var mailFrequency: MailFrequency? = MailFrequency.REAL_TIME,
                            var eventEnabled: Boolean? = true,
                            var maximumDistance: Int? = 75000,
                            var mentionEnabled: Boolean? = true,
                            var messagingEnabled: Boolean? = true,
                            var commentEnabled: Boolean? = true,
                            var likeEnabled: Boolean? = true,
                            var offerEnabled: Boolean? = true,
                            var soundEnabled: Boolean? = true,
                            var newsletterEnabled: Boolean? = true) : Serializable {

        enum class MailFrequency {
            NEVER, REAL_TIME, DAILY, WEEKLY
        }

    }

    enum class InterfaceLanguage {
        FR, EN, DE
    }

    enum class LanguageZone {
        FR, EN, DE
    }

}