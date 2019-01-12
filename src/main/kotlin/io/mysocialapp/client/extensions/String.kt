package io.mysocialapp.client.extensions

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.MediaType
import okhttp3.RequestBody

/**
 * Created by evoxmusic on 09/05/2018.
 */
fun String.imageMediaType(): MediaType? {
    val imageType = this.split(".").getOrNull(this.length - 1)?.toLowerCase() ?: "jpeg"
    if (imageType == "jpg") {
        return MediaType.parse("image/jpeg")
    }

    return MediaType.parse("image/$imageType")
}

fun String?.toRequestBody(): RequestBody = if (this.isNullOrBlank()) {
    RequestBody.create(null, "")
} else {
    RequestBody.create(MediaType.parse("multipart/form-data"), this)
}

fun String.fromJSONStringToMap(objectMapper: ObjectMapper = ObjectMapper()): Map<String, Any?> = if (this.isBlank()) {
    emptyMap()
} else {
    objectMapper.readValue(this, object : TypeReference<Map<String, Any?>>() {})
}
