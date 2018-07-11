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

        fun blockingStream(limit: Int = Int.MAX_VALUE): Iterable<PreviewNotification> = stream(limit).toBlocking().toIterable()

        fun stream(limit: Int = Int.MAX_VALUE): Observable<PreviewNotification> = list(0, limit)

        fun blockingList(page: Int = 0, size: Int = 10): Iterable<PreviewNotification> = list(page, size).toBlocking().toIterable()

        fun list(page: Int = 0, size: Int = 10): Observable<PreviewNotification> {
            return stream(page, size, object : PaginationResource<PreviewNotification> {
                override fun onNext(page: Int, size: Int): List<PreviewNotification> {
                    return session.clientService.notificationUnread.list(page, size).toBlocking().first() ?: emptyList()
                }
            }).map { it?.session = session; it }
        }

    }

    class Read(private val session: Session) {

        fun blockingStream(limit: Int = Int.MAX_VALUE): Iterable<PreviewNotification> = stream(limit).toBlocking().toIterable()

        fun stream(limit: Int = Int.MAX_VALUE): Observable<PreviewNotification> = list(0, limit)

        fun blockingList(page: Int = 0, size: Int = 10): Iterable<PreviewNotification> = list(page, size).toBlocking().toIterable()

        fun list(page: Int = 0, size: Int = 10): Observable<PreviewNotification> {
            return stream(page, size, object : PaginationResource<PreviewNotification> {
                override fun onNext(page: Int, size: Int): List<PreviewNotification> {
                    return session.clientService.notificationRead.list(page, size).toBlocking().first() ?: emptyList()
                }
            }).map { it?.session = session; it }
        }

    }

}