package io.mysocialapp.client.java;

import io.mysocialapp.client.MySocialApp;
import io.mysocialapp.client.Session;
import io.mysocialapp.client.models.Photo;
import io.mysocialapp.client.models.PhotoAlbum;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Arrays;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by evoxmusic on 10/05/2018.
 */
class PhotoAlbumTest {

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
    void listPhotoAlbums() {
        session.getPhotoAlbum().blockingStream().forEach(v -> {
            System.out.println(v.getName() + " total photos: " + v.getPreviewPhotos().getTotal());
        });
    }

    @Test
    void createPhotoAlbum() {
        PhotoAlbum photoAlbum = new PhotoAlbum.Builder()
                .setName("Photo Album " + new Date().toString())
                .build();

        session.getPhotoAlbum().blockingCreate(photoAlbum);
    }

    @Test
    void createPhotoAlbum_and_addPhotos() {
        // image + text
        Photo aPhoto = new Photo.Builder().setName("cover image").setImage(getFile("/cover_image.jpg")).build();

        // image only
        Photo bPhoto = new Photo.Builder().setImage(getFile("/hello.jpg")).build();

        // image + text with user mention
        Photo cPhoto = new Photo.Builder()
                .setName("Hey this is [[user:" + session.getAccount().blockingGet().getId() + "]] :)")
                .setImage(getFile("/profile_image.png"))
                .build();

        // create photo album with the 3 Photos
        PhotoAlbum photoAlbum = new PhotoAlbum.Builder()
                .setName("Photo Album with Pix " + new Date().toString())
                .setPhotos(Arrays.asList(aPhoto, bPhoto, cPhoto))
                .build();

        PhotoAlbum finalPhotoAlbum = session.getPhotoAlbum().blockingCreate(photoAlbum);
        assertTrue(finalPhotoAlbum.getPreviewPhotos().getTotal() == 3);
    }

    @Test
    void deletePhotoAlbum() {
        PhotoAlbum photoAlbum = new PhotoAlbum.Builder()
                .setName("Photo Album " + new Date().toString())
                .build();

        PhotoAlbum createdPhotoAlbum = session.getPhotoAlbum().blockingCreate(photoAlbum);

        createdPhotoAlbum.blockingDelete();

        assertNull(session.getPhotoAlbum().blockingGet(createdPhotoAlbum.getId()));
    }

    @Test
    void changeNamePhotoAlbum() {
        String now = new Date().toString();

        PhotoAlbum photoAlbum = new PhotoAlbum.Builder()
                .setName("Photo Album " + now)
                .build();

        PhotoAlbum createdPhotoAlbum = session.getPhotoAlbum().blockingCreate(photoAlbum);

        String albumName = "Edited Photo Album " + now;
        createdPhotoAlbum.setName(albumName);
        createdPhotoAlbum.blockingSave();

        assertEquals(session.getPhotoAlbum().blockingGet(createdPhotoAlbum.getId()).getName(), albumName);
    }

}
