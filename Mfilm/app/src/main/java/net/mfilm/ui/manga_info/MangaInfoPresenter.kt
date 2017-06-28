package net.mfilm.ui.manga_info

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import net.mfilm.data.DataManager
import net.mfilm.data.db.models.MangaFavoriteRealm
import net.mfilm.data.db.models.MangaHistoryRealm
import net.mfilm.data.network_retrofit.Manga
import net.mfilm.data.network_retrofit.MangaDetailResponse
import net.mfilm.data.network_retrofit.RetrofitService
import net.mfilm.ui.base.realm.BaseRealmPresenter
import net.mfilm.utils.*
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by MRVU on 6/23/2017.
 */
class MangaInfoPresenter<V : MangaInfoMvpView> @Inject
constructor(val retrofitService: RetrofitService, val iBus: IBus,
            dataManager: DataManager, compositeDisposable: CompositeDisposable) :
        BaseRealmPresenter<V>(dataManager, compositeDisposable), MangaInfoMvpPresenter<V> {
    override fun onAttach(mvpView: V) {
        super.onAttach(mvpView)
        initIBus()
    }

    override fun requestManga(id: Int) {
        mvpView?.showLoading() ?: return
        val d = object : MDisposableObserver<MangaDetailResponse>({ mvpView?.onFailure() },
                { mvpView?.onNoInternetConnections() }) {
            override fun onNext(t: MangaDetailResponse?) {
                mvpView?.onMangaDetailResponse(t)
            }
        }
        retrofitService.mApisService.requestMangaDetail(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(d)
        compositeDisposable.add(d)
    }

    override fun initIBus() {
        mvpView ?: return
        //flowable to receive TapEvent from MainPresenter
        val flowable = iBus.asFlowable().filter { it is TapEvent }
        val tapEventEmitter = flowable.publish()
        val tapCountConsumer = tapEventEmitter.publish { it.buffer(it.debounce(500, TimeUnit.MILLISECONDS)) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    onTaps(it.size)
                }
        compositeDisposable.apply {
            add(tapCountConsumer)
            add(tapEventEmitter.connect())
        }
    }

    override fun isFavorite(id: Int) {
        val x = dataManager.isFavorite(id)?.fav
        Timber.e("-----isFavorite($id)---------$x----------------")
        iBus.send(Favorite(x))
    }

    override fun onTaps(tapCount: Int) {
        Timber.e("-----onTaps---------------------tapCount---- ${tapCount} --------------------")
        if (tapCount.isEven()) return
        mvpView?.toggleFav()
    }

    override fun toggleFav(manga: Manga): Boolean {
        manga.apply {
            val mangaFavRealm = dataManager.isFavorite(id!!)
            val x = mangaFavRealm?.fav
            Timber.e("----------toggleFav----------$x---------------")
            var fav = false
            x?.apply {
                if (this) fav = true
            }
            val newFavRealm = MangaFavoriteRealm(id, name, coverUrl, System.currentTimeMillis(), !fav)
            dataManager.saveObject(newFavRealm)
            iBus.send(Favorite(!fav))
        }
        return false
    }

    override fun saveHistory(manga: Manga) {
        manga.apply {
            val newHistoryRealm = MangaHistoryRealm(id, name, coverUrl, System.currentTimeMillis(), true)
            dataManager.saveObject(newHistoryRealm)
        }
    }
}