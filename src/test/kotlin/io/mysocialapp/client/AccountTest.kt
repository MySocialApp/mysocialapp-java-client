package io.mysocialapp.client

import io.mysocialapp.client.exceptions.InvalidCredentialsMySocialAppException
import io.mysocialapp.client.models.CustomField
import io.mysocialapp.client.models.SimpleLocation
import org.junit.Test
import java.io.File
import java.util.*

/**
 * Created by evoxmusic on 27/04/2018.
 */
class AccountTest {

    companion object {
        const val APP_ID = "u470584465854a194805"
    }

    private fun getBadSession() = MySocialApp.Builder().setAppId(APP_ID).build().connect("alicex@mysocialapp.io", "mybadpassword")

    private fun getBlockingBadSession() = MySocialApp.Builder().setAppId(APP_ID).build().blockingConnect("alicex@mysocialapp.io", "mybadpassword")

    private fun getSession(): Session? = MySocialApp.Builder().setAppId(APP_ID).build().blockingConnect("alicex@mysocialapp.io", "myverysecretpassw0rd")

    @Test
    fun `create account`() {
        val msa = MySocialApp.Builder().setAppId(APP_ID).build()

        val s = try {
            msa.blockingCreateAccount("alicex@mysocialapp.io", "myverysecretpassw0rd", "AliceX")
        } catch (e: Exception) {
            msa.blockingConnect("alicex@mysocialapp.io", "myverysecretpassw0rd")
        }

        assert(s?.authenticationToken?.accessToken?.isNotEmpty() == true)
    }

    @Test
    fun `get authentication token`() {
        val s = getSession()
        assert(s?.authenticationToken?.accessToken?.isNotEmpty() == true)
    }


    @Test
    fun `get not existing account`() {
        getBadSession().subscribe({
            println("good credentials")
        }) { throwable ->
            if (throwable is InvalidCredentialsMySocialAppException) {
                println("bad credentials")
                return@subscribe
            }

            println(throwable.message)
        }

        try {
            getBlockingBadSession()
        } catch (e: InvalidCredentialsMySocialAppException) {
            println("bad credentials with blocking")
        }
    }

    @Test
    fun `get account`() {
        assert(getSession()?.account?.blockingGet()?.id != null)
    }

    @Test
    fun `set account properties, save them, retrieve them`() {
        val s = getSession()

        s?.account?.blockingGet()?.apply {
            lastName = "Josh"
            blockingSave()
        }

        assert(s?.account?.blockingGet()?.lastName == "Josh")

        s?.account?.blockingGet()?.apply {
            lastName = "JoshX"
            externalId = "123externalID123"
            blockingSave()
        }

        assert(s?.account?.blockingGet()?.lastName == "JoshX")
    }

    @Test
    fun `update account custom fields properties`() {
        val s = getSession()

        val newarkLocation = SimpleLocation(-22.9098755, -43.20949710000002)

        val customFields = s?.account?.blockingGetAvailableCustomFields()?.map { customField ->
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

        val acc = s?.account?.blockingGet()
        acc?.customFields = customFields
        acc?.blockingSave()

        val savedCustomFields = s?.account?.blockingGet()?.customFields
        assert(savedCustomFields != null)
    }

    @Test
    fun `update profile photo`() {
        val s = getSession()

        s?.account?.blockingChangeProfilePhoto(File("/tmp/my_profile.png"))
        assert(s?.account?.blockingGet()?.profilePhoto != null)
    }

    @Test
    fun `update cover photo`() {
        val s = getSession()

        s?.account?.blockingChangeProfileCoverPhoto(File("/tmp/my_image.jpg"))
        assert(s?.account?.blockingGet()?.profileCoverPhoto != null)
    }

    @Test
    fun `take the living location of first feed owner, and set it has my own living location`() {
        val s = getSession()

        val feed = s?.newsFeed?.blockingStream(1)?.first()

        s?.account?.blockingGet()?.apply {
            livingLocation = feed?.owner?.livingLocation
            blockingSave()
        }

        val mAcc = s?.account?.blockingGet()
        assert(mAcc?.livingLocation?.latitude == feed?.owner?.livingLocation?.latitude)
        assert(mAcc?.livingLocation?.longitude == feed?.owner?.livingLocation?.longitude)
    }

    @Test
    fun `accept all friends requests`() {
        val s = getSession()

        s?.account?.blockingGet()?.blockingListFriends()
    }

    @Test
    fun `change my notification settings`() {
        val s = getSession()

        val acc = s?.account?.blockingGet()
        val originalValue = acc?.userSettings?.notification?.newsletterEnabled ?: false

        acc?.userSettings?.notification?.newsletterEnabled = !originalValue
        val receivedAccount = acc?.blockingSave()

        assert(receivedAccount?.userSettings?.notification?.newsletterEnabled == !originalValue)
    }

    @Test
    fun `list account custom fields`() {
        val s = getSession()
        val customFields = s?.account?.blockingGetAvailableCustomFields()?.toList()
        assert(customFields != null)
    }

}