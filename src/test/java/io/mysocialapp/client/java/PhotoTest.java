package io.mysocialapp.client.java;

import io.mysocialapp.client.MySocialApp;
import io.mysocialapp.client.Session;
import io.mysocialapp.client.models.AccessControl;
import io.mysocialapp.client.models.Photo;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.Assert.assertNull;

/**
 * Created by evoxmusic on 29/07/2018.
 */
class PhotoTest {

    private final static String APP_ID = "u470584465854a728453";
    private final static String EMAIL = "alice.jeith@mysocialapp.io";
    private final static String PASSWORD = "myverysecretpassw0rd";

    private final static Session session = new MySocialApp.Builder()
            .setAppId(APP_ID)
            .build()
            .blockingConnect(EMAIL, PASSWORD);

    private File getFile(String filePath) {
        return new File(System.class.getResource(filePath).getFile());
    }

    @Test
    void listPhotos() {
        session.getPhoto().blockingStream().forEach(v -> {
            System.out.println(v.getMessage());
        });
    }

    @Test
    void createPhoto() {
        Photo photo = new Photo.Builder()
                .setName("This is a super photo with [[user:" + session.getAccount().blockingGet().getId() + "]]")
                .setImage(getFile("/profile_image.png"))
                .setVisibility(AccessControl.FRIEND)
                .build();

        session.getPhoto().blockingCreate(photo);
    }

    @Test
    void deletePhoto() {
        Photo photo = new Photo.Builder()
                .setName("This is a super photo with [[user:" + session.getAccount().blockingGet().getId() + "]]")
                .setImage(getFile("/profile_image.png"))
                .setVisibility(AccessControl.FRIEND)
                .build();

        Photo createdPhoto = session.getPhoto().blockingCreate(photo);

        createdPhoto.blockingDelete();

        assertNull(session.getPhoto().blockingGet(createdPhoto.getId()));
    }

}
