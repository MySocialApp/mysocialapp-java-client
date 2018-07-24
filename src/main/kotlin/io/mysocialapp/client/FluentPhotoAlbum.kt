package io.mysocialapp.client

import io.mysocialapp.client.extensions.prepareAsync
import io.mysocialapp.client.models.PhotoAlbum
import rx.Observable

/**
 * Created by evoxmusic on 24/07/2018.
 */
class FluentPhotoAlbum(private val session: Session) {

    @JvmOverloads
    fun blockingStream(limit: Int = Int.MAX_VALUE): Iterable<PhotoAlbum> = stream(limit).toBlocking().toIterable()

    @JvmOverloads
    fun stream(limit: Int = Int.MAX_VALUE): Observable<PhotoAlbum> = list(0, limit)

    @JvmOverloads
    fun blockingList(page: Int = 0, size: Int = 10): Iterable<PhotoAlbum> = list(page, size).toBlocking()?.toIterable() ?: emptyList()

    @JvmOverloads
    fun list(page: Int = 0, size: Int = 10): Observable<PhotoAlbum> {
        return session.clientService.photoAlbum.list(page, size).flatMapIterable { it }.map { it.session = session; it }
    }

    fun blockingGet(id: Long): PhotoAlbum? = get(id).toBlocking()?.first()

    fun get(id: Long): Observable<PhotoAlbum> = session.clientService.photoAlbum.get(id).map { it.session = session; it }

    fun blockingCreate(photoAlbum: PhotoAlbum): PhotoAlbum? = create(photoAlbum).toBlocking()?.first()

    fun create(photoAlbum: PhotoAlbum): Observable<PhotoAlbum> {
        val photoAlbumObservable = session.clientService.photoAlbum.post(photoAlbum).map { it.session = session; it }.cache()

        if (photoAlbum.photos?.isEmpty() == true) {
            // there is no photos to upload
            return photoAlbumObservable
        }

        return photoAlbumObservable.flatMap { mPhotoAlbum ->
            Observable.merge(photoAlbum.photos?.filter { it.multipartPhoto != null }?.map { mPhotoAlbum.addPhoto(it).prepareAsync() }).prepareAsync()
        }.flatMap {
            photoAlbumObservable.flatMap { get(it.id ?: -1L).prepareAsync() }.prepareAsync()
        }.map { it.session = session; it }
    }

}