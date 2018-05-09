package io.mysocialapp.client.models

import java.io.Serializable

/**
 * Created by evoxmusic on 04/09/2017.
 */
data class LikeBlob(var total: Int? = null, var hasLike: Boolean? = null, var samples: List<Like>? = null) : Serializable