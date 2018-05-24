package io.mysocialapp.client.models

import io.mysocialapp.client.Session
import rx.Observable
import java.io.Serializable
import java.util.*

/**
 * Created by evoxmusic on 18/01/15.
 */
interface BaseImpl : Serializable {

    var type: String?

    var id: Long?

    var idStr: String?

    var createdDate: Date?

    var displayedName: String?

    var displayedPhoto: Photo?

    val owner: User?

    val bodyImageURL: String?

    val bodyImageText: String?

    var bodyMessage: String?

    var entityType: EntityType?

    var accessControl: AccessControl?

    fun blockingDelete() = delete().toBlocking()?.first()

    fun delete(): Observable<*>

    fun blockingSave(): Any? = save().toBlocking()?.first()

    fun save(): Observable<*>

    var session: Session?

}
