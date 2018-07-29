package io.mysocialapp.client.java;

import io.mysocialapp.client.MySocialApp;
import io.mysocialapp.client.Session;
import io.mysocialapp.client.models.Base;
import io.mysocialapp.client.models.PreviewNotification;
import org.junit.jupiter.api.Test;

/**
 * Created by evoxmusic on 10/05/2018.
 */
class NotificationTest {

    private final static String APP_ID = "u470584465854a728453";
    private final static String EMAIL = "alice.jeith@mysocialapp.io";
    private final static String PASSWORD = "myverysecretpassw0rd";

    private final static Session session = new MySocialApp.Builder()
            .setAppId(APP_ID)
            .build()
            .blockingConnect(EMAIL, PASSWORD);

    @Test
    void listUnreadNotifications() {
        Iterable<PreviewNotification> notifications = session.getNotification().getUnread().blockingList(0, 10);
        notifications.forEach(v -> {
            Base payload = v.getLastNotification().getPayload();
            System.out.println(payload.getDisplayedName());
        });
    }

    @Test
    void listUnreadNotifications_and_consume() {
        Iterable<PreviewNotification> notifications = session.getNotification().getUnread().blockingList(0, 10);
        notifications.forEach(v -> {
            PreviewNotification consumedPreviewNotification = v.blockingConsume();
            Base payload = consumedPreviewNotification.getLastNotification().getPayload();
            System.out.println(payload.getDisplayedName());
        });
    }

    @Test
    void consumeUnreadNotifications() {
        Iterable<PreviewNotification> notifications = session.getNotification().getUnread().blockingListAndConsume();
        notifications.forEach(v -> {
            Base payload = v.getLastNotification().getPayload();
            System.out.println(payload.getDisplayedName());
        });
    }

    @Test
    void listReadNotifications() {
        Iterable<PreviewNotification> notifications = session.getNotification().getRead().blockingList(0, 10);
        notifications.forEach(v -> {
            Base payload = v.getLastNotification().getPayload();
            System.out.println(payload.getDisplayedName());
        });
    }

}
