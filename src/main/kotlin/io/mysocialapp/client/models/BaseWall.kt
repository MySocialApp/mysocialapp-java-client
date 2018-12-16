package io.mysocialapp.client.models

import com.fasterxml.jackson.annotation.JsonProperty
import rx.Observable

/**
 * Created by evoxmusic on 10/01/15.
 */
open class BaseWall : Base(), Likable, Commentable {

    var payload: Map<String, Any?>? = null
    var externalId: String? = null
    @JsonProperty("likes")
    private var likes: LikeBlob? = null
    @JsonProperty("comments")
    private var comments: CommentBlob? = null

    override var commentsTotal: Int
        get() = comments?.total ?: 0
        set(total) {
            comments?.total = total
        }

    override val commentsSamples: List<Comment>
        get() = comments?.samples ?: emptyList()

    override fun setLikersTotal(total: Int?) {
        likes?.total = total
    }

    override var likersTotal: Int
        get() = likes?.total ?: 0
        set(total) {
            likes?.total = total
        }

    override var isLiked: Boolean
        get() = likes?.hasLike ?: false
        set(like) {
            likes?.hasLike = like
        }

    override fun blockingListLikes(): Iterable<Like> = getLikes().toBlocking()?.toIterable() ?: emptyList()

    override fun getLikes(): Observable<Like> {
        return session?.clientService?.feedLike?.list(idStr?.toLong())
                ?.flatMapIterable { it }?.map { it.session = session; it } ?: Observable.empty()
    }

    override fun blockingAddLike(): Like? = addLike().toBlocking()?.first()

    override fun addLike(): Observable<Like> {
        return session?.clientService?.feedLike?.post(idStr?.toLong())?.map { it.session = session; it } ?: Observable.empty()
    }

    override fun blockingRemoveLike() {
        removeLike().toBlocking()?.first()
    }

    override fun removeLike(): Observable<Void> {
        return session?.clientService?.feedLike?.delete(idStr?.toLong()) ?: Observable.empty()
    }

    override fun blockingListComments(): Iterable<Comment> = listComments().toBlocking()?.toIterable() ?: emptyList()

    override fun listComments(): Observable<Comment> {
        return session?.clientService?.feedComment?.list(idStr?.toLong())
                ?.flatMapIterable { it }?.map { it.session = session; it } ?: Observable.empty()
    }

    override fun blockingAddComment(commentPost: CommentPost): Comment? = addComment(commentPost).toBlocking()?.first()

    override fun addComment(commentPost: CommentPost): Observable<Comment> {
        if (commentPost.multipartPhoto == null && commentPost.comment == null) {
            return Observable.empty()
        }

        if (commentPost.multipartPhoto == null && commentPost.comment != null) {
            return session?.clientService?.feedComment?.post(idStr?.toLong(), commentPost.comment)?.map { it.session = session; it }
                    ?: Observable.empty()
        }

        return session?.clientService?.photoComment?.post(idStr?.toLong(), commentPost.multipartPhoto!!.photo,
                commentPost.multipartPhoto.payload, commentPost.multipartPhoto.externalId, commentPost.multipartPhoto.message,
                commentPost.multipartPhoto.tagEntities)?.map { it.session = session; it } ?: Observable.empty()
    }

    fun ignore(): Observable<Void> {
        return session?.clientService?.feedIgnore?.post(id) ?: Observable.empty()
    }

    fun abuse(): Observable<Void> {
        return session?.clientService?.feedAbuse?.post(id) ?: Observable.empty()
    }

    override fun delete(): Observable<Void> {
        return session?.clientService?.feed?.delete(id) ?: Observable.empty()
    }

}
