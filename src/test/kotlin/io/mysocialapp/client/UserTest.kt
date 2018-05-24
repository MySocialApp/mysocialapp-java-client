package io.mysocialapp.client

import io.mysocialapp.client.models.SimpleLocation
import org.junit.Test

/**
 * Created by evoxmusic on 05/05/2018.
 */
class UserTest {

    companion object {
        const val APP_ID = "u470584465854a194805"
    }

    private fun getSession(): Session? = MySocialApp.Builder()
            .setAppId(APP_ID)
            .setClientConfiguration(ClientConfiguration(30_000L))
            .build()
            .connect("AliceX", "myverysecretpassw0rd")

    @Test
    fun `get 100 users`() {
        val s = getSession()
        assert(s?.user?.blockingStream(100)?.map { it.users ?: emptyList() }?.flatten() != null)
    }

    @Test
    fun `list my news feed`() {
        val s = getSession()
        assert(s?.account?.blockingGet()?.blockingStreamNewsFeed(100)?.toList() != null)
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
        val results = s?.user?.blockingSearch(FluentUser.Search.Builder().setFirstName("romaric").build())
        assert(results != null)
    }

    @Test
    fun `search for users by location`() {
        val s = getSession()

        val parisLocation = SimpleLocation(48.85661400000001, 2.3522219000000177)
        val query = FluentUser.Search.Builder()
                .setLivingLocation(parisLocation)
                .setLivingLocationMaximumDistanceInKilometers(100.0)
                .build()

        val results = s?.user?.blockingSearch(query)

        assert(results != null)
    }

    @Test
    fun `search for users by their presentation`() {
        val s = getSession()
        val results = s?.user?.blockingSearch(FluentUser.Search.Builder().setPresentation("mysocialapp").build())
        assert(results != null)
    }

    @Test
    fun `find user by external id`() {
        val s = getSession()
        val user = s?.user?.blockingGetByExternalId("123externalID123")
        assert(user?.externalId == "123externalID123")
    }

}