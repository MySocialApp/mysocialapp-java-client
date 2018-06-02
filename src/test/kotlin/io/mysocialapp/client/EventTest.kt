package io.mysocialapp.client

import io.mysocialapp.client.models.SimpleLocation
import org.junit.Test
import java.util.*

/**
 * Created by evoxmusic on 02/06/2018.
 */
class EventTest {

    companion object {
        const val APP_ID = "u470584465854a194805"
    }

    private fun getSession(): Session? = MySocialApp.Builder()
            .setAppId(APP_ID)
            .setClientConfiguration(ClientConfiguration(30_000L))
            .build()
            .blockingConnect("AliceX", "myverysecretpassw0rd")

    @Test
    fun `get 100 next events`() {
        val s = getSession()
        assert(s?.event?.blockingStream(100)?.toList() != null)
    }

    @Test
    fun `get past events`() {
        val s = getSession()

        val query = FluentEvent.Search.Builder()
                .setFromDate(Date(0))
                .setToDate(Date())
                .build()

        assert(s?.event?.blockingSearch(query)?.toList() != null)
    }

    @Test
    fun `list my events`() {
        val s = getSession()
        assert(s?.account?.blockingGet()?.blockingStreamGroup(100)?.toList() != null)
    }

    @Test
    fun `search for events`() {
        val s = getSession()
        val results = s?.event?.blockingSearch(FluentEvent.Search.Builder().setName("test").build())
        assert(results != null)
    }

    @Test
    fun `search for events by location`() {
        val s = getSession()

        val parisLocation = SimpleLocation(48.85661400000001, 2.3522219000000177)
        val query = FluentEvent.Search.Builder()
                .setLocation(parisLocation)
                .setLocationMaximumDistanceInKilometers(100.0)
                .build()

        val results = s?.event?.blockingSearch(query)

        assert(results != null)
    }

    @Test
    fun `search for events by their description`() {
        val s = getSession()
        val results = s?.event?.blockingSearch(FluentEvent.Search.Builder().setDescription("test").build())
        assert(results != null)
    }

}