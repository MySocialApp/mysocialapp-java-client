package io.mysocialapp.client

import io.mysocialapp.client.extensions.PaginationResource
import io.mysocialapp.client.extensions.stream
import io.mysocialapp.client.models.PreviewNotification
import rx.Observable

/**
 * Created by evoxmusic on 21/05/2018.
 */
class FluentNotification(private val session: Session) {

    val unread by lazy { Unread(session) }
    val read by lazy { Read(session) }

    fun addNotificationListener(notificationCallback: NotificationCallback) =
            session.webSocketService.addNotificationListener(notificationCallback)

    class Unread(private val session: Session) {

        fun blockingTotal(): Int = total().toBlocking().first()

        fun total(): Observable<Int> {
            return session.clientService.accountEvent.get().map { it.notification?.totalUnreads ?: 0 }
        }

        @JvmOverloads
        fun blockingStream(limit: Int = Int.MAX_VALUE): Iterable<PreviewNotification> = stream(limit).toBlocking().toIterable()

        @JvmOverloads
        fun stream(limit: Int = Int.MAX_VALUE): Observable<PreviewNotification> = list(0, limit)

        @JvmOverloads
        fun blockingList(page: Int = 0, size: Int = 10): Iterable<PreviewNotification> = list(page, size).toBlocking().toIterable()

        @JvmOverloads
        fun list(page: Int = 0, size: Int = 10): Observable<PreviewNotification> {
            return stream(page, size, object : PaginationResource<PreviewNotification> {
                override fun getRealResultObject(response: List<PreviewNotification>): List<Any>? = response

                override fun onNext(page: Int, size: Int): List<PreviewNotification> {
                    return session.clientService.notificationUnread.list(page, size).toBlocking().first() ?: emptyList()
                }
            }).map { it?.session = session; it }
        }

        fun blockingListAndConsume(): Iterable<PreviewNotification> = listAndConsume().toBlocking().toIterable()

        fun listAndConsume(): Observable<PreviewNotification> {
            return session.clientService.notificationUnreadConsume.list().flatMapIterable { it }.map { it?.session = session; it }
        }

    }

    class Read(private val session: Session) {

        @JvmOverloads
        fun blockingStream(limit: Int = Int.MAX_VALUE): Iterable<PreviewNotification> = stream(limit).toBlocking().toIterable()

        @JvmOverloads
        fun stream(limit: Int = Int.MAX_VALUE): Observable<PreviewNotification> = list(0, limit)

        @JvmOverloads
        fun blockingList(page: Int = 0, size: Int = 10): Iterable<PreviewNotification> = list(page, size).toBlocking().toIterable()

        @JvmOverloads
        fun list(page: Int = 0, size: Int = 10): Observable<PreviewNotification> {
            return stream(page, size, object : PaginationResource<PreviewNotification> {
                override fun getRealResultObject(response: List<PreviewNotification>): List<Any>? = response

                override fun onNext(page: Int, size: Int): List<PreviewNotification> {
                    return session.clientService.notificationRead.list(page, size).toBlocking().first() ?: emptyList()
                }
            }).map { it?.session = session; it }
        }

    }

}