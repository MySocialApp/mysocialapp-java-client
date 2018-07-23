package io.mysocialapp.client.models

import rx.Observable

/**
 * Created by evoxmusic on 08/01/15.
 */
interface Likable : BaseImpl {

    fun blockingListLikes(): Iterable<Like>

    fun getLikes(): Observable<Like>

    fun setLikersTotal(total: Int?)

    var likersTotal: Int

    var isLiked: Boolean

    fun blockingAddLike(): Like?

    fun addLike(): Observable<Like>

    fun blockingRemoveLike()

    fun removeLike(): Observable<Void>

}
