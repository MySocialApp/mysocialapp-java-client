package io.mysocialapp.client.java;

import io.mysocialapp.client.FluentUser;
import io.mysocialapp.client.MySocialApp;
import io.mysocialapp.client.Session;
import io.mysocialapp.client.models.SimpleLocation;
import io.mysocialapp.client.models.User;
import io.mysocialapp.client.models.Users;
import io.mysocialapp.client.models.UsersSearchResult;
import org.junit.jupiter.api.Test;
import rx.Observable;
import rx.observers.TestSubscriber;

import java.util.stream.StreamSupport;

import static org.junit.Assert.*;

/**
 * Created by evoxmusic on 17/07/2018.
 */
class UserTest {

    private final static String APP_ID = "u470584465854a728453";
    private final static String EMAIL = "alice.jeith@mysocialapp.io";
    private final static String PASSWORD = "myverysecretpassw0rd";

    private final static Session session = new MySocialApp.Builder()
            .setAppId(APP_ID)
            .build()
            .blockingConnect(EMAIL, PASSWORD);

    @Test
    void listUsers() {
        Iterable<Users> usersIterable = session.getUser().blockingList(0, 10);

        usersIterable.forEach(users -> {
            System.out.println(users.getTotal());

            users.getUsers().forEach(user -> {
                System.out.println("ID: " + user.getId() + " Name: " + user.getDisplayedName());
            });
        });
    }

    @Test
    void RxListUsers() {
        Observable<Users> observable = session.getUser().list(0, 10).cache();

        TestSubscriber<Users> subscriber = new TestSubscriber<>();
        observable.subscribe(subscriber);
        subscriber.assertCompleted();
        subscriber.assertNoErrors();
        assertTrue(subscriber.getValueCount() > 0);

        observable.subscribe(users -> {
            System.out.println(users.getTotal());

            users.getUsers().forEach(user -> {
                System.out.println("ID: " + user.getId() + " Name: " + user.getDisplayedName());
            });
        });
    }

    @Test
    void streamUsers() {
        Iterable<Users> usersIterable = session.getUser().blockingStream(100);

        usersIterable.forEach(users -> {
            System.out.println(users.getTotal());

            users.getUsers().forEach(user -> {
                System.out.println("ID: " + user.getId() + " Name: " + user.getDisplayedName());
            });
        });
    }

    @Test
    void RxStreamUsers() {
        Observable<Users> observable = session.getUser().stream(100).cache();

        TestSubscriber<Users> subscriber = new TestSubscriber<>();
        observable.subscribe(subscriber);
        subscriber.assertCompleted();
        subscriber.assertNoErrors();
        assertTrue(subscriber.getValueCount() > 0);

        observable.subscribe(users -> {
            System.out.println(users.getTotal());

            users.getUsers().forEach(user -> {
                System.out.println("ID: " + user.getId() + " Name: " + user.getDisplayedName());
            });
        });
    }

    @Test
    void findUsersByLocation() {
        SimpleLocation tokyo = new SimpleLocation(35.6894875, 139.69170639999993);

        FluentUser.Search query = new FluentUser.Search.Builder()
                .setLocation(tokyo)
                .setLivingLocationMaximumDistanceInKilometers(70.5)
                .build();

        Iterable<UsersSearchResult> results = session.getUser().blockingSearch(query);

        results.forEach(result -> {
            System.out.println("Total users found:" + result.getMatchedCount());

            result.getData().forEach(user -> {
                System.out.println("ID: " + user.getId() + " Name: " + user.getDisplayedName());
            });
        });
    }

    /**
     * Take the first user from the user list, then search him by it is ID
     */
    @Test
    void findUserById() {
        // There is at least one user, so I know that I can safely get index 0
        User user = StreamSupport.stream(session.getUser().blockingList().spliterator(), false).findFirst().get().getUsers().get(0);
        User foundUserById = session.getUser().blockingGet(user.getId());

        assertEquals(user.getId(), foundUserById.getId());
    }

    @Test
    void findUserByExternalId() {
        // set my own external ID
        User myAccount = session.getAccount().blockingGet();

        // reset external ID
        myAccount.setExternalId(null);
        assertNull(myAccount.blockingSave().getExternalId());

        // set external ID
        String externalId = "my_external_id_12345";
        myAccount.setExternalId(externalId);
        myAccount.blockingSave();
        assertEquals(myAccount.blockingSave().getExternalId(), externalId);

        // find by external ID
        assertEquals(session.getUser().blockingGetByExternalId(externalId).getId(), myAccount.getId());
    }

    @Test
    void findUserByFirstName() {
        FluentUser.Search query = new FluentUser.Search.Builder()
                .setFirstName("Alice")
                .build();

        Iterable<UsersSearchResult> results = session.getUser().blockingSearch(query);

        results.forEach(result -> {
            System.out.println("Total users found:" + result.getMatchedCount());

            result.getData().forEach(user -> {
                System.out.println("ID: " + user.getId() + " Name: " + user.getDisplayedName());
            });
        });
    }

    @Test
    void findUserByLastName() {
        FluentUser.Search query = new FluentUser.Search.Builder()
                .setLastName("Jeith")
                .build();

        Iterable<UsersSearchResult> results = session.getUser().blockingSearch(query);

        results.forEach(result -> {
            System.out.println("Total users found:" + result.getMatchedCount());

            result.getData().forEach(user -> {
                System.out.println("ID: " + user.getId() + " Name: " + user.getDisplayedName());
            });
        });
    }

    @Test
    void findUserByFullName() {
        FluentUser.Search query = new FluentUser.Search.Builder()
                .setFullName("Alice Jeith")
                .build();

        Iterable<UsersSearchResult> results = session.getUser().blockingSearch(query);

        results.forEach(result -> {
            System.out.println("Total users found:" + result.getMatchedCount());

            result.getData().forEach(user -> {
                System.out.println("ID: " + user.getId() + " Name: " + user.getDisplayedName());
            });
        });
    }

    @Test
    void findUserByPresentation() {
        FluentUser.Search query = new FluentUser.Search.Builder()
                .setPresentation("Alice Jeith")
                .build();

        Iterable<UsersSearchResult> results = session.getUser().blockingSearch(query);

        results.forEach(result -> {
            System.out.println("Total users found:" + result.getMatchedCount());

            result.getData().forEach(user -> {
                System.out.println("ID: " + user.getId() + " Name: " + user.getDisplayedName());
            });
        });
    }

}
