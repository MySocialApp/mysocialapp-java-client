package io.mysocialapp.client.models

import java.io.Serializable

/**
 * Created by evoxmusic on 04/09/2017.
 */
data class CommentBlob(var total: Int? = null, var samples: List<Comment>? = null) : Serializable