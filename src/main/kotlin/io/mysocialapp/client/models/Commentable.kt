package io.mysocialapp.client.models

import rx.Observable

/**
 * Created by evoxmusic on 08/01/15.
 */
interface Commentable : BaseImpl {

    fun blockingListComments(): Iterable<Comment>

    fun listComments(): Observable<Comment>

    fun blockingAddComment(commentPost: CommentPost): Comment?

    fun addComment(commentPost: CommentPost): Observable<Comment>

    var commentsTotal: Int

    val commentsSamples: List<Comment>

}
