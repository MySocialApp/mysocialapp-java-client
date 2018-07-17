package io.mysocialapp.client.kotlin

import io.mysocialapp.client.ClientConfiguration
import io.mysocialapp.client.FluentGroup
import io.mysocialapp.client.MySocialApp
import io.mysocialapp.client.Session
import io.mysocialapp.client.models.CustomField
import io.mysocialapp.client.models.Group
import io.mysocialapp.client.models.GroupMemberAccessControl
import io.mysocialapp.client.models.SimpleLocation
import org.junit.Test
import java.io.File
import java.util.*

/**
 * Created by evoxmusic on 01/06/2018.
 */
class GroupTest {

    companion object {
        const val APP_ID = "u470584465854a728453"
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

        val customFields = s?.group?.blockingGetAvailableCustomFields()?.map { customField ->
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

        val group = Group.Builder()
                .setName("New test event")
                .setDescription("This is a new event create with our SDK")
                .setLocation(newarkLocation)
                .setMemberAccessControl(GroupMemberAccessControl.PUBLIC)
                .setImage(File("/tmp/image.jpg"))
                .setCustomFields(customFields)
                .build()

        assert(s!!.group.blockingCreate(group) != null)
    }

    @Test
    fun `list my groups`() {
        val s = getSession()
        assert(s?.account?.blockingGet()?.blockingStreamGroup(100)?.toList() != null)
    }

    @Test
    fun `list groups by location`() {
        val s = getSession()

        val madridLocation = SimpleLocation(40.416775, -3.703790)
        val groupsNearestMadrid = s?.group?.blockingStream(10, FluentGroup.Options.Builder().setLocation(madridLocation).build())?.toList()

        val berlinLocation = SimpleLocation(52.520008, 13.404954)
        val groupsNearestBerlin = s?.group?.blockingStream(10, FluentGroup.Options.Builder().setLocation(berlinLocation).build())?.toList()

        val madridFirstEvent = groupsNearestMadrid?.firstOrNull()
        assert(madridFirstEvent?.distanceInMeters != groupsNearestBerlin?.find { it.id == madridFirstEvent?.id }?.distanceInMeters)
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

    @Test
    fun `list group custom fields`() {
        val s = getSession()
        val customFields = s?.group?.blockingGetAvailableCustomFields()?.toList()
        assert(customFields != null)
    }

}