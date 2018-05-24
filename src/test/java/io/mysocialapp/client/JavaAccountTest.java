package io.mysocialapp.client;

import org.junit.jupiter.api.Test;

/**
 * Created by evoxmusic on 10/05/2018.
 */
class JavaAccountTest {

    private final static String APP_ID = "u470584465854a194805";
    private final static Session session = new MySocialApp.Builder().setAppId(APP_ID).build().blockingConnect("AliceX", "myverysecretpassw0rd");

    @Test
    void createAccount() {
        assert (session.getAccount().blockingGet().getFirstName() != null);
    }

}
