package io.mysocialapp.client

import org.junit.Test

/**
 * Created by evoxmusic on 09/05/2018.
 */
class NotificationTest {

    companion object {
        const val APP_ID = "u470584465854a194805"
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

}