package io.mysocialapp.client.java;

import io.mysocialapp.client.MySocialApp;
import io.mysocialapp.client.Session;
import io.mysocialapp.client.models.Gender;
import io.mysocialapp.client.models.Location;
import io.mysocialapp.client.models.SimpleLocation;
import io.mysocialapp.client.models.User;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by evoxmusic on 17/07/2018.
 */
class AccountTest {

    private final static String APP_ID = "u470584465854a728453";
    private final static String EMAIL = "alice.jeith@mysocialapp.io";
    private final static String PASSWORD = "myverysecretpassw0rd";

    private final static Session session = new MySocialApp.Builder()
            .setAppId(APP_ID)
            .build()
            .blockingConnect(EMAIL, PASSWORD);

    @Test
    void signUp() {
        String fakeEmail = "test" + new Date().getTime() + "@mysocialapp.io";
        String fakeName = "Georges" + new Date().getTime();

        new MySocialApp.Builder()
                .setAppId(APP_ID)
                .build()
                .blockingCreateAccount(fakeEmail, PASSWORD, fakeName);
    }

    @Test
    void signIn() {
        new MySocialApp.Builder()
                .setAppId(APP_ID)
                .build()
                .blockingConnect(EMAIL, PASSWORD);
    }

    @Test
    void getProfile() {
        session.getAccount().blockingGet();
    }

    @Test
    void updateProfile() {
        User profile = session.getAccount().blockingGet();

        Calendar cal = Calendar.getInstance();
        cal.set(2018, Calendar.JANUARY, 1);

        // reset profile
        profile.setLastName(null);
        profile.setDateOfBirth(null);
        profile.setExternalId(null);
        profile.setPresentation(null);
        profile.setGender(null);
        profile.setLivingLocation(null);

        User profile1 = profile.blockingSave();

        assertNull(profile1.getLastName());
        assertNull(profile1.getDateOfBirth());
        assertNull(profile1.getExternalId());
        assertNull(profile1.getPresentation());
        assertNull(profile1.getGender());
        assertNull(profile1.getLivingLocation());

        // set profile
        SimpleLocation tokyo = new SimpleLocation(35.6894875, 139.69170639999993);

        profile.setLastName("Jeith" + new Date().getTime());
        profile.setDateOfBirth(new Date());
        profile.setExternalId("jeith");
        profile.setPresentation("Hello I'm am Alice Jeith and this is a test from MySocialApp SDK :) See you soon");
        profile.setGender(Gender.FEMALE);
        profile.setLivingLocation(new Location(tokyo));

        User profile2 = profile.blockingSave();

        assertEquals(profile2.getLastName(), profile.getLastName());
        assertEquals(profile2.getDateOfBirth(), profile.getDateOfBirth());
        assertEquals(profile2.getExternalId(), profile.getExternalId());
        assertEquals(profile2.getGender(), profile.getGender());
        assertEquals(profile2.getLivingLocation().getLatitude(), profile.getLivingLocation().getLatitude());
        assertEquals(profile2.getLivingLocation().getLongitude(), profile.getLivingLocation().getLongitude());
    }

    @Test
    void updateProfilePhoto() {
        File profileImage = new File(System.class.getResource("/profile_image.png").getFile());
        session.getAccount().changeProfilePhoto(profileImage);

        assertNotNull(session.getAccount().blockingGet().getProfilePhoto());
    }

    @Test
    void updateProfileCoverPhoto() {
        File profileImage = new File(System.class.getResource("/cover_image.jpg").getFile());
        session.getAccount().changeProfileCoverPhoto(profileImage);

        assertNotNull(session.getAccount().blockingGet().getProfileCoverPhoto());
    }

    @Test
    void getAvailableCustomFields() {
        assertNotNull(session.getAccount().blockingGetAvailableCustomFields().iterator());
    }

    @Test
    void deleteAccount() {
        String fakeEmail = "test" + new Date().getTime() + "@mysocialapp.io";
        String fakeName = "Georges" + new Date().getTime();

        Session session = new MySocialApp.Builder()
                .setAppId(APP_ID)
                .build()
                .blockingCreateAccount(fakeEmail, PASSWORD, fakeName);

        session.getAccount().blockingRequestForDeleteAccount(PASSWORD);
    }

}
