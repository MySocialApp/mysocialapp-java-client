package io.mysocialapp.client.kotlin

import io.mysocialapp.client.ClientConfiguration
import io.mysocialapp.client.FluentEvent
import io.mysocialapp.client.MySocialApp
import io.mysocialapp.client.Session
import io.mysocialapp.client.models.CustomField
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
        const val APP_ID = "u470584465854a728453"
    }

    private fun getSession(): Session = MySocialApp.Builder()
            .setAppId(APP_ID)
            .setClientConfiguration(ClientConfiguration(30_000L))
            .build()
            .blockingConnect("alice.jeith@mysocialapp.io", "myverysecretpassw0rd")

    private fun getFile(filePath: String): File {
        return File(System::class.java.getResource(filePath).file)
    }

    @Test
    fun `get 100 next events`() {
        val s = getSession()
        val events = s.event.blockingStream(100).toList()
        assert(events != null)
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

        val customFields = s.event.blockingGetAvailableCustomFields().map { customField ->
            customField.value = when (customField.fieldType) {
                CustomField.FieldType.INPUT_TEXT -> "Text test"
                CustomField.FieldType.INPUT_TEXTAREA -> "TextArea test text"
                CustomField.FieldType.INPUT_NUMBER -> 1337
                CustomField.FieldType.INPUT_BOOLEAN -> false
                CustomField.FieldType.INPUT_DATE -> Date()
                CustomField.FieldType.INPUT_URL -> "https://mysocialapp.io"
                CustomField.FieldType.INPUT_EMAIL -> "test@mysocialapp.io"
                CustomField.FieldType.INPUT_PHONE -> "+33123452345"
                CustomField.FieldType.INPUT_LOCATION -> newarkLocation
                CustomField.FieldType.INPUT_SELECT -> customField.possibleValues?.firstOrNull()
                CustomField.FieldType.INPUT_CHECKBOX -> customField.possibleValues?.take(2)
                null -> null
            }

            customField
        }

        val event = Event.Builder()
                .setName("New test event")
                .setDescription("This is a new event create with our SDK")
                .setStartDate(tomorrow.time)
                .setEndDate(afterTomorrow.time)
                .setLocation(newarkLocation)
                .setMaxSeats(100)
                .setMemberAccessControl(EventMemberAccessControl.PUBLIC)
                .setCoverImage(getFile("/cover_image.jpg"))
                .setCustomFields(customFields)
                .build()

        assert(s.event.blockingCreate(event) != null)
    }

    @Test
    fun `get past events`() {
        val s = getSession()

        val query = FluentEvent.Search.Builder()
                .setFromDate(Date(0))
                .setToDate(Date())
                .build()

        assert(s.event.blockingSearch(query).toList() != null)
    }

    @Test
    fun `list events by location`() {
        val s = getSession()

        val madridLocation = SimpleLocation(40.416775, -3.703790)
        val eventsNearestMadrid = s.event.blockingStream(10, FluentEvent.Options.Builder().setLocation(madridLocation).build()).toList()

        val berlinLocation = SimpleLocation(52.520008, 13.404954)
        val eventsNearestBerlin = s.event.blockingStream(10, FluentEvent.Options.Builder().setLocation(berlinLocation).build()).toList()

        val madridFirstEvent = eventsNearestMadrid.firstOrNull()
        assert(madridFirstEvent?.distanceInMeters != eventsNearestBerlin.find { it.id == madridFirstEvent?.id }?.distanceInMeters)
    }

    @Test
    fun `list my events`() {
        val s = getSession()
        val myEvents = s.account.blockingGet().blockingStreamEvent(22).toList()

        assert(myEvents != null)
    }

    @Test
    fun `list events with limited=false`() {
        val s = getSession()

        val events = s.event.blockingStream(10, FluentEvent.Options.Builder().setLimited(false).build())
        assert(events.firstOrNull()?.members?.firstOrNull()?.user?.isFriend != null)
    }

    @Test
    fun `cancel event`() {
        val s = getSession()
        val event = s.account.blockingGet().blockingStreamEvent(1).firstOrNull()
        event?.blockingCancel()

        assert(s.account.blockingGet().blockingStreamEvent(1).firstOrNull()?.isCancelled == true)
    }

    @Test
    fun `search for events`() {
        val s = getSession()
        val results = s.event.blockingSearch(FluentEvent.Search.Builder().setName("test").build())
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

        val results = s.event.blockingSearch(query)

        assert(results != null)
    }

    @Test
    fun `search for events by their description`() {
        val s = getSession()
        val results = s.event.blockingSearch(FluentEvent.Search.Builder().setDescription("test").build())
        assert(results != null)
    }

    @Test
    fun `list members from the first event`() {
        val s = getSession()
        val event = s.event.blockingStream(1, FluentEvent.Options.Builder().setLimited(false).build()).first()
        assert(event.members != null)
    }

    @Test
    fun `list event custom fields`() {
        val s = getSession()
        val customFields = s.event.blockingGetAvailableCustomFields().toList()
        assert(customFields != null)
    }

    @Test
    fun `check event member count on un-join`() {
        val s = getSession()
        val event = s.account.blockingGet().blockingStreamEvent(1).firstOrNull() ?: return

        val totalMembers = event.totalMembers

        assert(event.blockingUnParticipate()?.event?.totalMembers == (totalMembers ?: 0) - 1)
    }

}