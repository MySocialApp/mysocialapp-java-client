package io.mysocialapp.client

import io.mysocialapp.client.exceptions.InvalidCredentialsMySocialAppException
import org.junit.Test

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

}