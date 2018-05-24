package io.mysocialapp.client.models

import rx.Observable

/**
 * Created by evoxmusic on 08/01/15.
 */
class Status(var message: String? = null) : BaseWall() {

    override var bodyMessage: String?
        get() = if (message != null) message else displayedName
        set(message) = if (this.message != null) {
            this.message = message
        } else {
            this.displayedName = message
        }

    override fun getLikes(): Observable<Like> {
        return session?.clientService?.statusLike?.list(idStr?.toLong())
                ?.flatMapIterable { it }?.map { it.session = session; it } ?: Observable.empty()
    }

    override fun addLike(): Observable<Like> {
        return session?.clientService?.statusLike?.post(idStr?.toLong()) ?: Observable.empty()
    }

    override fun deleteLike(): Observable<Void> {
        return session?.clientService?.statusLike?.delete(idStr?.toLong()) ?: Observable.empty()
    }

    override fun getComments(): Observable<Comment> {
        return session?.clientService?.statusComment?.list(idStr?.toLong())
                ?.flatMapIterable { it }?.map { it.session = session; it } ?: Observable.empty()
    }

    override fun addComment(comment: Comment): Observable<Comment> {
        return session?.clientService?.statusComment?.post(idStr?.toLong(), comment)?.map { it.session = session; it } ?: Observable.empty()
    }

    override fun delete(): Observable<Void> {
        return session?.clientService?.status?.delete(idStr?.toLong()) ?: Observable.empty()
    }

    override fun save(): Observable<Status> {
        return session?.clientService?.status?.update(idStr?.toLong(), this)?.map { it.session = session; it } ?: Observable.empty()
    }

    override fun addComment(multipartPhoto: MultipartPhoto): Observable<Comment> {
        return if (multipartPhoto.message != null && multipartPhoto.tagEntities != null) {
            session?.clientService?.statusComment?.post(idStr?.toLong(), multipartPhoto.photo, multipartPhoto.message, multipartPhoto.tagEntities)
                    ?: Observable.empty()
        } else if (multipartPhoto.message != null) {
            session?.clientService?.statusComment?.post(idStr?.toLong(), multipartPhoto.photo, multipartPhoto.message)
        } else {
            session?.clientService?.statusComment?.post(idStr?.toLong(), multipartPhoto.photo)
        }?.map { it.session = session; it } ?: Observable.empty()
    }

}
