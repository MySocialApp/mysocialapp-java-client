package io.mysocialapp.client.extensions

import okhttp3.MediaType

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
