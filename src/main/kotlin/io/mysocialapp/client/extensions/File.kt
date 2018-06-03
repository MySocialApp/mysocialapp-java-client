package io.mysocialapp.client.extensions

import okhttp3.RequestBody
import java.io.File

/**
 * Created by evoxmusic on 03/06/2018.
 */
fun File.toRequestBody(): RequestBody = RequestBody.create(name.imageMediaType(), this)