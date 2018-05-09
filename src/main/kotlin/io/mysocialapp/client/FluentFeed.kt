package io.mysocialapp.client

import io.mysocialapp.client.extensions.PaginationResource
import io.mysocialapp.client.extensions.stream
import io.mysocialapp.client.models.Feed
import io.mysocialapp.client.models.MultipartPhoto
import io.mysocialapp.client.models.TextWallMessage
import rx.Observable

/**
 * Created by evoxmusic on 28/04/2018.
 */
class FluentFeed(private val session: Session) {

    fun blockingStream(limit: Int = Int.MAX_VALUE): Iterable<Feed> = stream(limit).toBlocking().toIterable()

    fun stream(limit: Int = Int.MAX_VALUE): Observable<Feed> = list(0, limit)

    fun blockingList(page: Int = 0, size: Int = 10): Iterable<Feed> = list(page, size).toBlocking().toIterable()

    fun list(page: Int = 0, size: Int = 10): Observable<Feed> {
        return stream(page, size, object : PaginationResource<Feed> {
            override fun onNext(page: Int, size: Int): List<Feed> {
                return session.clientService.feed.list(page, size).toBlocking().first() ?: emptyList()
            }
        }).map { it.session = session; it }
    }

    fun blockingSendWallPost(textWallMessage: TextWallMessage): Feed = sendWallPost(textWallMessage).toBlocking().first()

    fun sendWallPost(textWallMessage: TextWallMessage): Observable<Feed> {
        return session.account.get().map { it.blockingSendWallPost(textWallMessage) }
    }

    fun blockingSendWallPost(multipartPhoto: MultipartPhoto): Feed = sendWallPost(multipartPhoto).toBlocking().first()

    fun sendWallPost(multipartPhoto: MultipartPhoto): Observable<Feed> {
        return session.account.get().map { it.blockingSendWallPost(multipartPhoto) }
    }

}