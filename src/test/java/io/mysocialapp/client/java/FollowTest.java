package io.mysocialapp.client.java;

import io.mysocialapp.client.MySocialApp;
import io.mysocialapp.client.Session;
import io.mysocialapp.client.models.User;
import org.junit.jupiter.api.Test;

import static junit.framework.TestCase.assertTrue;


/**
 * Created by evoxmusic on 17/07/2018.
 */
class FollowTest {

    private final static String APP_ID = "u470584465854a728453";
    private final static String EMAIL = "alice.jeith@mysocialapp.io";
    private final static String PASSWORD = "myverysecretpassw0rd";

    private final static Session session = new MySocialApp.Builder()
            .setAppId(APP_ID)
            .build()
            .blockingConnect(EMAIL, PASSWORD);

    @Test
    void listFollowing() {
        Iterable<User> usersIterable = session.getFollow().blockingListFollowing(0, 50);

        usersIterable.forEach(user -> {
            System.out.println("ID: " + user.getId() + " Name: " + user.getDisplayedName());
            final User u = session.getUser().blockingGet(user.getId());
            assertTrue(u.isFollowing());
        });
    }

    @Test
    void listFollower() {
        Iterable<User> usersIterable = session.getFollow().blockingListFollower(0, 10);

        usersIterable.forEach(user -> {
            System.out.println("ID: " + user.getId() + " Name: " + user.getDisplayedName());
            final User u = session.getUser().blockingGet(user.getId());
            assertTrue(u.isFollower());
        });
    }

    @Test
    void do_follow_and_unfollow() {
        Iterable<User> usersIterable = session.getUser().blockingList(0, 10).iterator().next().getUsers();

        usersIterable.forEach(user -> {
            user.blockingFollow();
            assertTrue(session.getUser().blockingGet(user.getId()).isFollowing());
            user.blockingUnfollow();
            assertTrue(session.getUser().blockingGet(user.getId()).isFollowing() == false);
        });
    }

    @Test
    void do_follow() {
        Iterable<User> usersIterable = session.getUser().blockingList(0, 20).iterator().next().getUsers();

        usersIterable.forEach(user -> {
            user.blockingFollow();
            assertTrue(session.getUser().blockingGet(user.getId()).isFollowing());
        });
    }

}
