package io.mysocialapp.client.models

import io.mysocialapp.client.extensions.prepareAsync
import rx.Observable

/**
 * Created by evoxmusic on 08/01/15.
 */
class Comment(var message: String? = null,
              var photo: Photo? = null,
              var parent: Base? = null) : Base(), Taggable {

    override var tagEntities: TagEntities? = null

    fun blockingReplyBack(commentPost: CommentPost) = replyBack(commentPost).toBlocking()?.first()

    fun replyBack(commentPost: CommentPost): Observable<Comment> {
        if (parent?.id == null) {
            return Observable.empty()
        }

        return session?.newsFeed?.get(parent?.id!!)?.flatMap { it?.addComment(commentPost)?.prepareAsync() } ?: Observable.empty()
    }

    override fun save(): Observable<Comment> {
        return session?.clientService?.feedComment?.put(id, id, this)?.map { it.session = session; it } ?: Observable.empty()
    }

    override fun delete(): Observable<Void> {
        return session?.clientService?.feedComment?.delete(id, id) ?: Observable.empty()
    }

}
