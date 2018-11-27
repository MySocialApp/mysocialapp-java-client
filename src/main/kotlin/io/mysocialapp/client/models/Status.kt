package io.mysocialapp.client.models

import rx.Observable

/**
 * Created by evoxmusic on 08/01/15.
 */
class Status(var message: String? = null) : BaseWall() {

    override var bodyMessage: String?
        get() = message
        set(message) {
            this.message = message
        }

    override fun getLikes(): Observable<Like> {
        return session?.clientService?.statusLike?.list(idStr?.toLong())
                ?.flatMapIterable { it }?.map { it.session = session; it } ?: Observable.empty()
    }

    override fun addLike(): Observable<Like> {
        return session?.clientService?.statusLike?.post(idStr?.toLong()) ?: Observable.empty()
    }

    override fun removeLike(): Observable<Void> {
        return session?.clientService?.statusLike?.delete(idStr?.toLong()) ?: Observable.empty()
    }

    override fun listComments(): Observable<Comment> {
        return session?.clientService?.statusComment?.list(idStr?.toLong())
                ?.flatMapIterable { it }?.map { it.session = session; it } ?: Observable.empty()
    }

    override fun delete(): Observable<Void> {
        return session?.clientService?.status?.delete(idStr?.toLong()) ?: Observable.empty()
    }

    override fun save(): Observable<Status> {
        return session?.clientService?.status?.update(idStr?.toLong(), this)?.map { it.session = session; it } ?: Observable.empty()
    }

    override fun addComment(commentPost: CommentPost): Observable<Comment> {
        if (commentPost.multipartPhoto == null && commentPost.comment == null) {
            return Observable.empty()
        } else if (commentPost.multipartPhoto == null && commentPost.comment != null) {
            return session?.clientService?.statusComment?.post(idStr?.toLong(), commentPost.comment)?.map { it.session = session; it }
                    ?: Observable.empty()
        }

        return session?.clientService?.statusComment?.post(idStr?.toLong(), commentPost.multipartPhoto!!.photo,
                commentPost.multipartPhoto.payload, commentPost.multipartPhoto.message, commentPost.multipartPhoto.tagEntities)
                ?.map { it.session = session; it } ?: Observable.empty()
    }

}
