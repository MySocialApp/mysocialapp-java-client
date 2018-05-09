package io.mysocialapp.client

import okhttp3.ConnectionPool
import java.util.concurrent.TimeUnit

/**
 * Created by evoxmusic on 27/04/2018.
 */
data class ClientConfiguration(val readTimeoutInMilliseconds: Long = 10_000L,
                               val writeTimeoutInMilliseconds: Long = 10_000L,
                               val connectionPool: ConnectionPool = ConnectionPool(5, 5, TimeUnit.MINUTES),
                               val headersToInclude: Map<String, String>? = null,
                               val debug: Boolean = false)