package io.mysocialapp.client.java;

import io.mysocialapp.client.MySocialApp;
import io.mysocialapp.client.Session;
import io.mysocialapp.client.exceptions.InvalidCredentialsMySocialAppException;
import io.mysocialapp.client.models.*;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by evoxmusic on 17/07/2018.
 */
class AccountTest {

    private final static String APP_ID = "u470584465854a728453";
    private final static String EMAIL = "alice.jeith@mysocialapp.io";
    private final static String PASSWORD = "myverysecretpassw0rd";

    private final static String JOHN_EMAIL = "john.paul@mysocialapp.io";

    private Session getSession() {
        return new MySocialApp.Builder()
                .setAppId(APP_ID)
                .build()
                .blockingConnect(EMAIL, PASSWORD);
    }

    @Test
    void signUpAlice() {
        Session s = new MySocialApp.Builder()
                .setAppId(APP_ID)
                .build()
                .blockingCreateAccount(EMAIL, PASSWORD, "Alice");

        Account account = s.getAccount().blockingGet();
        account.setLastName("Jeith");
        account.blockingSave();
    }

    @Test
    void signUpJohn() {
        Session s = new MySocialApp.Builder()
                .setAppId(APP_ID)
                .build()
                .blockingCreateAccount(JOHN_EMAIL, PASSWORD, "John");

        Account account = s.getAccount().blockingGet();
        account.setLastName("Paul");
        account.blockingSave();
    }

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
    void badSignIn() throws InterruptedException {
        new MySocialApp.Builder()
                .setAppId(APP_ID)
                .build()
                .connect("badAccount@mail.com", "badPassword" + new Date().getTime())
                .subscribe(v -> {
                    v.getAccount().blockingGet();
                }, err -> {
                    if (err instanceof InvalidCredentialsMySocialAppException) {
                        assertTrue(true);
                        return;
                    }

                    fail();
                });

        Thread.sleep(3000);
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
        getSession().getAccount().blockingGet();
    }

    @Test
    void updateProfile() {
        Account profile = getSession().getAccount().blockingGet();

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

        profile.setFirstName("Alice");
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
        getSession().getAccount().changeProfilePhoto(profileImage);

        assertNotNull(getSession().getAccount().blockingGet().getProfilePhoto());
    }

    @Test
    void updateProfileCoverPhoto() {
        File profileImage = new File(System.class.getResource("/cover_image.jpg").getFile());
        getSession().getAccount().changeProfileCoverPhoto(profileImage);

        assertNotNull(getSession().getAccount().blockingGet().getProfileCoverPhoto());
    }

    @Test
    void getAvailableCustomFields() {
        assertNotNull(getSession().getAccount().blockingGetAvailableCustomFields().iterator());
    }

    @Test
    void getAvailableCustomFields_2() {
        assertNotNull(getSession().getAccount().blockingGet().getCustomFields());
    }

    @Test
    void updateCustomFields() {
        User profile = getSession().getAccount().blockingGet();

        for (CustomField customField : profile.getCustomFields()) {
            CustomField.FieldType customFieldType = customField.getFieldType();

            if (customFieldType == CustomField.FieldType.INPUT_TEXT) {
                customField.setValue("this is a text");

            } else if (customFieldType == CustomField.FieldType.INPUT_TEXTAREA) {
                customField.setValue("this is a text for text area");

            } else if (customFieldType == CustomField.FieldType.INPUT_BOOLEAN) {
                customField.setValue(true);

            } else if (customFieldType == CustomField.FieldType.INPUT_NUMBER) {
                customField.setValue(1234.2329);

            } else if (customFieldType == CustomField.FieldType.INPUT_DATE) {
                customField.setValue(new Date());

            } else if (customFieldType == CustomField.FieldType.INPUT_EMAIL) {
                customField.setValue("name@domain.tld");

            } else if (customFieldType == CustomField.FieldType.INPUT_PHONE) {
                customField.setValue("+33123456789");

            } else if (customFieldType == CustomField.FieldType.INPUT_LOCATION) {
                SimpleLocation tokyo = new SimpleLocation(35.6894875, 139.69170639999993);
                customField.setLocationValue(tokyo);

            } else if (customFieldType == CustomField.FieldType.INPUT_URL) {
                customField.setValue("https://mysocialapp.io");

            } else if (customFieldType == CustomField.FieldType.INPUT_SELECT) {
                // checkbox accept string from possible values
                String possibleValue = customField.getPossibleValues().get(0);
                customField.setValue(possibleValue);

            } else if (customFieldType == CustomField.FieldType.INPUT_CHECKBOX) {
                // checkbox accept list of string from possible values
                String possibleValue = customField.getPossibleValues().get(0);
                customField.setValue(Collections.singletonList(possibleValue));

            }
        }

        profile.blockingSave();
    }

    @Test
    void resetPassword() {
        new MySocialApp.Builder()
                .setAppId(APP_ID)
                .build()
                .blockingResetPasswordByEmail(EMAIL);
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

    @Test
    void switchAccount() {
        final Session session = getSession();
        User user = session.getUser().blockingList(2).iterator().next().getUsers().get(0);
        assertNotEquals(user.getId(), session.getAccount().blockingGet().getId());
        assertEquals(user.blockingConnectAsUser().getAccount().blockingGet().getId(), user.getId());
    }

}
