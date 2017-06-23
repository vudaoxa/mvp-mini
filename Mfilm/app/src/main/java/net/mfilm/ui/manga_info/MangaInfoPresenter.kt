package net.mfilm.ui.manga_info

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import net.mfilm.data.DataManager
import net.mfilm.data.network_retrofit.Manga
import net.mfilm.ui.base.BasePresenter
import net.mfilm.utils.IBus
import net.mfilm.utils.isEven
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by MRVU on 6/23/2017.
 */
class MangaInfoPresenter<V : MangaInfoMvpView> @Inject
constructor(val iBus: IBus,
            dataManager: DataManager, compositeDisposable: CompositeDisposable) :
        BasePresenter<V>(dataManager, compositeDisposable), MangaInfoMvpPresenter<V> {
    override fun initIBus() {
        if (!isViewAttached) return
        val tapEventEmitter = iBus.asFlowable().publish()
        val tapCountConsumer = tapEventEmitter.publish { it.buffer(it.debounce(1, TimeUnit.SECONDS)) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    onTaps(it.size)
                }
        compositeDisposable.apply {
            add(tapCountConsumer)
            add(tapEventEmitter.connect())
        }
    }

    override fun onTaps(tapCount: Int) {
        if (tapCount.isEven()) return
        mvpView?.toggleFav()
    }

    override fun toggleFav(manga: Manga): Boolean {
        manga.apply {
            if ()
        }
    }
}