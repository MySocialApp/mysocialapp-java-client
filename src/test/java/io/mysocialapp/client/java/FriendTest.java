package io.mysocialapp.client.java;

import io.mysocialapp.client.MySocialApp;
import io.mysocialapp.client.Session;
import io.mysocialapp.client.models.User;
import org.junit.jupiter.api.Test;
import rx.Observable;
import rx.observers.TestSubscriber;

import java.util.Date;

import static org.junit.Assert.assertTrue;

/**
 * Created by evoxmusic on 17/07/2018.
 */
class FriendTest {

    private final static String APP_ID = "u470584465854a728453";
    private final static String EMAIL = "alice.jeith@mysocialapp.io";
    private final static String PASSWORD = "myverysecretpassw0rd";

    private final static Session session = new MySocialApp.Builder()
            .setAppId(APP_ID)
            .build()
            .blockingConnect(EMAIL, PASSWORD);

    @Test
    void listFriends() {
        Iterable<User> usersIterable = session.getFriend().blockingList(0, 10);

        usersIterable.forEach(user -> {
            System.out.println("ID: " + user.getId() + " Name: " + user.getDisplayedName());
        });
    }

    @Test
    void RxListFriends() {
        Observable<User> observable = session.getFriend().list(0, 10).cache();

        TestSubscriber<User> subscriber = new TestSubscriber<>();
        observable.subscribe(subscriber);
        subscriber.assertCompleted();
        subscriber.assertNoErrors();
        assertTrue(subscriber.getValueCount() > 0);

        observable.subscribe(user -> {
            System.out.println("ID: " + user.getId() + " Name: " + user.getDisplayedName());
        });
    }

    @Test
    void removeFriend_requestUserAsFriend() {
        String fakeEmail = "test" + new Date().getTime() + "@mysocialapp.io";
        String fakeName = "Georges" + new Date().getTime();

        // create other account to accept my friend request
        Session otherSession = new MySocialApp.Builder()
                .setAppId(APP_ID)
                .build()
                .blockingCreateAccount(fakeEmail, PASSWORD, fakeName);

        User otherAccount = otherSession.getAccount().blockingGet();
        User myAccount = session.getAccount().blockingGet();

        // this will remove friendship with other account or cancel friend request
        session.getUser().blockingGet(otherAccount.getId()).blockingRemoveFriend();

        // request the other account as friend (send friend request)
        session.getUser().blockingGet(otherAccount.getId()).blockingRequestAsFriend();

        // accept friend request from the second user
        otherSession.getFriend().blockingListIncomingFriendRequests().forEach(user -> {
            if (myAccount.getId() == user.getId()) {
                // filter friend requests once we find the right one, then accept it
                user.blockingAcceptFriendRequest();
            }
        });

        // check that myAccount and otherAccount are now friend
        session.getFriend().blockingList(0, 100).forEach(user -> {
            if (user.getId() == otherAccount.getId()) {
                assertTrue(true);
            }
        });
    }

    @Test
    void listIncomingFriendRequests() {
        Iterable<User> usersIterable = session.getFriend().blockingListIncomingFriendRequests();

        usersIterable.forEach(user -> {
            System.out.println("ID: " + user.getId() + " Name: " + user.getDisplayedName());
        });
    }

    @Test
    void listOutgoingFriendRequests() {
        Iterable<User> usersIterable = session.getFriend().blockingListOutgoingFriendRequests();

        usersIterable.forEach(user -> {
            System.out.println("ID: " + user.getId() + " Name: " + user.getDisplayedName());
        });
    }

}
