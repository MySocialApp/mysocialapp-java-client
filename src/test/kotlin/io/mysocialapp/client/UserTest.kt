package io.mysocialapp.client

import org.junit.Test

/**
 * Created by evoxmusic on 05/05/2018.
 */
class UserTest {

    companion object {
        const val APP_ID = "u470584465854a269772"
    }

    private fun getSession(): Session? = MySocialApp.Builder().setAppId(APP_ID).build().connect("AliceX", "myverysecretpassw0rd")

    @Test
    fun `get 100 users`() {
        val s = getSession()
        assert(s?.user?.blockingStream(100)?.map { it.users ?: emptyList() }?.flatten() != null)
    }

    @Test
    fun `list my news feed`() {
        val s = getSession()
        assert(s?.account?.blockingGet()?.blockingStreamNewsFeed(100) != null)
    }

    @Test
    fun `list news feed from people`() {
        val s = getSession()
        val users = s?.user?.blockingStream(10)?.map { it.users ?: emptyList() }?.flatten()
        assert(users?.map { it.blockingStreamNewsFeed(10) }?.flatten() != null)
    }

    @Test
    fun `list some users friends`() {
        val s = getSession()
        val users = s?.user?.blockingStream(3)?.map { it.users ?: emptyList() }?.flatten()
        assert(users?.map { it.blockingListFriends().map { it.displayedName } }?.flatten() != null)
    }

    @Test
    fun `request some friends then cancel`() {
        val s = getSession()
        val users = s?.user?.blockingStream(3)?.map { it.users ?: emptyList() }?.flatten()
        assert(users?.map { it.blockingRequestAsFriend() }?.size == 3)

        users?.map { it.blockingCancelFriendRequest() } != null
    }

    @Test
    fun `search for users`() {
        val s = getSession()
        assert(s?.user?.blockingSearch(FluentUser.Search.Builder().setFirstName("romaric").build()) != null)
    }

}