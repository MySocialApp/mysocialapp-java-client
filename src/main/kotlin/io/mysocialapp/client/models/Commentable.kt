package io.mysocialapp.client.models

import rx.Observable

/**
 * Created by evoxmusic on 08/01/15.
 */
interface Commentable : BaseImpl {

    fun getBlockingComments(): Iterable<Comment>

    fun getComments(): Observable<Comment>

    fun addBlockingComment(comment: Comment): Comment?

    fun addComment(comment: Comment): Observable<Comment>

    fun addBlockingComment(multipartPhoto: MultipartPhoto): Comment?

    fun addComment(multipartPhoto: MultipartPhoto): Observable<Comment>

    var commentsTotal: Int

    val commentsSamples: List<Comment>

}
