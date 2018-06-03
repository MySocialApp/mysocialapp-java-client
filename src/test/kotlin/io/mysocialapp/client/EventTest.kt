package io.mysocialapp.client

import io.mysocialapp.client.models.Event
import io.mysocialapp.client.models.EventMemberAccessControl
import io.mysocialapp.client.models.SimpleLocation
import org.junit.Test
import java.io.File
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
    fun `create an event`() {
        val s = getSession()

        val newarkLocation = SimpleLocation(40.736504474883915, -74.18175405)

        val tomorrow = Calendar.getInstance().apply {
            time = Date()
            add(Calendar.DATE, 1)
        }

        val afterTomorrow = Calendar.getInstance().apply {
            time = Date()
            add(Calendar.DATE, 2)
        }

        val event = Event.Builder()
                .setName("New test event")
                .setDescription("This is a new event create with our SDK")
                .setStartDate(tomorrow.time)
                .setEndDate(afterTomorrow.time)
                .setLocation(newarkLocation)
                .setMaxSeats(100)
                .setMemberAccessControl(EventMemberAccessControl.PUBLIC)
                .setCoverImage(File("/tmp/image.jpg"))
                .build()

        assert(s!!.event.blockingCreate(event) != null)
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
        val myEvents = s?.account?.blockingGet()?.blockingStreamEvent(10)?.toList()

        assert(myEvents != null)
    }

    @Test
    fun `cancel event`() {
        val s = getSession()
        val event = s?.account?.blockingGet()?.blockingStreamEvent(1)?.firstOrNull()
        event?.blockingCancel()

        assert(s?.account?.blockingGet()?.blockingStreamEvent(1)?.firstOrNull()?.cancelled == true)
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

    @Test
    fun `list members from the first event`() {
        val s = getSession()
        val event = s?.event?.blockingStream(1)?.first()
        assert(event?.members != null)
    }

}