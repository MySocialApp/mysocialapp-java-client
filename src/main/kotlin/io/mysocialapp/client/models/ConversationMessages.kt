package io.mysocialapp.client.models

import io.mysocialapp.client.Session
import io.mysocialapp.client.extensions.PaginationResource
import rx.Observable
import java.io.Serializable

/**
 * Created by evoxmusic on 10/12/2017.
 */
data class ConversationMessages(var totalUnreads: Int? = null,
                                val samples: List<ConversationMessage>? = null) : Serializable {

    var conversationId: Long? = null
    var session: Session? = null

    fun blockingStream(limit: Int = Int.MAX_VALUE): Iterable<ConversationMessage> = stream(limit).toBlocking().toIterable()

    fun stream(limit: Int = Int.MAX_VALUE): Observable<ConversationMessage> = list(0, limit)

    fun blockingList(page: Int = 0, size: Int = 10): Iterable<ConversationMessage> = list(page, size).toBlocking().toIterable()

    fun list(page: Int = 0, size: Int = 10): Observable<ConversationMessage> {
        return io.mysocialapp.client.extensions.stream(page, size, object : PaginationResource<ConversationMessage> {
            override fun onNext(page: Int, size: Int): List<ConversationMessage> {
                return session?.clientService?.conversationMessage?.list(conversationId, page, size)?.toBlocking()?.first() ?: emptyList()
            }
        }).map { it?.session = session; it }
    }

    fun blockingStreamAndConsume(limit: Int = Int.MAX_VALUE): Iterable<ConversationMessage> = stream(limit).toBlocking().toIterable()

    fun streamAndConsume(limit: Int = Int.MAX_VALUE): Observable<ConversationMessage> = list(0, limit)

    fun blockingListAndConsume(page: Int = 0, size: Int = 10): Iterable<ConversationMessage> = list(page, size).toBlocking().toIterable()

    fun listAndConsume(page: Int = 0, size: Int = 10): Observable<ConversationMessage> {
        return io.mysocialapp.client.extensions.stream(page, size, object : PaginationResource<ConversationMessage> {
            override fun onNext(page: Int, size: Int): List<ConversationMessage> {
                return session?.clientService?.conversationMessageConsume
                        ?.list(conversationId, page, size)?.toBlocking()?.first() ?: emptyList()
            }
        }).map { it?.session = session; it }
    }

}