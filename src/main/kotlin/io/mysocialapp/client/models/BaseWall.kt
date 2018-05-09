package io.mysocialapp.client.models

import rx.Observable

/**
 * Created by evoxmusic on 10/01/15.
 */
open class BaseWall : Base(), Likable, Commentable {

    private var likes: LikeBlob? = null
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

    override fun getBlockingLikes(): Iterable<Like> = getLikes().toBlocking()?.toIterable() ?: emptyList()

    override fun getLikes(): Observable<Like> {
        return session?.clientService?.feedLike?.list(idStr?.toLong())
                ?.flatMapIterable { it }?.map { it.session = session; it } ?: Observable.empty()
    }

    override fun addBlockingLike(): Like? = addLike().toBlocking()?.first()

    override fun addLike(): Observable<Like> {
        return session?.clientService?.feedLike?.post(idStr?.toLong())?.map { it.session = session; it } ?: Observable.empty()
    }

    override fun deleteBlockingLike() {
        deleteLike().toBlocking()?.first()
    }

    override fun deleteLike(): Observable<Void> {
        return session?.clientService?.feedLike?.delete(idStr?.toLong()) ?: Observable.empty()
    }

    override fun getBlockingComments(): Iterable<Comment> = getComments().toBlocking()?.toIterable() ?: emptyList()

    override fun getComments(): Observable<Comment> {
        return session?.clientService?.feedComment?.list(idStr?.toLong())
                ?.flatMapIterable { it }?.map { it.session = session; it } ?: Observable.empty()
    }

    override fun addBlockingComment(comment: Comment): Comment? = addComment(comment).toBlocking()?.first()

    override fun addComment(comment: Comment): Observable<Comment> {
        return session?.clientService?.feedComment?.post(idStr?.toLong(), comment)?.map { it.session = session; it } ?: Observable.empty()
    }

    override fun addBlockingComment(multipartPhoto: MultipartPhoto): Comment? = addComment(multipartPhoto).toBlocking()?.first()

    override fun addComment(multipartPhoto: MultipartPhoto): Observable<Comment> {
        return if (multipartPhoto.message != null) {
            session?.clientService?.photoComment?.post(idStr?.toLong(), multipartPhoto.photo, multipartPhoto.message)
        } else {
            session?.clientService?.photoComment?.post(idStr?.toLong(), multipartPhoto.photo)
        }?.map { it.session = session; it } ?: Observable.empty()
    }

}
