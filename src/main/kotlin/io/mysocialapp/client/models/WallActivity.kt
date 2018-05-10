package io.mysocialapp.client.models

import io.mysocialapp.client.Session
import io.mysocialapp.client.utils.MyObjectMapper
import rx.Observable
import java.util.*

/**
 * Created by evoxmusic on 04/09/2017.
 */
abstract class WallActivity(val action: ActivityType? = null,
                            val `object`: Map<String, Any?>? = null,
                            val languageZone: UserSettings.LanguageZone? = null,
                            val actor: User? = null,
                            val target: Map<String, Any?>? = null,
                            val accessControl: AccessControl? = null,
                            val summary: String? = null) : Wallable {

    private fun convert(map: Map<String, Any?>?): BaseWall? {
        return when (map?.get("entity_type")?.let { EntityType.valueOf(it.toString()) }) {
            EntityType.TEXT_WALL_MESSAGE -> MyObjectMapper.objectMapper.convertValue(map, TextWallMessage::class.java)
            EntityType.PHOTO -> MyObjectMapper.objectMapper.convertValue(map, Photo::class.java)
            EntityType.EVENT -> MyObjectMapper.objectMapper.convertValue(map, Event::class.java)
            EntityType.RIDE -> MyObjectMapper.objectMapper.convertValue(map, Ride::class.java)
            EntityType.GROUP -> MyObjectMapper.objectMapper.convertValue(map, Group::class.java)
            EntityType.STATUS -> MyObjectMapper.objectMapper.convertValue(map, Status::class.java)
            else -> null
        }
    }

    override var session: Session? = null

    override val baseObject: BaseWall? by lazy { convert(`object`)?.also { it.session = session } }
    override val baseTarget: BaseWall? by lazy { convert(target)?.also { it.session = session } }

    override val createdDate: Date?
        get() = baseObject?.createdDate

    override val owner: User?
        get() = actor

    override var bodyMessage: String?
        get() = baseObject?.bodyMessage
        set(message) {
            baseObject?.bodyMessage = message
        }

    override val bodyMessageTagEntities: TagEntities?
        get() = (baseObject as? Taggable)?.tagEntities

    override val bodyImageURL: String?
        get() = baseObject?.bodyImageURL

    override val bodyImageText: String?
        get() = baseObject?.bodyImageText

    override val firstURLTagEntity: URLTag?
        get() = (baseObject as? Taggable)?.tagEntities?.urlTags?.firstOrNull()

    override val location: BaseLocation?
        get() = (baseObject as? Localizable)?.getLocality()

    override fun getLikes(): Observable<Like> {
        return baseObject?.getLikes()?.map { it.session = session; it } ?: Observable.empty()
    }

    override fun addLike(): Observable<Like> {
        return baseObject?.addLike()?.map { it.session = session; it } ?: Observable.empty()
    }

    override fun deleteLike(): Observable<Void> {
        return baseObject?.deleteLike() ?: Observable.empty()
    }

    override fun getComments(): Observable<Comment> {
        return baseObject?.getComments()?.map { it.session = session; it } ?: Observable.empty()
    }

    override fun addComment(comment: Comment): Observable<Comment> {
        return baseObject?.addComment(comment) ?: Observable.empty()
    }

    override fun addComment(multipart: MultipartPhoto): Observable<Comment> {
        return baseObject?.addComment(multipart) ?: Observable.empty()
    }

}