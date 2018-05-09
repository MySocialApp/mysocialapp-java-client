package io.mysocialapp.client.models

import io.mysocialapp.client.Session
import rx.Observable
import java.io.Serializable
import java.util.*

/**
 * Created by evoxmusic on 10/01/15.
 */
interface Wallable : Serializable {

    val createdDate: Date?

    val owner: User?

    val baseTarget: BaseWall?

    val baseObject: BaseWall?

    var bodyMessage: String?

    val bodyMessageTagEntities: TagEntities?

    val bodyImageURL: String?

    val bodyImageText: String?

    val firstURLTagEntity: URLTag?

    val location: BaseLocation?

    fun getBlockingLikes(): Iterable<Like> = getLikes().toBlocking()?.toIterable() ?: emptyList()

    fun getLikes(): Observable<Like>

    fun addBlockingLike(): Like? = addLike().toBlocking()?.first()

    fun addLike(): Observable<Like>

    fun deleteBlockingLike() {
        deleteLike().toBlocking()?.first()
    }

    fun deleteLike(): Observable<Void>

    fun getBlockingComments(): Iterable<Comment> = getComments().toBlocking()?.toIterable() ?: emptyList()

    fun getComments(): Observable<Comment>

    fun addBlockingComment(comment: Comment): Comment? = addComment(comment).toBlocking()?.first()

    fun addComment(comment: Comment): Observable<Comment>

    fun addBlockingComment(multipart: MultipartPhoto): Comment? = addComment(multipart).toBlocking()?.first()

    fun addComment(multipart: MultipartPhoto): Observable<Comment>

    var session: Session?

}
