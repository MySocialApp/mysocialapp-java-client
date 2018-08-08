package io.mysocialapp.client.java;

import io.mysocialapp.client.AbstractNotificationCallback;
import io.mysocialapp.client.MySocialApp;
import io.mysocialapp.client.Session;
import io.mysocialapp.client.models.*;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

/**
 * Created by evoxmusic on 10/05/2018.
 */
class ReceivingEventNotificationTest {

    private final static String APP_ID = "u470584465854a728453";
    private final static String EMAIL = "alice.jeith@mysocialapp.io";
    private final static String PASSWORD = "myverysecretpassw0rd";
    private final static String EMAIL_2 = "john.clemz@mysocialapp.io";

    private final static MySocialApp msa = new MySocialApp.Builder()
            .setAppId(APP_ID)
            .build();

    private final static Session session = msa.blockingConnect(EMAIL, PASSWORD);
    private final static Session bSession = msa.blockingConnect(EMAIL_2, PASSWORD);

    @Test
    void signUp2() {
        Session s = new MySocialApp.Builder()
                .setAppId(APP_ID)
                .build()
                .blockingCreateAccount(EMAIL_2, PASSWORD, "John");

        Account account = s.getAccount().blockingGet();
        account.setLastName("Clemz");
        account.blockingSave();
    }

    @Test
    void listenForNewsFeed() {
        session.getNotification().addNotificationListener(new AbstractNotificationCallback() {

            @Override
            public void onNewsFeed(@NotNull Feed feed) {
                Assert.assertEquals(feed.getOwner().getId(), bSession.getAccount().blockingGet().getId());
            }

        });

        FeedPost feedPost = new FeedPost.Builder()
                .setMessage("Hey this is an incredible message")
                .setVisibility(AccessControl.PUBLIC)
                .build();

        bSession.getNewsFeed().blockingCreate(feedPost);
    }

    @Test
    void listenForNotificationEvents() {

        session.getNotification().addNotificationListener(new AbstractNotificationCallback() {

            @Override
            public void onNewsFeed(@NotNull Feed feed) {
                // called when my friends or someone that I matter published a new content
            }

            @Override
            public void onComment(@NotNull Comment comment) {
                // called when someone comments on a post that belongs to me or on which I have interacted
            }

            @Override
            public void onLike(@NotNull Like like) {
                // called when someone likes on a post that belongs to me or on which I have interacted
            }

            @Override
            public void onMention(@NotNull Feed feed) {
                // called when someone mention me on a post
            }

            @Override
            public void onMention(@NotNull Comment comment) {
                // called when someone mention me on a comment
            }

            @Override
            public void onConversationMessage(@NotNull ConversationMessage conversationMessage) {
                // called when someone chat with me in private
            }

            @Override
            public void onFriendRequest(@NotNull User user) {
                // called when someone request me as friend
            }

            @Override
            public void onFriend(@NotNull User user) {
                // called when someone accept me as friend
            }

            @Override
            public void onEvent(@NotNull Event event) {
                // called when a new event near to me has been created
            }

            @Override
            public void onUserJoinMyEvent(@NotNull Event event) {
                // called when user receive an event invitation
            }

            @Override
            public void onJoinEventConfirmation(@NotNull Event event) {
                // called when user receive event confirmation
            }

            @Override
            public void onGroup(@NotNull Group group) {
                // called when a new group near to me has been created
            }

        });
    }

}
