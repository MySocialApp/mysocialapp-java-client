package io.mysocialapp.client

/**
 * Created by evoxmusic on 27/04/2018.
 */
data class Configuration(private val appId: String,
                         private val apiEndpointURL: String? = null) {

    val completeAPIEndpointURL = apiEndpointURL?.let { "$it/api/v1/" } ?: "https://$appId-api.mysocialapp.io/api/v1/"
    val completeWebSocketEndpointURL = apiEndpointURL?.let { "$it/ws" } ?: "wss://$appId-ws.mysocialapp.io/ws"

}