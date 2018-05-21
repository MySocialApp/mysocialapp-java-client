package io.mysocialapp.client.models

import io.mysocialapp.client.Session
import rx.Observable

/**
 * Created by evoxmusic on 10/10/16.
 */
data class PreviewNotification(val configId: String? = null,
                               val type: String? = null,
                               val id: Long? = null,
                               val idStr: String? = null,
                               val total: Int? = null,
                               val lastNotification: Notification? = null) {

    var session: Session? = null

    fun blockingConsume(): PreviewNotification? = consume().toBlocking()?.first()

    fun consume(): Observable<PreviewNotification> {
        return session?.clientService?.notificationUnreadConsume?.get(id) ?: Observable.empty()
    }

}