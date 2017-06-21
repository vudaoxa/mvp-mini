package net.mfilm.utils

import io.reactivex.Flowable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.observers.DisposableObserver
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * Created by tusi on 4/11/17.
 */
class ValveUtil {
    private var valveOpened = false
    private var d: Disposable? = null
    fun toggle(d: Disposable?, on: Boolean) {
        d?.apply {
            if (isDisposed) {
                return
            }
            valveOpened = on
//            valve.onNext(on) xx
        }
    }

    fun dispose() {
        d?.dispose()
        valveOpened = false
    }

    fun startLoad(f: () -> Unit) {
        if (valveOpened) {
            return
        }
        val consumer: Consumer<Long>?
        consumer = Consumer {
            if (valveOpened) {
                //statistics
                Timber.e(it.toString() + "-----")
                f()
//                mLCPresenter.requestChannelsInfo(this@LiveCountFragment, Constants.API_KEY,
//                        Constants.PART_STATISTICS, id, null, DURATION * 1000)
            }
        }
        d = Flowable.interval(0, DURATION, TimeUnit.SECONDS)
                .subscribe(consumer)
        toggle(d, true)
    }
}

abstract class MDisposableObserver<V : Any?>(val fHttpExp: () -> Unit, val fIOExp: () -> Unit, val fOnNext: (() -> Unit)? = null) : DisposableObserver<V>() {
    override fun onComplete() {
        Timber.e("--------------onComplete------------")
    }

    override fun onNext(t: V?) {
        fOnNext?.invoke()
    }
    override fun onError(e: Throwable?) {
        when (e) {
            is HttpException -> {
                Timber.e("connect failed---------")
                fHttpExp()
            }
            is IOException -> {
                Timber.e("no internet connection ----------------")
                fIOExp()
            }
        }
    }
}

abstract class MRealmDisposableObserver<V : Any?>(val fOnNext: (() -> Unit)? = null) : DisposableObserver<V>() {
    override fun onComplete() {
        Timber.e("--------------onComplete------------")
    }

    override fun onNext(t: V?) {
        Timber.e("--------------onNext------------")
    }

    override fun onError(e: Throwable?) {
        Timber.e("--------------onError------------")
        fOnNext?.invoke()
    }
}