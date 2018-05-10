package io.mysocialapp.client.models

import com.fasterxml.jackson.annotation.JsonProperty
import io.mysocialapp.client.extensions.imageMediaType
import okhttp3.MediaType
import okhttp3.RequestBody
import rx.Observable
import java.io.File

/**
 * Created by evoxmusic on 09/01/15.
 */
class Photo : BaseWall(), Taggable {

    var message: String? = null
    var url: String? = null
    @JsonProperty("small_url")
    val smallURL: String? = null
    @JsonProperty("medium_url")
    val mediumURL: String? = null
    @JsonProperty("high_url")
    val highURL: String? = null
    val target: Base? = null
    var visible: Boolean? = null
    override var tagEntities: TagEntities? = null

    override var bodyMessage: String?
        get() = message
        set(message) {
            this.message = message
        }

    override val bodyImageURL: String?
        get() = highURL

    class Builder {
        private var mMessage: String? = null
        private var mImage: File? = null
        private var mVisibility: AccessControl = AccessControl.FRIEND

        fun setMessage(message: String?): Builder {
            this.mMessage = message
            return this
        }

        fun setImage(image: File?): Builder {
            this.mImage = image
            return this
        }

        fun setVisibility(visibility: AccessControl): Builder {
            this.mVisibility = visibility
            return this
        }

        fun build(): MultipartPhoto {
            if (mImage == null) {
                throw IllegalArgumentException("image file is mandatory")
            }

            val photoRequestBody = RequestBody.create(mImage?.name?.imageMediaType(), mImage!!)
            val messageRequestBody = if (mMessage.isNullOrBlank()) null else RequestBody.create(MediaType.parse("multipart/form-data"), mMessage!!)
            val accessControlRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), mVisibility.name)

            return MultipartPhoto(photoRequestBody, messageRequestBody, accessControl = accessControlRequestBody)
        }
    }

    override fun getLikes(): Observable<Like> {
        return session?.clientService?.photoLike?.list(idStr?.toLong())
                ?.flatMapIterable { it }?.map { it.session = session; it } ?: Observable.empty()
    }

    override fun addLike(): Observable<Like> {
        return session?.clientService?.photoLike?.post(idStr?.toLong())?.map { it.session = session; it } ?: Observable.empty()
    }

    override fun deleteLike(): Observable<Void> {
        return session?.clientService?.photoLike?.delete(idStr?.toLong()) ?: Observable.empty()
    }

    override fun getComments(): Observable<Comment> {
        return session?.clientService?.photoComment?.list(idStr?.toLong())
                ?.flatMapIterable { it }?.map { it.session = session; it } ?: Observable.empty()
    }

    override fun addComment(comment: Comment): Observable<Comment> {
        return session?.clientService?.photoComment?.post(idStr?.toLong(), comment)?.map { it.session = session; it } ?: Observable.empty()
    }

    override fun addComment(multipartPhoto: MultipartPhoto): Observable<Comment> {
        return if (multipartPhoto.message != null && multipartPhoto.tagEntities != null) {
            session?.clientService?.photoComment?.post(idStr?.toLong(), multipartPhoto.photo, multipartPhoto.message, multipartPhoto.tagEntities)
                    ?: Observable.empty()
        } else if (multipartPhoto.message != null) {
            session?.clientService?.photoComment?.post(idStr?.toLong(), multipartPhoto.photo, multipartPhoto.message)
        } else {
            session?.clientService?.photoComment?.post(idStr?.toLong(), multipartPhoto.photo)
        }?.map { it.session = session; it } ?: Observable.empty()
    }

    override fun delete(): Observable<Void> {
        return session?.clientService?.photo?.delete(idStr?.toLong()) ?: Observable.empty()
    }

}
