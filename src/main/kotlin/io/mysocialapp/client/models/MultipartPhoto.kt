package io.mysocialapp.client.models

import okhttp3.RequestBody

/**
 * Created by evoxmusic on 01/06/16.
 */
data class MultipartPhoto(val photo: RequestBody,
                          val message: RequestBody = RequestBody.create(null, ""),
                          val tagEntities: RequestBody = RequestBody.create(null, ""),
                          val accessControl: RequestBody = RequestBody.create(null, ""),
                          val payload: RequestBody = RequestBody.create(null, ""))