package io.mysocialapp.client.models

import okhttp3.RequestBody

/**
 * Created by evoxmusic on 01/06/16.
 */
data class MultipartPhoto(val photo: RequestBody,
                          val message: RequestBody? = null,
                          val tagEntities: RequestBody? = null,
                          val accessControl: RequestBody? = null)