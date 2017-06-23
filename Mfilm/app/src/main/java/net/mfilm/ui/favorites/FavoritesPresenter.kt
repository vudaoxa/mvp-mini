package net.mfilm.ui.favorites

import io.reactivex.disposables.CompositeDisposable
import io.realm.RealmResults
import net.mfilm.data.DataManager
import net.mfilm.data.db.models.MangaRealm
import net.mfilm.ui.base.realm.BaseRealmPresenter
import net.mfilm.utils.MRealmDisposableObserver
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by MRVU on 6/20/2017.
 */
class FavoritesPresenter<V : FavoritesMvpView> @Inject
constructor(dataManager: DataManager, compositeDisposable: CompositeDisposable) :
        BaseRealmPresenter<V>(dataManager, compositeDisposable), FavoritesMvpPresenter<V> {
    override fun requestFavorites() {
        if (!isViewAttached) return
        mvpView?.showLoading()
        val mRealmDisposableObserver = object : MRealmDisposableObserver<RealmResults<MangaRealm>>({ mvpView?.onFailure() }) {
            override fun onNext(t: RealmResults<MangaRealm>?) {
                if (isViewAttached) {
                    mvpView?.onFavoritesResponse(t)
                    t?.addChangeListener { t, changeSet ->
                        changeSet.apply {
                            Timber.e("--changeSet--------------${changeRanges}----------${xx}--------------")
                        }
                        mvpView?.onFavoritesResponse(t)

                    }
                }
            }
        }

        val disposable = dataManager.loadFavorites(mRealmDisposableObserver)
        compositeDisposable.add(disposable)
        compositeDisposable.add(mRealmDisposableObserver)
    }
}