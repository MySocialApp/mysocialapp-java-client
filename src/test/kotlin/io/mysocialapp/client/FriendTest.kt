package io.mysocialapp.client

import org.junit.Test

/**
 * Created by evoxmusic on 27/04/2018.
 */
class FriendTest {

    companion object {
        const val APP_ID = "u470584465854a194805"
    }

    private fun getSession(): Session? = MySocialApp.Builder().setAppId(APP_ID).build().connect("AliceX", "myverysecretpassw0rd")

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