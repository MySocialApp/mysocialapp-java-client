package io.mysocialapp.client.kotlin

import io.mysocialapp.client.MySocialApp
import io.mysocialapp.client.Session
import org.junit.Test

/**
 * Created by evoxmusic on 27/04/2018.
 */
class FriendTest {

    companion object {
        const val APP_ID = "u470584465854a728453"
    }

    private fun getSession(): Session? = MySocialApp.Builder().setAppId(APP_ID).build().blockingConnect("AliceX", "myverysecretpassw0rd")

    @Test
    fun `accept all incoming friends requests`() {
        val s = getSession()
        assert(s?.friend?.blockingListIncomingFriendRequests()?.map { it.blockingAcceptFriendRequest() } != null)
    }

    @Test
    fun `get friends`() {
        val s = getSession()
        assert(s?.friend?.blockingList() != null)
    }

}