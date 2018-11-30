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
                               val debug: Boolean = false) {

    class Builder {
        private var mReadTimeoutInMilliseconds: Long = 10_000L
        private var mWriteTimeoutInMilliseconds: Long = 10_000L
        private var mConnectionPool: ConnectionPool = ConnectionPool(5, 5, TimeUnit.MINUTES)
        private var mHeadersToInclude: Map<String, String>? = null
        private var mDebug: Boolean = false

        fun setReadTimeout(millis: Long): Builder {
            this.mReadTimeoutInMilliseconds = millis
            return this
        }

        fun setWriteTimeout(millis: Long): Builder {
            this.mWriteTimeoutInMilliseconds = millis
            return this
        }

        fun setConnectionPool(connectionPool: ConnectionPool): Builder {
            this.mConnectionPool = connectionPool
            return this
        }

        fun setHeaders(headers: Map<String, String>): Builder {
            this.mHeadersToInclude = headers
            return this
        }

        fun setDebug(debug: Boolean): Builder {
            this.mDebug = debug
            return this
        }

        fun build(): ClientConfiguration = ClientConfiguration(
                mReadTimeoutInMilliseconds,
                mWriteTimeoutInMilliseconds,
                mConnectionPool,
                mHeadersToInclude,
                mDebug
        )
    }

}