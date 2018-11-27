package io.mysocialapp.client.models

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import io.mysocialapp.client.extensions.imageMediaType
import io.mysocialapp.client.extensions.toJSONString
import io.mysocialapp.client.extensions.toRequestBody
import okhttp3.RequestBody
import rx.Observable
import java.io.File

/**
 * Created by evoxmusic on 09/01/15.
 */
class Photo(var message: String? = null,
            val url: String? = null,
            @get:JsonProperty("small_url")
            val smallURL: String? = null,
            @get:JsonProperty("medium_url")
            val mediumURL: String? = null,
            @get:JsonProperty("high_url")
            val highURL: String? = null,
            val target: Base? = null,
            var visible: Boolean? = null,
            override var tagEntities: TagEntities? = null) : BaseWall(), Taggable {

    @JsonIgnore
    var multipartPhoto: MultipartPhoto? = null

    override var bodyMessage: String?
        get() = message
        set(message) {
            this.message = message
        }

    override val bodyImageURL: String?
        get() = highURL

    override fun getLikes(): Observable<Like> {
        return session?.clientService?.photoLike?.list(idStr?.toLong())
                ?.flatMapIterable { it }?.map { it.session = session; it } ?: Observable.empty()
    }

    override fun addLike(): Observable<Like> {
        return session?.clientService?.photoLike?.post(idStr?.toLong())?.map { it.session = session; it } ?: Observable.empty()
    }

    override fun removeLike(): Observable<Void> {
        return session?.clientService?.photoLike?.delete(idStr?.toLong()) ?: Observable.empty()
    }

    override fun listComments(): Observable<Comment> {
        return session?.clientService?.photoComment?.list(idStr?.toLong())
                ?.flatMapIterable { it }?.map { it.session = session; it } ?: Observable.empty()
    }

    override fun addComment(commentPost: CommentPost): Observable<Comment> {
        if (commentPost.multipartPhoto == null && commentPost.comment == null) {
            return Observable.empty()
        } else if (commentPost.multipartPhoto == null && commentPost.comment != null) {
            return session?.clientService?.photoComment?.post(idStr?.toLong(), commentPost.comment)?.map { it.session = session; it }
                    ?: Observable.empty()
        }

        return session?.clientService?.photoComment?.post(idStr?.toLong(), commentPost.multipartPhoto!!.photo,
                commentPost.multipartPhoto.payload, commentPost.multipartPhoto.message,
                commentPost.multipartPhoto.tagEntities)?.map { it.session = session; it } ?: Observable.empty()
    }

    override fun delete(): Observable<Void> {
        return session?.clientService?.photo?.delete(idStr?.toLong()) ?: Observable.empty()
    }

    override fun save(): Observable<Photo> {
        return session?.clientService?.photo?.put(id, this)?.map { it.session = session; it } ?: Observable.empty()
    }

    class Builder {
        private var mMessage: String? = null
        private var mImageFile: File? = null
        private var mVisibility: AccessControl = AccessControl.FRIEND
        private var mPayload: Map<String, Any?>? = null

        fun setName(name: String): Builder {
            this.mMessage = name
            return this
        }

        fun setImage(image: File): Builder {
            this.mImageFile = image
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

        fun build(): Photo {
            if (mImageFile == null) {
                throw IllegalArgumentException("Image is mandatory")
            }

            val photo = Photo()
            val photoRequestBody = RequestBody.create(mImageFile?.name?.imageMediaType(), mImageFile!!)

            return photo.apply {
                multipartPhoto = MultipartPhoto(photoRequestBody,
                        mMessage.toRequestBody(),
                        accessControl = mVisibility.name.toRequestBody(),
                        payload = mPayload?.toJSONString().toRequestBody())
            }
        }

    }

}
