package io.mysocialapp.client.models

import okhttp3.MediaType
import okhttp3.RequestBody
import rx.Observable

/**
 * Created by evoxmusic on 24/07/18.
 */
data class PhotoAlbum(var name: String? = null,
                      var photos: List<Photo>? = null,
                      var previewPhotos: PhotoBlob? = null) : Base() {

    fun blockingAddPhoto(photo: Photo): Photo? = addPhoto(photo).toBlocking()?.first()

    fun addPhoto(photo: Photo): Observable<Photo> {
        return addPhotoWithFeedResult(photo).map { it.baseObject as Photo }
    }

    fun blockingAddPhotoWithFeedResult(photo: Photo): Feed? = addPhotoWithFeedResult(photo).toBlocking()?.first()

    fun addPhotoWithFeedResult(photo: Photo): Observable<Feed> {
        if (photo.multipartPhoto == null || name.isNullOrBlank()) {
            return Observable.empty()
        }

        val albumName = RequestBody.create(MediaType.parse("multipart/form-data"), name!!)

        return if (photo.multipartPhoto?.message != null && photo.multipartPhoto?.tagEntities != null) {
            session?.clientService?.photo?.postWithAlbumName(photo.multipartPhoto!!.photo, albumName, photo.multipartPhoto?.accessControl!!,
                    photo.multipartPhoto?.message!!, photo.multipartPhoto?.tagEntities!!) ?: Observable.empty()

        } else if (photo.multipartPhoto?.message != null) {
            session?.clientService?.photo?.postWithAlbumName(photo.multipartPhoto!!.photo, albumName, photo.multipartPhoto?.accessControl!!,
                    photo.multipartPhoto?.message!!) ?: Observable.empty()

        } else if (photo.multipartPhoto?.photo != null) {
            session?.clientService?.photo?.postWithAlbumName(photo.multipartPhoto!!.photo, albumName, photo.multipartPhoto?.accessControl!!)
            
        } else {
            null
        }?.map { it.session = session; it } ?: Observable.empty()
    }

    override fun delete(): Observable<Void> {
        return session?.clientService?.photoAlbum?.delete(id) ?: Observable.empty()
    }

    override fun save(): Observable<PhotoAlbum> {
        return session?.clientService?.photoAlbum?.update(id, this) ?: Observable.empty()
    }

    class Builder {
        private var mName: String? = null
        private var mPhotos = mutableListOf<Photo>()

        fun setName(name: String): Builder {
            this.mName = name
            return this
        }

        fun addPhoto(photo: Photo): Builder {
            this.mPhotos.add(photo)
            return this
        }

        fun addPhoto(photos: List<Photo>): Builder {
            this.mPhotos.addAll(photos)
            return this
        }

        fun setPhotos(photos: List<Photo>): Builder {
            this.mPhotos = photos.toMutableList()
            return this
        }

        fun build() = PhotoAlbum(mName, mPhotos)
    }

}