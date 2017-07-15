package net.mfilm.ui.favorites

import android.view.MenuItem
import io.reactivex.disposables.CompositeDisposable
import io.realm.RealmResults
import net.mfilm.data.DataManager
import net.mfilm.data.db.models.MangaFavoriteRealm
import net.mfilm.ui.base.realm.BaseRealmPresenter
import net.mfilm.utils.IBus
import net.mfilm.utils.MRealmDisposableObserver
import javax.inject.Inject

/**
 * Created by MRVU on 6/20/2017.
 */
class FavoritesPresenter<V : FavoritesMvpView> @Inject
constructor(val iBus: IBus, dataManager: DataManager, compositeDisposable: CompositeDisposable) :
        BaseRealmPresenter<V>(dataManager, compositeDisposable), FavoritesMvpPresenter<V> {
    override fun onAttach(mvpView: V) {
        super.onAttach(mvpView)
        initIBus()
    }

    override fun initIBus() {
        val flowable = iBus.asFlowable().filter { it is MenuItem }
        val favEventEmitter = flowable.publish()
        compositeDisposable.run {
            add(favEventEmitter.subscribe { mvpView?.onReceiveOptionsMenuItem((it as MenuItem)) })
            add(favEventEmitter.connect())
        }
    }
    override fun requestFavorites() {
        mvpView?.showLoading() ?: return
        val mRealmDisposableObserver = object : MRealmDisposableObserver<RealmResults<MangaFavoriteRealm>>({ mvpView?.onFailure() }) {
            override fun onNext(t: RealmResults<MangaFavoriteRealm>?) {
                mvpView?.run {
                    onFavoritesResponse(t)
                    t?.addChangeListener { t, _ ->
                        onFavoritesResponse(t)
                    }
                }
            }
        }
        val disposable = dataManager.loadFavorites(mRealmDisposableObserver)
        compositeDisposable.add(disposable)
        compositeDisposable.add(mRealmDisposableObserver)
    }

    //with condition non-empty list, all have same fav value
    override fun toggleFav(mangaFavoriteRealms: List<MangaFavoriteRealm>) {
        mangaFavoriteRealms.run {
            val newMangaFavoriteRealms = map { MangaFavoriteRealm(it.id, it.name, it.coverUrl, System.currentTimeMillis(), !it.favorite) }
            dataManager.saveObjects(newMangaFavoriteRealms)
        }
    }
}