package io.mysocialapp.client.models

/**
 * Created by evoxmusic on 28/01/15.
 */
class TextWallMessage(var message: String? = null) : BaseWall(), Taggable {

    override var tagEntities: TagEntities? = null

    override var bodyMessage: String?
        get() = message
        set(message) {
            this.message = message
        }

}
