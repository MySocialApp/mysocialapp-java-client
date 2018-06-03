package io.mysocialapp.client

import io.mysocialapp.client.models.Group
import io.mysocialapp.client.models.GroupMemberAccessControl
import io.mysocialapp.client.models.SimpleLocation
import org.junit.Test
import java.io.File

/**
 * Created by evoxmusic on 01/06/2018.
 */
class GroupTest {

    companion object {
        const val APP_ID = "u470584465854a194805"
    }

    private fun getSession(): Session? = MySocialApp.Builder()
            .setAppId(APP_ID)
            .setClientConfiguration(ClientConfiguration(30_000L))
            .build()
            .blockingConnect("AliceX", "myverysecretpassw0rd")

    @Test
    fun `get 100 groups`() {
        val s = getSession()
        assert(s?.group?.blockingStream(100)?.toList() != null)
    }

    @Test
    fun `create a group`() {
        val s = getSession()

        val newarkLocation = SimpleLocation(-22.9098755, -43.20949710000002)

        val group = Group.Builder()
                .setName("New test event")
                .setDescription("This is a new event create with our SDK")
                .setLocation(newarkLocation)
                .setMemberAccessControl(GroupMemberAccessControl.PUBLIC)
                .setImage(File("/tmp/image.jpg"))
                .build()

        assert(s!!.group.blockingCreate(group) != null)
    }

    @Test
    fun `list my groups`() {
        val s = getSession()
        assert(s?.account?.blockingGet()?.blockingStreamGroup(100)?.toList() != null)
    }

    @Test
    fun `search for groups`() {
        val s = getSession()
        val results = s?.group?.blockingSearch(FluentGroup.Search.Builder().setName("test").build())
        assert(results != null)
    }

    @Test
    fun `search for groups by location`() {
        val s = getSession()

        val parisLocation = SimpleLocation(48.85661400000001, 2.3522219000000177)
        val query = FluentGroup.Search.Builder()
                .setLocation(parisLocation)
                .setLocationMaximumDistanceInKilometers(100.0)
                .build()

        val results = s?.group?.blockingSearch(query)

        assert(results != null)
    }

    @Test
    fun `search for groups by their description`() {
        val s = getSession()
        val results = s?.group?.blockingSearch(FluentGroup.Search.Builder().setDescription("test").build())
        assert(results != null)
    }

    @Test
    fun `list members from the first group`() {
        val s = getSession()
        val group = s?.group?.blockingStream(1)?.first()
        assert(group?.members != null)
    }

}