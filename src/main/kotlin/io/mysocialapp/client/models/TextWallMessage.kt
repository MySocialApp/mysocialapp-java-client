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

    class Builder {
        var message: String? = null
        var visibility: AccessControl = AccessControl.FRIEND

        fun setMessage(message: String?): Builder {
            this.message = message
            return this
        }

        fun setVisibility(visibility: AccessControl): Builder {
            this.visibility = visibility
            return this
        }

        fun build() = TextWallMessage(message).apply { accessControl = visibility }
    }

}
