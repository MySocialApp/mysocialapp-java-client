package io.mysocialapp.client.java;

import io.mysocialapp.client.MySocialApp;
import io.mysocialapp.client.Session;
import io.mysocialapp.client.models.AccessControl;
import io.mysocialapp.client.models.Feed;
import io.mysocialapp.client.models.FeedPost;
import org.junit.jupiter.api.Test;
import rx.Observable;
import rx.observers.TestSubscriber;

import java.io.File;

/**
 * Created by evoxmusic on 17/07/2018.
 */
class NewsFeedTest {

    private final static String APP_ID = "u470584465854a728453";
    private final static String EMAIL = "alice.jeith@mysocialapp.io";
    private final static String PASSWORD = "myverysecretpassw0rd";

    private final static Session publicSession = new MySocialApp.Builder()
            .setAppId(APP_ID)
            .build()
            .getPublicSession();

    private final static Session session = new MySocialApp.Builder()
            .setAppId(APP_ID)
            .build()
            .blockingConnect(EMAIL, PASSWORD);

    @Test
    void publicListNewsFeed() {
        Iterable<Feed> newsFeedIterable = publicSession.getNewsFeed().blockingList(0, 10);

        newsFeedIterable.forEach(feed -> {
            System.out.println(feed.getActor().getDisplayedName() +
                    " do " + feed.getAction().name() +
                    " on" + feed.getBaseObject().getDisplayedName());
        });
    }

    @Test
    void RxPublicListNewsFeed() {
        Observable<Feed> observable = publicSession.getNewsFeed().list(0, 10).cache();

        TestSubscriber<Feed> subscriber = new TestSubscriber<>();
        observable.subscribe(subscriber);
        subscriber.assertCompleted();
        subscriber.assertNoErrors();

        observable.subscribe(feed -> {
            System.out.println(feed.getActor().getDisplayedName() +
                    " do " + feed.getAction().name() +
                    " on" + feed.getBaseObject().getDisplayedName());
        });
    }

    @Test
    void listNewsFeed() {
        Iterable<Feed> newsFeedIterable = session.getNewsFeed().blockingList(0, 10);

        newsFeedIterable.forEach(feed -> {
            System.out.println(feed.getActor().getDisplayedName() +
                    " do " + feed.getAction().name() +
                    " on" + feed.getBaseObject().getDisplayedName());
        });
    }

    @Test
    void RxListNewsFeed() {
        Observable<Feed> observable = session.getNewsFeed().list(0, 10).cache();

        TestSubscriber<Feed> subscriber = new TestSubscriber<>();
        observable.subscribe(subscriber);
        subscriber.assertCompleted();
        subscriber.assertNoErrors();

        observable.subscribe(feed -> {
            System.out.println(feed.getActor().getDisplayedName() +
                    " do " + feed.getAction().name() +
                    " on" + feed.getBaseObject().getDisplayedName());
        });
    }

    @Test
    void createNewsFeed() {
        String message = "Hey I am [[user:" + session.getAccount().blockingGet().getId() + "]] and happy to be here with you :) " +
                "visit my website https://mysocialapp.io #hello";

        final FeedPost feedPost = new FeedPost.Builder()
                .setMessage(message)
                .setImage(new File("hello.jpg"))
                .setVisibility(AccessControl.PUBLIC)
                .build();

        session.getNewsFeed().blockingCreate(feedPost);
    }


}
