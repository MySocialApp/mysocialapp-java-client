package io.mysocialapp.client

import io.mysocialapp.client.extensions.PaginationResource
import io.mysocialapp.client.models.Conversation
import rx.Observable

/**
 * Created by evoxmusic on 24/05/2018.
 */
class FluentConversation(private val session: Session) {

    @JvmOverloads
    fun blockingStream(limit: Int = Int.MAX_VALUE): Iterable<Conversation> = stream(limit).toBlocking().toIterable()

    @JvmOverloads
    fun stream(limit: Int = Int.MAX_VALUE): Observable<Conversation> = list(0, limit)

    @JvmOverloads
    fun blockingList(page: Int = 0, size: Int = 10): Iterable<Conversation> = list(page, size).toBlocking().toIterable()

    @JvmOverloads
    fun list(page: Int = 0, size: Int = 10): Observable<Conversation> {
        return io.mysocialapp.client.extensions.stream(page, size, object : PaginationResource<Conversation> {
            override fun getRealResultObject(response: List<Conversation>): List<Any>? = response

            override fun onNext(page: Int, size: Int): List<Conversation> {
                return session.clientService.conversation.list(page, size).toBlocking().first()
            }
        }).map { it?.session = session; it }
    }

    fun blockingGet(id: Long): Conversation? = get(id).toBlocking()?.first()

    fun get(id: Long): Observable<Conversation> = session.clientService.conversation.get(id).map { it.session = session; it }

    fun blockingCreate(conversation: Conversation): Conversation? = create(conversation).toBlocking()?.first()

    fun create(conversation: Conversation): Observable<Conversation> {
        return session.clientService.conversation.post(conversation).map { it.session = session; it }
    }

}