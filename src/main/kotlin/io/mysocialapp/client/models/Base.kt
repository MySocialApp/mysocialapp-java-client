package io.mysocialapp.client.models

import com.fasterxml.jackson.annotation.JsonIgnore
import io.mysocialapp.client.Session
import rx.Observable
import java.util.*

/**
 * Created by evoxmusic on 11/01/17.
 */
open class Base : BaseImpl {

    override var id: Long? = null
    override var idStr: String? = null
    override var type: String? = null
    override var createdDate: Date? = null
    override var displayedName: String? = null
    override var displayedPhoto: Photo? = null
    override var entityType: EntityType? = null
    override var accessControl: AccessControl? = null
    override var owner: User? = null

    @JsonIgnore
    override var session: Session? = null

    override var bodyMessage: String?
        get() = null
        set(value) = Unit

    override val bodyImageURL: String?
        get() = null

    override val bodyImageText: String?
        get() = null

    override fun delete(): Observable<*> {
        return Observable.empty<Any>()
    }

    override fun save(): Observable<*> {
        return Observable.empty<Any>()
    }

}