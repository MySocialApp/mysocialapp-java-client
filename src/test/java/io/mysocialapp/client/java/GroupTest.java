package io.mysocialapp.client.java;

import io.mysocialapp.client.FluentGroup;
import io.mysocialapp.client.MySocialApp;
import io.mysocialapp.client.Session;
import io.mysocialapp.client.models.*;
import org.junit.jupiter.api.Test;

import java.io.File;
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
public class GroupTest {

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
    void listGroups() {
        BaseLocation parisLocation = new SimpleLocation(48.85661400000001, 2.3522219000000177);

        FluentGroup.Options options = new FluentGroup.Options.Builder()
                .setLocation(parisLocation)
                .setLimited(true)
                .build();

        Iterable<Group> groups = session.getGroup().blockingList(0, 10, options);
    }

    @Test
    void createGroup() {
        SimpleLocation newarkLocation = new SimpleLocation(40.736504474883915, -74.18175405);

        Group group = new Group.Builder()
                .setName("New test group")
                .setDescription("This is a new group create with our SDK")
                .setLocation(newarkLocation)
                .setMemberAccessControl(GroupMemberAccessControl.PUBLIC)
                .setImage(getFile("/hello.jpg"))
                .setCoverImage(getFile("/cover_image.jpg"))
                .build();

        assertNotNull(session.getGroup().blockingCreate(group));
    }

    @Test
    void createGroup_with_CustomFields() {
        SimpleLocation newarkLocation = new SimpleLocation(40.736504474883915, -74.18175405);

        // Get available custom fields for Event
        Iterable<CustomField> availableCustomFields = session.getGroup().blockingGetAvailableCustomFields();

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

        Group group = new Group.Builder()
                .setName("New test group")
                .setDescription("This is a new group create with our SDK")
                .setLocation(newarkLocation)
                .setMemberAccessControl(GroupMemberAccessControl.PUBLIC)
                .setImage(getFile("/hello.jpg"))
                .setCoverImage(getFile("/cover_image.jpg"))
                .setCustomFields(customFields)
                .build();

        assertNotNull(session.getGroup().blockingCreate(group));
    }

    @Test
    void listGroupMembers() {
        Group group = session.getGroup().blockingStream(1).iterator().next();
        List<GroupMember> groupMembers = group.getMembers();
        assertNotNull(groupMembers);
    }

    @Test
    void joinGroup() {
        SimpleLocation newarkLocation = new SimpleLocation(40.736504474883915, -74.18175405);

        Group group = new Group.Builder()
                .setName("New test group")
                .setDescription("This is a new group create with our SDK")
                .setLocation(newarkLocation)
                .setMemberAccessControl(GroupMemberAccessControl.PUBLIC)
                .setImage(getFile("/hello.jpg"))
                .setCoverImage(getFile("/cover_image.jpg"))
                .build();

        Group createdGroup = session.getGroup().blockingCreate(group);
        createdGroup.blockingJoin();

        assertTrue(session.getGroup().blockingGet(createdGroup.getId()).isMember());
    }

}
