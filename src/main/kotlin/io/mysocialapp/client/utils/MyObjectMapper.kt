package io.mysocialapp.client.utils

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.text.SimpleDateFormat

/**
 * Created by evoxmusic on 28/04/2018.
 */
object MyObjectMapper {

    val objectMapper by lazy {
        ObjectMapper().apply {
            registerModule(KotlinModule())
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
            dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
            propertyNamingStrategy = PropertyNamingStrategy.SNAKE_CASE
        }
    }

}