package io.mysocialapp.client.exceptions

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by evoxmusic on 13/06/2018.
 */
class InvalidCredentialsMySocialAppException(@JsonProperty("message") override val message: String? = null,
                                             @JsonProperty("status") override val statusCode: Int? = null,
                                             @JsonProperty("error") override val statusErrorMessage: String? = null,
                                             @JsonProperty("exception") override val serverExceptionClass: String? = null
) : MySocialAppException(message, statusCode, statusErrorMessage, serverExceptionClass)