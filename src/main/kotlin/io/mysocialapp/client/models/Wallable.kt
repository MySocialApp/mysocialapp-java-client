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

    val target: BaseWall?

    val `object`: BaseWall?

    var bodyMessage: String?

    val bodyMessageTagEntities: TagEntities?

    val bodyImageURL: String?

    val bodyImageText: String?

    val firstURLTagEntity: URLTag?

    val location: BaseLocation?

    fun blockingListLikes(): Iterable<Like> = listLikes().toBlocking()?.toIterable() ?: emptyList()

    fun listLikes(): Observable<Like>

    fun blockingAddLike(): Like? = addLike().toBlocking()?.first()

    fun addLike(): Observable<Like>

    fun blockingRemoveLike() = removeLike().toBlocking()?.first()

    fun removeLike(): Observable<Void>

    fun blockingListComments(): Iterable<Comment> = listComments().toBlocking()?.toIterable() ?: emptyList()

    fun listComments(): Observable<Comment>

    fun blockingAddComment(commentPost: CommentPost): Comment? = addComment(commentPost).toBlocking()?.first()

    fun addComment(commentPost: CommentPost): Observable<Comment>

    fun blockingIgnore() = ignore().toBlocking()?.first()

    fun ignore(): Observable<Void>

    fun blockingAbuse() = abuse().toBlocking()?.first()

    fun abuse(): Observable<Void>

    fun blockingDelete() = delete().toBlocking()?.first()

    fun delete(): Observable<Void>

    fun blockingSave() = save().toBlocking()?.first()

    fun save(): Observable<*>

    var session: Session?

}
