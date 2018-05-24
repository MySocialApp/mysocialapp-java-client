package io.mysocialapp.client

import io.mysocialapp.client.models.Conversation
import io.mysocialapp.client.models.ConversationMessagePost
import org.junit.Test

/**
 * Created by evoxmusic on 24/05/2018.
 */
class ConversationTest {

    companion object {
        const val APP_ID = "u470584465854a194805"
    }

    private fun getSession(): Session? = MySocialApp.Builder().setAppId(APP_ID).build().connect("AliceX", "myverysecretpassw0rd")

    @Test
    fun `create conversation with 3 people`() {
        val s = getSession()

        val people = s?.user?.blockingStream(3)?.toList()?.get(0)?.users?.toSet() ?: emptySet()

        val conversation = Conversation.Builder()
                .setName("talk about the next event")
                .addMembers(people)
                .build()

        val createdConversation = s?.conversation?.blockingCreate(conversation)
        assert(createdConversation != null)
    }

    @Test
    fun `kick one person from this conversation`() {
        val s = getSession()
        val acc = s?.account?.blockingGet()
        val conversion = s?.conversation?.blockingList()?.firstOrNull()
        val kickedMember = conversion?.members?.find { it.id != acc?.id }?.let { conversion.blockingKickMember(it) }
        assert(kickedMember != null)
    }

    @Test
    fun `invite someone to this conversation`() {
        val s = getSession()
        val acc = s?.account?.blockingGet()

        // take 100 users, shuffle them, take the first that is not myself
        val userToInvite = s?.user?.blockingStream(100)?.map { it.users }?.flatMap {
            it?.asIterable() ?: emptyList()
        }?.shuffled()?.find { it.id != acc?.id }


        val conversion = s?.conversation?.blockingList()?.firstOrNull()

        val addedMember = userToInvite?.let { conversion?.blockingAddMember(it) }
        assert(addedMember != null)
    }

    @Test
    fun `rename the conversation`() {
        val s = getSession()
        val conversion = s?.conversation?.blockingList()?.firstOrNull()
        conversion?.name = "new conversation title :)"
        conversion?.blockingSave()

        assert(s?.conversation?.blockingGet(conversion?.id ?: -1L)?.name == "new conversation title :)")
    }

    @Test
    fun `get my conversations`() {
        val s = getSession()
        val conversations = s?.conversation?.blockingList()?.toList()
        assert(conversations != null)
    }

    @Test
    fun `get conversation messages`() {
        val s = getSession()
        val conversationMessages = s?.conversation?.blockingList()?.firstOrNull()?.messages?.blockingStream(33)?.toList()
        assert(conversationMessages != null)
    }

    @Test
    fun `get unread conversation messages`() {
        val s = getSession()
        val conversationMessages = s?.conversation?.blockingList()?.firstOrNull()?.messages?.blockingStreamAndConsume(33)?.toList()
        assert(conversationMessages != null)
    }

    @Test
    fun `add message to the last conversation`() {
        val s = getSession()
        val lastConversation = s?.conversation?.blockingList()?.firstOrNull()

        val message = ConversationMessagePost.Builder()
                .setMessage("Hello, this is a message from our SDK")
                .build()

        val messageSent = lastConversation?.blockingSendMessage(message)
        assert(messageSent != null)
    }

    @Test
    fun `quit the last conversation`() {
        val s = getSession()
        val conversation = s?.conversation?.blockingList()?.firstOrNull()
        conversation?.blockingQuit()
        assert(s?.conversation?.blockingList()?.firstOrNull()?.id != conversation?.id)
    }


}