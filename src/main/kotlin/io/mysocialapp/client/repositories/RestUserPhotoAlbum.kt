package io.mysocialapp.client.repositories

import io.mysocialapp.client.models.PhotoAlbum
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import rx.Observable

/**
 * Created by evoxmusic on 24/10/18.
 */
interface RestUserPhotoAlbum {

    @GET("user/{id}/photo/album")
    fun list(@Path("id") id: Long?, @Query("page") page: Int, @Query("size") size: Int): Observable<List<PhotoAlbum>>

}
