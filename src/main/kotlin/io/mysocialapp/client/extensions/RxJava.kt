package io.mysocialapp.client.extensions

import rx.Observable
import rx.Subscription
import rx.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * Created by evoxmusic on 27/03/16.
 */

fun <T> Observable<T>.prepareAsync(): Observable<T> = this.subscribeOn(Schedulers.io())
        .onErrorResumeNext(Observable.empty<T>())
        .doOnError { it.printStackTrace() }

fun <T> Observable<T>.subscribeAsync(cls: (onNext: T) -> Unit = {}): Subscription = this.prepareAsync().subscribe { cls(it) }

fun <T> Observable<T>.subscribeAsync(doOnError: (throwable: Throwable) -> Unit, cls: (onNext: T) -> Unit = {}): Subscription =
        this.prepareAsync().subscribe(cls, doOnError)

fun <T> Observable<T>.prepareAsyncBackground(): Observable<T> = this.subscribeOn(Schedulers.io())
        .observeOn(Schedulers.computation())
        .onErrorResumeNext(Observable.empty<T>())
        .doOnError { it.printStackTrace() }

fun <T> Observable<T>.subscribeAsyncBackground(cls: (onNext: T) -> Unit = {}): Subscription = this.prepareAsyncBackground().subscribe { cls(it) }

fun <T> Observable<T>.subscribeAsyncBackground(doOnError: (throwable: Throwable) -> Unit, cls: (onNext: T) -> Unit = {}): Subscription =
        this.prepareAsyncBackground().subscribe(cls, doOnError)

fun <T> Observable<T>.retryWithDelay(count: Int = -1, delayInSeconds: Long): Observable<T> =
        this.retryWhen { it.zipWith(Observable.range(1, if (count < 1) 1000000 else count), { n, i -> i }) }
                .flatMap { i -> Observable.timer(delayInSeconds, TimeUnit.SECONDS) }
                .flatMap { this }