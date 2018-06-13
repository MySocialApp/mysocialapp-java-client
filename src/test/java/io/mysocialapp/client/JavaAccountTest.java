package io.mysocialapp.client;

import io.mysocialapp.client.exceptions.MySocialAppException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * Created by evoxmusic on 10/05/2018.
 */
class JavaAccountTest {

    private final static String APP_ID = "u470584465854a194805";
    private static Session session;

    static {
        try {
            session = new MySocialApp.Builder().setAppId(APP_ID).build().blockingConnect("AliceX", "myverysecretpassw0rd");
        } catch (MySocialAppException | IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void createAccount() {
        assert (session.getAccount().blockingGet().getFirstName() != null);
    }

}
