package io.mysocialapp.client.exceptions

/**
 * Created by evoxmusic on 28/04/2018.
 */
interface ResponseCallback<T> {

    fun onError(e: Throwable)

    fun onSuccess(t: T)

}