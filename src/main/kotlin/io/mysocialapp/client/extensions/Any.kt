package io.mysocialapp.client.extensions

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import java.util.*

/**
 * Created by evoxmusic on 11/05/2018.
 */
fun Any.toJSONString(mapper: ObjectMapper = ObjectMapper()): String = mapper.writeValueAsString(this)

fun Any.toMap(mapper: ObjectMapper = ObjectMapper()): HashMap<String, Any?> = mapper.readValue(toJSONString(mapper), object : TypeReference<HashMap<String, Any?>>() {})
