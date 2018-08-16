package io.mysocialapp.client.java;

import io.mysocialapp.client.FluentNewsFeed;
import io.mysocialapp.client.MySocialApp;
import io.mysocialapp.client.Session;
import io.mysocialapp.client.models.*;
import org.junit.jupiter.api.Test;
import rx.Observable;
import rx.observers.TestSubscriber;

import java.io.File;
import java.util.List;
import java.util.Objects;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;

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
                    " on" + feed.getObject().getDisplayedName());
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
                    " on" + feed.getObject().getDisplayedName());
        });
    }

    @Test
    void listNewsFeed() {
        Iterable<Feed> newsFeedIterable = session.getNewsFeed().blockingList(0, 10);

        newsFeedIterable.forEach(feed -> {
            System.out.println(feed.getActor().getDisplayedName() +
                    " do " + feed.getAction().name() +
                    " on" + feed.getObject().getDisplayedName());
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
                    " on" + feed.getObject().getDisplayedName());
        });
    }

    @Test
    void createNewsFeed() {
        String message = "Hey I am [[user:" + session.getAccount().blockingGet().getId() + "]] and happy to be here with you :) " +
                "visit my website https://mysocialapp.io #hello";

        final FeedPost feedPost = new FeedPost.Builder()
                .setMessage(message)
                .setVisibility(AccessControl.PUBLIC)
                .build();

        session.getNewsFeed().blockingCreate(feedPost);
    }

    @Test
    void createNewsFeed_withImage() {
        String message = "Hey I am [[user:" + session.getAccount().blockingGet().getId() + "]] and happy to be here with you :) " +
                "visit my website https://mysocialapp.io #hello";

        final FeedPost feedPost = new FeedPost.Builder()
                .setMessage(message)
                .setImage(new File("hello.jpg"))
                .setVisibility(AccessControl.PUBLIC)
                .build();

        session.getNewsFeed().blockingCreate(feedPost);
    }

    @Test
    void editNewsFeed() {
        String message = "Hey I am [[user:" + session.getAccount().blockingGet().getId() + "]] and happy to be here with you :) " +
                "visit my website https://mysocialapp.io #hello";

        final FeedPost feedPost = new FeedPost.Builder()
                .setMessage(message)
                .setVisibility(AccessControl.PUBLIC)
                .build();

        Feed feed = session.getNewsFeed().blockingCreate(feedPost);

        feed.setBodyMessage(feed.getBodyMessage() + " (edited)");

        Feed editedFeed = (Feed) feed.blockingSave();
        assertTrue(editedFeed.getBodyMessage().endsWith("(edited)"));
    }

    @Test
    void listLikes() {
        Feed feed = session.getNewsFeed().blockingStream(1).iterator().next();

        for (Like like : feed.blockingListLikes()) {
            System.out.println(like.getOwner().getDisplayedName() + " has liked " + feed.getObject().getDisplayedName());
        }
    }

    @Test
    void findById() {
        Feed feed = session.getNewsFeed().blockingStream(1).iterator().next();
        assertNotNull(session.getNewsFeed().blockingGet(feed.getObject().getId()));
    }

    @Test
    void doLike() {
        Iterable<Feed> newsFeedIterable = session.getNewsFeed().blockingStream(1);
        newsFeedIterable.forEach(Feed::blockingAddLike);
    }

    @Test
    void removeLike() {
        Iterable<Feed> newsFeedIterable = session.getNewsFeed().blockingStream(1);
        newsFeedIterable.forEach(Feed::blockingRemoveLike);
    }

    @Test
    void listComments() {
        Feed feed = session.getNewsFeed().blockingStream(1).iterator().next();

        for (Comment comment : feed.blockingListComments()) {
            System.out.println(comment.getOwner().getDisplayedName() +
                    " has commented '" + comment.getMessage() +
                    "' from " + feed.getObject().getDisplayedName());
        }
    }

    @Test
    void doComment() {
        Iterable<Feed> newsFeedIterable = session.getNewsFeed().blockingStream(1);
        newsFeedIterable.forEach(f -> {
            CommentPost commentPost = new CommentPost.Builder()
                    .setMessage("Hey this is incredible [[user:" + f.getActor().getId() + "]]")
                    .build();

            f.blockingAddComment(commentPost);
        });
    }

    @Test
    void doComment_withImage() {
        Iterable<Feed> newsFeedIterable = session.getNewsFeed().blockingStream(1);
        newsFeedIterable.forEach(f -> {
            CommentPost commentPost = new CommentPost.Builder()
                    .setMessage("Hey this is incredible [[user:" + f.getActor().getId() + "]]")
                    .setImage(new File(System.class.getResource("/hello.jpg").getFile()))
                    .build();

            f.blockingAddComment(commentPost);
        });
    }

    @Test
    void editComment() {
        // add comment before
        doComment();

        Iterable<Feed> newsFeedIterable = session.getNewsFeed().blockingStream(1);
        User myProfile = session.getAccount().blockingGet();

        newsFeedIterable.forEach(feed -> {
            feed.blockingListComments().forEach(comment -> {
                if (Objects.equals(myProfile.getId(), comment.getOwner().getId())) {
                    // then edit comment
                    comment.setMessage(comment.getMessage() + " (edited)");
                    Comment editedComment = (Comment) comment.blockingSave();

                    System.out.println(editedComment.getMessage());
                }
            });
        });
    }

    @Test
    void deleteComment() {
        // add comment before
        doComment();

        Iterable<Feed> newsFeedIterable = session.getNewsFeed().blockingStream(1);
        User myProfile = session.getAccount().blockingGet();

        newsFeedIterable.forEach(feed -> {
            feed.blockingListComments().forEach(comment -> {
                if (Objects.equals(myProfile.getId(), comment.getOwner().getId())) {
                    // then edit comment
                    comment.blockingDelete();
                }
            });
        });
    }

    @Test
    void findByText() {
        FluentNewsFeed.Search query = new FluentNewsFeed.Search.Builder()
                .setTextToSearch("happy to be here")
                .build();

        Iterable<FeedsSearchResult> results = session.getNewsFeed().blockingSearch(query, 0, 10);
        assertNotNull(results.iterator().next().getMatchedCount());
    }

    @Test
    void feedIgnore() {
        Feed feed = session.getNewsFeed().blockingStream(1).iterator().next();
        feed.blockingIgnore();
    }

    @Test
    void feedAbuse() {
        Feed feed = session.getNewsFeed().blockingStream(1).iterator().next();
        feed.blockingAbuse();
    }

    @Test
    void listHashTagsFromFeed() {
        String message = "This is a #hashtag message";

        FeedPost feedPost = new FeedPost.Builder()
                .setMessage(message)
                .setVisibility(AccessControl.PUBLIC)
                .build();

        Feed createdFeed = session.getNewsFeed().blockingCreate(feedPost);
        List<HashTag> hashTags = createdFeed.getBodyMessageTagEntities().getHashTags();

        for (HashTag hashTag : hashTags) {
            System.out.println(hashTag.getText());
        }
    }

    @Test
    void listUserMentionsFromFeed() {
        String message = "Hey [[user:" + session.getAccount().blockingGet().getId() + "]] what's up today?";

        FeedPost feedPost = new FeedPost.Builder()
                .setMessage(message)
                .setVisibility(AccessControl.PUBLIC)
                .build();

        Feed createdFeed = session.getNewsFeed().blockingCreate(feedPost);
        List<UserMentionTag> userMentionTags = createdFeed.getBodyMessageTagEntities().getUserMentionTags();

        for (UserMentionTag userMentionTag : userMentionTags) {
            System.out.println(userMentionTag.getMentionedUser().getDisplayedName() + "has been mentioned");
        }
    }

    @Test
    void listURLTagsFromFeed() {
        String message = "This is a web link shared with all https://blog.algolia.com/supporting-open-source-projects";

        FeedPost feedPost = new FeedPost.Builder()
                .setMessage(message)
                .setVisibility(AccessControl.PUBLIC)
                .build();

        Feed createdFeed = session.getNewsFeed().blockingCreate(feedPost);
        List<URLTag> urlTags = createdFeed.getBodyMessageTagEntities().getUrlTags();

        for (URLTag urlTag : urlTags) {
            System.out.println(urlTag.getTitle());
        }
    }


    @Test
    void deleteFeed() {
        Feed feed = session.getNewsFeed().blockingStream(1).iterator().next();
        feed.blockingDelete();
    }

    @Test
    void getDynamicNewsFeed() {
        assertNotNull(session.getNewsFeed().getDynamic().blockingList(123));
    }

    @Test
    void createDynamicNewsFeed() {
        String message = "Hey I am [[user:" + session.getAccount().blockingGet().getId() + "]] and happy to be here with you :) " +
                "visit my website https://mysocialapp.io #hello";

        final FeedPost feedPost = new FeedPost.Builder()
                .setMessage(message)
                .setImage(new File("hello.jpg"))
                .setVisibility(AccessControl.PUBLIC)
                .build();

        session.getNewsFeed().getDynamic().blockingCreate(123, feedPost);
    }

}
