package io.mysocialapp.client.models

import io.mysocialapp.client.extensions.imageMediaType
import io.mysocialapp.client.extensions.toJSONString
import io.mysocialapp.client.extensions.toRequestBody
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
        private var mPayload: Map<String, Any?>? = null
        private var mExternalId: String? = null

        fun setMessage(message: String): Builder {
            this.mMessage = message
            return this
        }

        fun setImage(image: File): Builder {
            this.mImage = image
            return this
        }

        fun setVisibility(visibility: AccessControl): Builder {
            this.mVisibility = visibility
            return this
        }

        fun setPayload(payload: Map<String, Any?>): Builder {
            this.mPayload = payload
            return this
        }

        fun setExternalId(externalId: String): Builder {
            this.mExternalId = externalId
            return this
        }

        fun build(): FeedPost {
            if (mMessage == null && mImage == null) {
                throw IllegalArgumentException("Message or image is mandatory")
            } else if (mImage == null) {
                return FeedPost(textWallMessage = TextWallMessage(mMessage).apply {
                    accessControl = mVisibility
                    payload = mPayload
                    externalId = mExternalId
                })
            }

            val photoRequestBody = RequestBody.create(mImage?.name?.imageMediaType(), mImage!!)

            return FeedPost(multipartPhoto = MultipartPhoto(photoRequestBody, mMessage.toRequestBody(),
                    accessControl = mVisibility.name.toRequestBody(), payload = mPayload?.toJSONString().toRequestBody(),
                    externalId = mExternalId.toRequestBody()))
        }
    }

}