package io.mysocialapp.client.models

import rx.Observable

/**
 * Created by evoxmusic on 14/01/15.
 */
data class Conversation(var name: String? = null) : Base() {

    var members: MutableSet<User>? = null
        get() {
            field?.forEach { it.session = session }
            return field
        }

    var messages: ConversationMessages? = null
        get() {
            field?.conversationId = id
            field?.session = session
            return field
        }

    fun blockingSendMessage(message: ConversationMessagePost): ConversationMessage? = sendMessage(message).toBlocking()?.first()

    fun sendMessage(message: ConversationMessagePost): Observable<ConversationMessage> {
        if (message.multipartPhoto == null) {
            return message.conversationMessage?.let {
                session?.clientService?.conversationMessage?.post(id, it)
            }?.map { it.session = session; it } ?: Observable.empty()
        }

        return session?.clientService?.conversationMessagePhoto?.post(id, message.multipartPhoto.photo, message.multipartPhoto.payload,
                message.multipartPhoto.message, message.multipartPhoto.tagEntities)?.map { it.session = session; it } ?: Observable.empty()
    }

    fun blockingKickMember(user: User) = kickMember(user).toBlocking()?.first()

    fun kickMember(user: User): Observable<User> {
        members?.remove(user)
        return save().map { user }
    }

    fun blockingAddMember(user: User) = addMember(user).toBlocking()?.first()

    fun addMember(user: User): Observable<User> {
        members?.add(user)
        return save().map { user }
    }

    override fun blockingDelete() {
        delete().toBlocking()?.first()
    }

    override fun delete(): Observable<Void> {
        return session?.clientService?.conversation?.delete(id) ?: Observable.empty()
    }

    override fun blockingSave(): Conversation? = save().toBlocking()?.first()

    override fun save(): Observable<Conversation> {
        return session?.clientService?.conversation?.update(id, this)?.map { it.session = session; it } ?: Observable.empty()
    }

    fun blockingQuit() = blockingDelete()

    fun quit() = delete()

    class Builder {
        private var mName: String? = null
        private var mMembers = mutableSetOf<User>()

        fun setName(name: String): Builder {
            this.mName = name
            return this
        }

        fun addMember(member: User): Builder {
            this.mMembers.add(member)
            return this
        }

        fun addMembers(members: Set<User>): Builder {
            this.mMembers.addAll(members)
            return this
        }

        fun setMembers(members: Set<User>): Builder {
            this.mMembers = members.toMutableSet()
            return this
        }

        fun build() = Conversation(mName).apply { members = mMembers }
    }

}