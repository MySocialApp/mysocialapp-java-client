package io.mysocialapp.client.java;

import io.mysocialapp.client.FluentEvent;
import io.mysocialapp.client.MySocialApp;
import io.mysocialapp.client.Session;
import io.mysocialapp.client.models.*;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Created by evoxmusic on 31/07/2018.
 */
public class EventTest {

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
    void listEvents() {
        BaseLocation parisLocation = new SimpleLocation(48.85661400000001, 2.3522219000000177);

        FluentEvent.Options options = new FluentEvent.Options.Builder()
                .setLocation(parisLocation)
                .setFromDate(new Date())
                .setLimited(true)
                .build();

        Iterable<Event> events = session.getEvent().blockingList(0, 10, options);
    }

    @Test
    void createEvent() {
        SimpleLocation newarkLocation = new SimpleLocation(40.736504474883915, -74.18175405);

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DATE, 1);
        Date tomorrow = cal.getTime();

        cal.setTime(new Date());
        cal.add(Calendar.DATE, 2);
        Date afterTomorrow = cal.getTime();

        Event event = new Event.Builder()
                .setName("New test event")
                .setDescription("This is a new event create with our SDK")
                .setStartDate(tomorrow)
                .setEndDate(afterTomorrow)
                .setLocation(newarkLocation)
                .setMaxSeats(100)
                .setMemberAccessControl(EventMemberAccessControl.PUBLIC)
                .setImage(getFile("/hello.jpg"))
                .setCoverImage(getFile("/cover_image.jpg"))
                .build();

        assertNotNull(session.getEvent().blockingCreate(event));
    }

    @Test
    void createEvent_with_CustomFields() {
        SimpleLocation newarkLocation = new SimpleLocation(40.736504474883915, -74.18175405);

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DATE, 1);
        Date tomorrow = cal.getTime();

        cal.setTime(new Date());
        cal.add(Calendar.DATE, 2);
        Date afterTomorrow = cal.getTime();

        // Get available custom fields for Event
        Iterable<CustomField> availableCustomFields = session.getEvent().blockingGetAvailableCustomFields();

        // Convert Iterable to List of Custom Field
        List<CustomField> customFields = StreamSupport.stream(availableCustomFields.spliterator(), false)
                .collect(Collectors.toList());

        // Simulate adding value to Custom Field by their type
        for (CustomField customField : customFields) {
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

        Event event = new Event.Builder()
                .setName("New test event")
                .setDescription("This is a new event create with our SDK")
                .setStartDate(tomorrow)
                .setEndDate(afterTomorrow)
                .setLocation(newarkLocation)
                .setMaxSeats(100)
                .setMemberAccessControl(EventMemberAccessControl.PUBLIC)
                .setImage(getFile("/hello.jpg"))
                .setCoverImage(getFile("/cover_image.jpg"))
                .setCustomFields(customFields) // set custom fields
                .build();

        // Create Event with Custom Fields
        assertNotNull(session.getEvent().blockingCreate(event));
    }

    @Test
    void cancelEvent() {
        SimpleLocation newarkLocation = new SimpleLocation(40.736504474883915, -74.18175405);

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DATE, 1);
        Date tomorrow = cal.getTime();

        cal.setTime(new Date());
        cal.add(Calendar.DATE, 2);
        Date afterTomorrow = cal.getTime();

        Event event = new Event.Builder()
                .setName("New test event")
                .setDescription("This is a new event create with our SDK")
                .setStartDate(tomorrow)
                .setEndDate(afterTomorrow)
                .setLocation(newarkLocation)
                .setMaxSeats(100)
                .setMemberAccessControl(EventMemberAccessControl.PUBLIC)
                .setImage(getFile("/hello.jpg"))
                .setCoverImage(getFile("/cover_image.jpg"))
                .build();

        Event createdEvent = session.getEvent().blockingCreate(event);

        createdEvent.blockingCancel();

        assertTrue(session.getEvent().blockingGet(createdEvent.getId()).isCancelled());
    }

    @Test
    void listEventMembers() {
        Event event = session.getEvent().blockingStream(1).iterator().next();
        List<EventMember> eventMembers = event.getMembers();
        assertNotNull(eventMembers);
    }

    @Test
    void joinEvent() {
        SimpleLocation newarkLocation = new SimpleLocation(40.736504474883915, -74.18175405);

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DATE, 1);
        Date tomorrow = cal.getTime();

        cal.setTime(new Date());
        cal.add(Calendar.DATE, 2);
        Date afterTomorrow = cal.getTime();

        Event event = new Event.Builder()
                .setName("New test event")
                .setDescription("This is a new event create with our SDK")
                .setStartDate(tomorrow)
                .setEndDate(afterTomorrow)
                .setLocation(newarkLocation)
                .setMaxSeats(39)
                .setMemberAccessControl(EventMemberAccessControl.PUBLIC)
                .build();

        Event createdEvent = session.getEvent().blockingCreate(event);

        createdEvent.blockingParticipate();
        assertTrue(session.getEvent().blockingGet(createdEvent.getId()).isMember());
    }

}
