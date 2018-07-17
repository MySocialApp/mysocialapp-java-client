package io.mysocialapp.client.kotlin

import io.mysocialapp.client.FluentFeed
import io.mysocialapp.client.MySocialApp
import io.mysocialapp.client.Session
import io.mysocialapp.client.models.CommentPost
import org.junit.Test

/**
 * Created by evoxmusic on 05/05/2018.
 */
class FeedTest {

    companion object {
        const val APP_ID = "u470584465854a728453"
    }

    private fun getSession(): Session? = MySocialApp.Builder().setAppId(APP_ID).build().blockingConnect("AliceX", "myverysecretpassw0rd")

    @Test
    fun `get some news from feed`() {
        val s = getSession()
        s?.newsFeed?.blockingList()?.forEach { feed -> println(feed.bodyMessage) }
    }

    @Test
    fun `list all comments from the 10 firsts news feed`() {
        val session = getSession()

        var count = 0
        var isFinished = false

        session?.newsFeed?.stream(33)?.doOnError { it.printStackTrace() }?.subscribe({ feed ->
            count++
            println("$count - ${feed.baseObject?.type} - ${feed.bodyMessage}")
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

        val feedsWithoutLikes = s?.newsFeed?.blockingStream(300)?.filter { it.baseObject?.likersTotal == 0 }
        feedsWithoutLikes?.forEach { it.addBlockingLike() }
    }

    @Test
    fun `find the most popular feed post on the last 100`() {
        val s = getSession()

        val lastFeeds = s?.newsFeed?.blockingStream(1000)

        val mostPopularFeed = lastFeeds?.sortedByDescending {
            it.baseObject?.likersTotal?.plus((it.baseObject?.commentsTotal?.times(1.5)) ?: 0.0)
        }?.firstOrNull()

        print("most popular feed on 100 last feeds is : '${mostPopularFeed?.bodyMessage}'")
    }

    @Test
    fun `add comment to first feed post`() {
        val s = getSession()

        val feed = s?.newsFeed?.blockingStream(1)?.firstOrNull()

        feed?.addBlockingComment(CommentPost.Builder().setMessage("Here a comment from the SDK :) #sdk #java " +
                "by [[user:${s.account.blockingGet().id}]] with https://mysocialapp.io").build())

        feed?.addBlockingLike()
    }

    @Test
    fun `search for feeds containing "hello"`() {
        val s = getSession()
        val results = s?.newsFeed?.blockingSearch(FluentFeed.Search.Builder().setTextToSearch("hello").build())
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