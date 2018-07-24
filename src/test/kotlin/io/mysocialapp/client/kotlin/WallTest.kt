package io.mysocialapp.client.kotlin

import io.mysocialapp.client.MySocialApp
import io.mysocialapp.client.Session
import io.mysocialapp.client.models.AccessControl
import io.mysocialapp.client.models.FeedPost
import org.junit.Test
import java.io.File

/**
 * Created by evoxmusic on 09/05/2018.
 */
class WallTest {

    companion object {
        const val APP_ID = "u470584465854a728453"
    }

    private fun getSession(): Session? = MySocialApp.Builder().setAppId(APP_ID).build().blockingConnect("AliceX", "myverysecretpassw0rd")

    @Test
    fun `add simple text post on the news feed`() {
        val s = getSession()

        val post = FeedPost.Builder()
                .setMessage("hello this is a simple post visible only from my friends and me")
                .setVisibility(AccessControl.FRIEND)
                .build()

        assert(s?.newsFeed?.blockingSendWallPost(post) != null)
    }

    @Test
    fun `add text post with hashtag on the news feed`() {
        val s = getSession()

        val post = FeedPost.Builder()
                .setMessage("This is a post with #hashtag")
                .setVisibility(AccessControl.FRIEND)
                .build()

        assert(s?.newsFeed?.blockingSendWallPost(post) != null)
    }

    @Test
    fun `add text post with url on the news feed`() {
        val s = getSession()

        val post = FeedPost.Builder()
                .setMessage("This is a post with url https://mysocialapp.io")
                .setVisibility(AccessControl.FRIEND)
                .build()

        assert(s?.newsFeed?.blockingSendWallPost(post) != null)
    }

    @Test
    fun `add text post with mention on the news feed`() {
        val s = getSession()

        val post = FeedPost.Builder()
                .setMessage("This is a post with someone mentioned [[user:2375667195016462956]]")
                .setVisibility(AccessControl.FRIEND)
                .build()

        assert(s?.newsFeed?.blockingSendWallPost(post) != null)
    }

    @Test
    fun `add text post with hashtag + url + mention on the news feed`() {
        val s = getSession()

        val post = FeedPost.Builder()
                .setMessage("This is a post with #hashtag url https://mysocialapp.io and someone mentioned [[user:2375667195016462956]]")
                .setVisibility(AccessControl.FRIEND)
                .build()

        val feed = s?.newsFeed?.blockingSendWallPost(post)
        assert(feed != null)
    }

    @Test
    fun `add image post on the news feed which is only visible from my friends`() {
        val s = getSession()

        val post = FeedPost.Builder()
                .setMessage("This is a post with an image and a #hashtag :)")
                .setImage(File("/tmp/myimage.jpg"))
                .setVisibility(AccessControl.FRIEND)
                .build()

        assert(s?.newsFeed?.blockingSendWallPost(post) != null)
    }

    @Test
    fun `add text post with mention on a friend wall`() {
        val s = getSession()

        val friend = s?.account?.blockingGet()?.blockingListFriends()?.firstOrNull() ?: return

        val post = FeedPost.Builder()
                .setMessage("Hey [[user:${friend.id}]] what's up?")
                .setVisibility(AccessControl.FRIEND)
                .build()

        assert(friend.blockingCreateFeedPost(post) != null)
    }

}