package io.mysocialapp.client.models

import rx.Observable

/**
 * Created by evoxmusic on 28/01/15.
 */
class TextWallMessage(var message: String? = null) : BaseWall(), Taggable {

    override var tagEntities: TagEntities? = null

    override var bodyMessage: String?
        get() = message
        set(message) {
            this.message = message
        }

    override fun save(): Observable<TextWallMessage> {
        return session?.clientService?.feedMessage?.put(id, this)?.map { it.session = session; it.baseObject as TextWallMessage }
                ?: Observable.empty<TextWallMessage>()
    }

    override fun delete(): Observable<Void> {
        return session?.clientService?.feed?.delete(id) ?: Observable.empty()
    }

}
