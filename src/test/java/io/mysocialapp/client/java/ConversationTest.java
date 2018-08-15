package io.mysocialapp.client.java;

import io.mysocialapp.client.AbstractNotificationCallback;
import io.mysocialapp.client.MySocialApp;
import io.mysocialapp.client.Session;
import io.mysocialapp.client.models.Conversation;
import io.mysocialapp.client.models.ConversationMessage;
import io.mysocialapp.client.models.ConversationMessagePost;
import io.mysocialapp.client.models.User;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Set;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by evoxmusic on 31/07/2018.
 */
public class ConversationTest {

    private final static String APP_ID = "u470584465854a728453";
    private final static String EMAIL = "alice.jeith@mysocialapp.io";
    private final static String PASSWORD = "myverysecretpassw0rd";

    private final static String JOHN_EMAIL = "john.paul@mysocialapp.io";

    private final static Session session = new MySocialApp.Builder()
            .setAppId(APP_ID)
            .build()
            .blockingConnect(EMAIL, PASSWORD);

    private final static Session session2 = new MySocialApp.Builder()
            .setAppId(APP_ID)
            .build()
            .blockingConnect(JOHN_EMAIL, PASSWORD);

    private File getFile(String filePath) {
        return new File(System.class.getResource(filePath).getFile());
    }

    private Conversation getFirstConversation() {
        return StreamSupport.stream(session.getConversation().blockingStream(1).spliterator(), false).findFirst().get();
    }

    @Test
    void listConversations() {
        Iterable<Conversation> conversations = session.getConversation().blockingList(0, 10);
        assertNotNull(conversations);
    }

    @Test
    void createConversation() {
        User user = session.getUser().blockingStream(1).iterator().next().getUsers().get(0);

        Conversation conversation = new Conversation.Builder()
                .setName("SDK conversation")
                .addMember(user)
                .build();

        Conversation createdConversation = session.getConversation().blockingCreate(conversation);
        assertNotNull(createdConversation);
    }

    @Test
    void updateConversation() {
        User user = session.getUser().blockingStream(1).iterator().next().getUsers().get(0);

        Conversation conversation = new Conversation.Builder()
                .setName("SDK conversation")
                .addMember(user)
                .build();

        Conversation createdConversation = session.getConversation().blockingCreate(conversation);

        String cName = "Changed conversation name";
        createdConversation.setName(cName);
        createdConversation.blockingSave();

        assertEquals(session.getConversation().blockingGet(createdConversation.getId()).getName(), cName);
    }

    @Test
    void listMembersFromConversation() {
        Conversation conversation = getFirstConversation();
        Set<User> members = conversation.getMembers();
        assertTrue(members.size() > 0);
    }

    @Test
    void addMember() {

    }

    @Test
    void kickMember() {

    }

    @Test
    void listMessages() {
        Iterable<ConversationMessage> messages = getFirstConversation().getMessages().blockingStream(100);
        for (ConversationMessage conversationMessage : messages) {
            System.out.println(conversationMessage.getMessage());
        }
    }

    @Test
    void postMessage() {
        ConversationMessagePost conversationMessagePost = new ConversationMessagePost.Builder()
                .setMessage("This is a conversation message from SDK #top")
                .build();

        getFirstConversation().blockingSendMessage(conversationMessagePost);
    }

    @Test
    void postMessage_with_image() {
        ConversationMessagePost conversationMessagePost = new ConversationMessagePost.Builder()
                .setMessage("This is a conversation message with Image from SDK #yeah")
                .setImage(getFile("/hello.jpg"))
                .build();

        getFirstConversation().blockingSendMessage(conversationMessagePost);
    }

    @Test
    void quitConversation() {
        getFirstConversation().blockingQuit();
    }

    @Test
    void getUnreadMessages() {
        getFirstConversation().getMessages().getTotalUnreads();
    }

    @Test
    void handleConversationMessage() throws InterruptedException {
        session.getNotification().addNotificationListener(new AbstractNotificationCallback() {
            @Override
            public void onConversationMessage(@NotNull ConversationMessage conversationMessage) {
                System.out.println("received message: \"" + conversationMessage.getMessage()
                        + "\" from " + conversationMessage.getOwner().getDisplayedName());
            }
        });

        // message sent from another user
        User user = session2.getUser().blockingGet(session.getAccount().blockingGet().getId());

        ConversationMessagePost cmp = new ConversationMessagePost.Builder()
                .setMessage("hey [[user:" + user.getId() + "]]")
                .build();

        user.blockingSendPrivateMessage(cmp);

        Thread.sleep(10000L);
    }

    @Test
    void getTotalUnreadConversations() {
        assertNotNull(session.getConversation().blockingTotalUnread());
    }

}
