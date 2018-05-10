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
        private var mMessage: String? = null
        private var mVisibility: AccessControl = AccessControl.FRIEND

        fun setMessage(message: String?): Builder {
            this.mMessage = message
            return this
        }

        fun setVisibility(visibility: AccessControl): Builder {
            this.mVisibility = visibility
            return this
        }

        fun build() = TextWallMessage(mMessage).apply { accessControl = mVisibility }
    }

}
