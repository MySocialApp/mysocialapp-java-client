package io.mysocialapp.client.models

import io.mysocialapp.client.extensions.imageMediaType
import io.mysocialapp.client.extensions.toRequestBody
import okhttp3.RequestBody
import java.io.File

/**
 * Created by evoxmusic on 24/05/2018.
 */
data class ConversationMessagePost(val conversationMessage: ConversationMessage? = null,
                                   val multipartPhoto: MultipartPhoto? = null) {

    class Builder {
        private var mMessage: String? = null
        private var mImage: File? = null

        fun setMessage(message: String?): Builder {
            this.mMessage = message
            return this
        }

        fun setImage(image: File?): Builder {
            this.mImage = image
            return this
        }

        fun build(): ConversationMessagePost {
            if (mMessage == null && mImage == null) {
                throw IllegalArgumentException("Message or image is mandatory")
            } else if (mImage == null) {
                return ConversationMessagePost(conversationMessage = ConversationMessage(mMessage))
            }

            val photoRequestBody = RequestBody.create(mImage?.name?.imageMediaType(), mImage!!)

            return ConversationMessagePost(multipartPhoto = MultipartPhoto(photoRequestBody, mMessage.toRequestBody()))
        }
    }

}