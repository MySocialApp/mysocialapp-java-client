package io.mysocialapp.client.exceptions

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by evoxmusic on 13/06/2018.
 */
open class MySocialAppException(@JsonProperty("message") override val message: String? = null,
                                @JsonProperty("status") open val statusCode: Int? = null,
                                @JsonProperty("error") open val statusErrorMessage: String? = null,
                                @JsonProperty("exception") open val serverExceptionClass: String? = null) : Exception(message) {

    override fun toString(): String {
        return "message: $message - " +
                "status_code: $statusCode - " +
                "status_error_message: $statusErrorMessage - " +
                "server_exception_class: $serverExceptionClass"
    }

}