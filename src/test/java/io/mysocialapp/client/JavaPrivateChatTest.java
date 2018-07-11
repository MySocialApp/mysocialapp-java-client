package io.mysocialapp.client;

import io.mysocialapp.client.models.Conversation;
import io.mysocialapp.client.models.ConversationMessage;
import io.mysocialapp.client.models.ConversationMessagePost;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.io.File;

/**
 * Created by evoxmusic on 11/05/2018.
 */
class JavaPrivateChatTest {

    private final static String APP_ID = "u470584465854a728453";
    private static Session johnSession;
    private static Session aliceSession;
    private static Session jackSession;

    static {
        johnSession = new MySocialApp.Builder().setAppId(APP_ID).build().blockingConnect("John", "mySecretPassword");
        aliceSession = new MySocialApp.Builder().setAppId(APP_ID).build().blockingConnect("Alice", "mySecretPassword");
        jackSession = new MySocialApp.Builder().setAppId(APP_ID).build().blockingConnect("Jack", "mySecretPassword");
    }

    @Test
    void letsChatTogether() {
        // john create private conversation with Alice and Jack
        Conversation conv = new Conversation.Builder()
                .setName("Chat between Alice, Jack and John")
                .addMember(aliceSession.getAccount().blockingGet())
                .addMember(jackSession.getAccount().blockingGet())
                .build();

        Conversation conversation = johnSession.getConversation().blockingCreate(conv);

        startJohnListener();
        startAliceListener();
        startJackListener();

        // john start chatting with the first message
        conversation.blockingSendMessage(new ConversationMessagePost.Builder().setMessage("Hello here").build());
    }

    private void startJohnListener() {
        // listen for private messages from Alice and Jack
        johnSession.getNotification().addNotificationListener(new AbstractNotificationCallback() {

            @Override
            public void onConversationMessage(@NotNull ConversationMessage conversationMessage) {
                // called when John receive private message

                // add delay to reply
                simulateUserTypingDelay();

                ConversationMessagePost message = new ConversationMessagePost.Builder()
                        .setMessage("Hey [[user:" + conversationMessage.getOwner().getId() + "]] 👋")
                        .build();

                conversationMessage.blockingReplyBack(message);
            }

        });
    }

    private void startAliceListener() {
        // listen for private messages from John and Jack
        aliceSession.getNotification().addNotificationListener(new AbstractNotificationCallback() {

            @Override
            public void onConversationMessage(@NotNull ConversationMessage conversationMessage) {
                // called when Alice receive private message

                // add delay to reply
                simulateUserTypingDelay();

                ConversationMessagePost message = new ConversationMessagePost.Builder()
                        .setMessage("Look at https://www.nytimes.com/2018/07/04/sports/world-cup/benjamin-pavard-france.html " +
                                "[[user:" + conversationMessage.getOwner().getId() + "]]")
                        .build();

                conversationMessage.blockingReplyBack(message);
            }

        });
    }

    private void startJackListener() {
        // listen for private messages from Alice and John
        jackSession.getNotification().addNotificationListener(new AbstractNotificationCallback() {

            @Override
            public void onConversationMessage(@NotNull ConversationMessage conversationMessage) {
                // called when Jack receive private message

                // add delay to reply
                simulateUserTypingDelay();

                ConversationMessagePost message = new ConversationMessagePost.Builder()
                        .setMessage("Wow incredible #pavard")
                        .setImage(new File("/tmp/pavard.jpg"))
                        .build();

                conversationMessage.blockingReplyBack(message);
            }

        });
    }

    private void simulateUserTypingDelay() {
        try {
            Thread.sleep(randomTime());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private long randomTime() {
        long leftLimit = 300L;
        long rightLimit = 3200L;
        return leftLimit + (long) (Math.random() * (rightLimit - leftLimit));
    }

}
