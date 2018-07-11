package io.mysocialapp.client.models

import rx.Observable

/**
 * Created by evoxmusic on 08/01/15.
 */
interface Commentable : BaseImpl {

    fun getBlockingComments(): Iterable<Comment>

    fun getComments(): Observable<Comment>

    fun addBlockingComment(commentPost: CommentPost): Comment?

    fun addComment(commentPost: CommentPost): Observable<Comment>

    var commentsTotal: Int

    val commentsSamples: List<Comment>

}
