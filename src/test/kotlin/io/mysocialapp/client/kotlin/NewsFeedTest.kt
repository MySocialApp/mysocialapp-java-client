package io.mysocialapp.client.kotlin

import io.mysocialapp.client.FluentNewsFeed
import io.mysocialapp.client.MySocialApp
import io.mysocialapp.client.Session
import io.mysocialapp.client.models.CommentPost
import io.mysocialapp.client.models.FeedAlgorithm
import org.junit.Test
import java.io.File

/**
 * Created by evoxmusic on 05/05/2018.
 */
class NewsFeedTest {

    companion object {
        const val APP_ID = "u470584465854a728453"
    }

    private fun getSession(): Session? = MySocialApp.Builder().setAppId(APP_ID).build().blockingConnect("alice.jeith@mysocialapp.io", "myverysecretpassw0rd")

    private fun getFile(filePath: String): File {
        return File(System::class.java.getResource(filePath).file)
    }

    @Test
    fun `get some news from feed`() {
        val s = getSession()
        s?.newsFeed?.blockingList()?.forEach { feed -> println(feed.bodyMessage) }
    }

    @Test
    fun `list custom feeds`() {
        val s = getSession()

        val customAlgorithm = FeedAlgorithm.Builder()
                .setCustomFeedRequest(getFile("/custom_algorithm.json").readText())
                .build()

        s?.newsFeed?.blockingList(algorithm = customAlgorithm)?.forEach { feed -> println(feed) }
    }

    @Test
    fun `list all comments from the 10 firsts news feed`() {
        val session = getSession()

        var count = 0
        var isFinished = false

        session?.newsFeed?.stream(33)?.doOnError { it.printStackTrace() }?.subscribe({ feed ->
            count++
            println("$count - ${feed.`object`?.type} - ${feed.bodyMessage}")
        }, {
            it.printStackTrace()
        }, {
            println(count)
            isFinished = true
        })

        while (!isFinished) {
            Thread.sleep(1_000L)
        }
    }

    @Test
    fun `like news feed that have no likes`() {
        val s = getSession()

        val feedsWithoutLikes = s?.newsFeed?.blockingStream(300)?.filter { it.`object`?.likersTotal == 0 }
        feedsWithoutLikes?.forEach { it.blockingAddLike() }
    }

    @Test
    fun `find the most popular feed post on the last 100`() {
        val s = getSession()

        val lastFeeds = s?.newsFeed?.blockingStream(1000)

        val mostPopularFeed = lastFeeds?.sortedByDescending {
            it.`object`?.likersTotal?.plus((it.`object`?.commentsTotal?.times(1.5)) ?: 0.0)
        }?.firstOrNull()

        print("most popular feed on 100 last feeds is : '${mostPopularFeed?.bodyMessage}'")
    }

    @Test
    fun `add comment to first feed post`() {
        val s = getSession()

        val feed = s?.newsFeed?.blockingStream(1)?.firstOrNull()

        feed?.blockingAddComment(CommentPost.Builder().setMessage("Here a comment from the SDK :) #sdk #java " +
                "by [[user:${s.account.blockingGet().id}]] with https://mysocialapp.io").build())

        feed?.blockingAddLike()
    }

    @Test
    fun `search for feeds containing "hello"`() {
        val s = getSession()
        val results = s?.newsFeed?.blockingSearch(FluentNewsFeed.Search.Builder().setTextToSearch("hello").build())
        assert(results != null)
    }

    @Test
    fun `ignore last feed`() {
        val s = getSession()
        s?.newsFeed?.blockingStream(1)?.firstOrNull()?.blockingIgnore()
    }

    @Test
    fun `report last feed`() {
        val s = getSession()
        s?.newsFeed?.blockingStream(1)?.firstOrNull()?.blockingAbuse()
    }

    @Test
    fun `delete last feed`() {
        val s = getSession()
        s?.newsFeed?.blockingStream(1)?.firstOrNull()?.blockingDelete()
    }

}
