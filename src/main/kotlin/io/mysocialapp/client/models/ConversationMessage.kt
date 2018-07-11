package io.mysocialapp.client.models

import rx.Observable

/**
 * Created by evoxmusic on 14/01/15.
 */
data class ConversationMessage(val message: String? = null,
                               val photo: Photo? = null,
                               val conversation: Conversation? = null) : Base(), Taggable {

    override var tagEntities: TagEntities? = null

    fun blockingReplyBack(conversationMessagePost: ConversationMessagePost) = replyBack(conversationMessagePost).toBlocking()?.first()

    fun replyBack(conversationMessagePost: ConversationMessagePost): Observable<ConversationMessage> {
        conversation?.session = session
        return conversation?.sendMessage(conversationMessagePost) ?: Observable.empty()
    }

}