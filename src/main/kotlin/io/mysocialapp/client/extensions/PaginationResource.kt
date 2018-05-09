package io.mysocialapp.client.extensions

import rx.Observable

/**
 * Created by evoxmusic on 07/05/2018.
 */
interface PaginationResource<T> {

    fun onNext(page: Int, size: Int): List<T>

}

fun <T> stream(page: Int = 0, size: Int = 10, paginationResource: PaginationResource<T>): Observable<T> {
    return Observable.unsafeCreate<List<T>> {
        try {
            (0 until (size / 10)).forEach { i ->
                val results = paginationResource.onNext(i + page, 10)
                if (results.isEmpty()) {
                    it.onCompleted()
                    return@unsafeCreate
                }

                it.onNext(results)
            }

            if (size % 10 > 0) {
                it.onNext(paginationResource.onNext(size / 10 + page, size % 10))
            }
        } catch (e: Exception) {
            it.onError(e)
        }

        it.onCompleted()
    }.flatMapIterable { it }
}