package io.mysocialapp.client.models

import com.fasterxml.jackson.annotation.JsonProperty
import io.mysocialapp.client.Session
import io.mysocialapp.client.extensions.convertToBase
import rx.Observable
import java.util.*

/**
 * Created by evoxmusic on 04/09/2017.
 */
open class Feed(val action: ActivityType? = null,
                @get:JsonProperty("object") val rawObject: Map<String, Any?>? = null,
                val languageZone: UserSettings.LanguageZone? = null,
                val actor: User? = null,
                @get:JsonProperty("target") val rawTarget: Map<String, Any?>? = null,
                val accessControl: AccessControl? = null,
                val summary: String? = null) : Wallable {

    private fun convert(map: Map<String, Any?>?): BaseWall? = map?.convertToBase() as? BaseWall

    override var session: Session? = null

    override val `object`: BaseWall? by lazy { convert(rawObject)?.also { it.session = session } }
    override val target: BaseWall? by lazy { convert(rawTarget)?.also { it.session = session } }

    override val id: Long?
        get() = `object`?.id

    override val createdDate: Date?
        get() = `object`?.createdDate

    override val owner: User?
        get() = actor

    override var bodyMessage: String?
        get() = `object`?.bodyMessage
        set(message) {
            `object`?.bodyMessage = message
        }

    override val bodyMessageTagEntities: TagEntities?
        get() = (`object` as? Taggable)?.tagEntities

    override val bodyImageURL: String?
        get() = `object`?.bodyImageURL

    override val bodyImageText: String?
        get() = `object`?.bodyImageText

    override val firstURLTagEntity: URLTag?
        get() = (`object` as? Taggable)?.tagEntities?.urlTags?.firstOrNull()

    override val location: BaseLocation?
        get() = (`object` as? Localizable)?.getLocality()

    override val payload: Map<String, Any?>?
        get() = `object`?.payload

    override fun listLikes(): Observable<Like> {
        return `object`?.getLikes()?.map { it.session = session; it } ?: Observable.empty()
    }

    override fun addLike(): Observable<Like> {
        return `object`?.addLike()?.map { it.session = session; it } ?: Observable.empty()
    }

    override fun removeLike(): Observable<Void> {
        return `object`?.removeLike() ?: Observable.empty()
    }

    override fun listComments(): Observable<Comment> {
        return `object`?.listComments()?.map { it.session = session; it } ?: Observable.empty()
    }

    override fun addComment(commentPost: CommentPost): Observable<Comment> {
        return `object`?.addComment(commentPost) ?: Observable.empty()
    }

    override fun ignore(): Observable<Void> {
        return session?.clientService?.feedIgnore?.post(`object`?.id) ?: Observable.empty()
    }

    override fun abuse(): Observable<Void> {
        return session?.clientService?.feedAbuse?.post(`object`?.id) ?: Observable.empty()
    }

    override fun delete(): Observable<Void> {
        return session?.clientService?.feed?.delete(`object`?.id) ?: Observable.empty()
    }

    override fun save(): Observable<*> {
        return `object`?.save() ?: Observable.empty<Void>()
    }

}