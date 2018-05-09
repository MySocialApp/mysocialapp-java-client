package io.mysocialapp.client.models

/**
 * Created by evoxmusic on 08/01/15.
 */
class Comment(var message: String? = null) : Base(), Taggable {

    var photo: Photo? = null
    override var tagEntities: TagEntities? = null

}
