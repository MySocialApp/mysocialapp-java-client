package io.mysocialapp.client.models

/**
 * Created by evoxmusic on 14/01/15.
 */
data class ConversationMessage(val message: String? = null,
                               val photo: Photo? = null) : Base(), Taggable {

    override var tagEntities: TagEntities? = null

}