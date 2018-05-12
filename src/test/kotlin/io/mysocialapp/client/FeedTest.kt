package io.mysocialapp.client

import org.junit.Test

/**
 * Created by evoxmusic on 05/05/2018.
 */
class FeedTest {

    companion object {
        const val APP_ID = "u470584465854a194805"
    }

    private fun getSession(): Session? = MySocialApp.Builder().setAppId(APP_ID).build().connect("AliceX", "myverysecretpassw0rd")

    @Test
    fun `get some news from feed`() {
        val s = getSession()
        s?.newsFeed?.list()?.toBlocking()?.subscribe { feed -> println(feed.bodyMessage) }
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

}