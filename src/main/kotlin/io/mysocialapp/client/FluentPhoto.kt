package io.mysocialapp.client

import io.mysocialapp.client.models.Photo
import rx.Observable

/**
 * Created by evoxmusic on 29/07/2018.
 */
class FluentPhoto(private val session: Session) {

    @JvmOverloads
    fun blockingStream(limit: Int = Int.MAX_VALUE): Iterable<Photo> = stream(limit).toBlocking().toIterable()

    @JvmOverloads
    fun stream(limit: Int = Int.MAX_VALUE): Observable<Photo> = list(0, limit)

    @JvmOverloads
    fun blockingList(page: Int = 0, size: Int = 10): Iterable<Photo> = list(page, size).toBlocking()?.toIterable() ?: emptyList()

    @JvmOverloads
    fun list(page: Int = 0, size: Int = 10): Observable<Photo> {
        return session.clientService.photo.list(page, size).flatMapIterable { it }.map { it.session = session; it }
    }

    fun blockingGet(id: Long): Photo? = get(id).toBlocking()?.first()

    fun get(id: Long): Observable<Photo> = session.clientService.photo.get(id).map { it.session = session; it }

    fun blockingCreate(photo: Photo): Photo? = create(photo).toBlocking()?.first()

    fun create(photo: Photo): Observable<Photo> {
        if (photo.multipartPhoto == null) {
            return Observable.empty()
        }

        return if (photo.multipartPhoto?.message != null && photo.multipartPhoto?.tagEntities != null) {
            session.clientService.photo.post(photo.multipartPhoto!!.photo, photo.multipartPhoto?.accessControl!!,
                    photo.multipartPhoto?.message!!, photo.multipartPhoto?.tagEntities!!).map { it.`object` as Photo }

        } else if (photo.multipartPhoto?.message != null) {
            session.clientService.photo.post(photo.multipartPhoto!!.photo, photo.multipartPhoto?.accessControl!!,
                    photo.multipartPhoto?.message!!).map { it.`object` as Photo }

        } else if (photo.multipartPhoto?.photo != null) {
            session.clientService.photo.post(photo.multipartPhoto!!.photo, photo.multipartPhoto?.accessControl!!)

        } else {
            null
        }?.map { it.session = session; it.`object` as Photo } ?: Observable.empty()
    }

    fun blockingDelete(id: Long) {
        delete(id).toBlocking()?.first()
    }

    fun delete(id: Long): Observable<Void> {
        return session.clientService.photo.delete(id)
    }

}