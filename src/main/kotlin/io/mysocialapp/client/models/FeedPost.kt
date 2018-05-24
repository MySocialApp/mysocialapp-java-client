package io.mysocialapp.client.models

import io.mysocialapp.client.extensions.imageMediaType
import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.File

/**
 * Created by evoxmusic on 24/05/2018.
 */
data class FeedPost(val textWallMessage: TextWallMessage? = null,
                    val multipartPhoto: MultipartPhoto? = null) {

    class Builder {
        private var mMessage: String? = null
        private var mImage: File? = null
        private var mVisibility: AccessControl = AccessControl.FRIEND

        fun setMessage(message: String?): FeedPost.Builder {
            this.mMessage = message
            return this
        }

        fun setImage(image: File?): FeedPost.Builder {
            this.mImage = image
            return this
        }

        fun setVisibility(visibility: AccessControl): FeedPost.Builder {
            this.mVisibility = visibility
            return this
        }

        fun build(): FeedPost {
            if (mMessage == null && mImage == null) {
                throw IllegalArgumentException("Message or image is mandatory")
            } else if (mImage == null) {
                return FeedPost(textWallMessage = TextWallMessage(mMessage).apply { accessControl = mVisibility })
            }

            val photoRequestBody = RequestBody.create(mImage?.name?.imageMediaType(), mImage!!)
            val messageRequestBody = if (mMessage.isNullOrBlank()) null else RequestBody.create(MediaType.parse("multipart/form-data"), mMessage!!)
            val accessControlRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), mVisibility.name)

            return FeedPost(multipartPhoto = MultipartPhoto(photoRequestBody, messageRequestBody, accessControl = accessControlRequestBody))
        }
    }

}