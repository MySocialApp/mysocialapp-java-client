package io.mysocialapp.client

import io.mysocialapp.client.exceptions.InvalidCredentialsMySocialAppException
import io.mysocialapp.client.exceptions.MySocialAppException
import io.mysocialapp.client.utils.MyObjectMapper
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import rx.Observable
import java.lang.reflect.Type

/**
 * Created by evoxmusic on 13/06/2018.
 */
class RxErrorHandlingCallAdapterFactory : CallAdapter.Factory() {
    private val original = RxJavaCallAdapterFactory.create()

    companion object {
        fun create(): CallAdapter.Factory = RxErrorHandlingCallAdapterFactory()
    }

    override fun get(returnType: Type, annotations: Array<out Annotation>, retrofit: Retrofit): CallAdapter<Observable<*>, Observable<*>> {
        return RxCallAdapterWrapper(original.get(returnType, annotations, retrofit) as CallAdapter<Observable<*>, Observable<*>>)
    }

    private class RxCallAdapterWrapper(val wrapped: CallAdapter<Observable<*>, Observable<*>>) : CallAdapter<Observable<*>, Observable<*>> {

        override fun responseType(): Type = wrapped.responseType()

        override fun adapt(call: Call<Observable<*>>): Observable<*> {
            return wrapped.adapt(call).onErrorResumeNext { throwable -> Observable.error(asMySocialAppException(throwable)) }
        }

        private fun asMySocialAppException(throwable: Throwable): Throwable {
            // We had non-200 http error
            if (throwable is HttpException) {
                val res = throwable.response()

                if (res.code() >= 400 || res.code() < 200) {
                    val responseBody = res.errorBody()?.string()
                    val exception = MyObjectMapper.objectMapper.readValue(responseBody, MySocialAppException::class.java)

                    if (res.code() == 400 && exception.serverExceptionClass?.endsWith("InvalidCredentialsException") == true) {
                        return MyObjectMapper.objectMapper.readValue(responseBody, InvalidCredentialsMySocialAppException::class.java)
                    }

                    return exception
                }

                throwable.response().errorBody()?.string()
            }

            return throwable
        }

    }

}