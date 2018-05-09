package io.mysocialapp.client.models

import java.io.Serializable

/**
 * Created by evoxmusic on 13/09/15.
 */
interface TagEntity : Serializable {

    var type: String?

    var text: String?

    val textShown: String?

    var indices: IntArray?

}
