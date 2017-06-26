package net.mfilm.ui.favorites

import io.reactivex.disposables.CompositeDisposable
import io.realm.RealmResults
import net.mfilm.data.DataManager
import net.mfilm.data.db.models.MangaFavoriteRealm
import net.mfilm.ui.base.realm.BaseRealmPresenter
import net.mfilm.utils.MRealmDisposableObserver
import javax.inject.Inject

/**
 * Created by MRVU on 6/20/2017.
 */
class FavoritesPresenter<V : FavoritesMvpView> @Inject
constructor(dataManager: DataManager, compositeDisposable: CompositeDisposable) :
        BaseRealmPresenter<V>(dataManager, compositeDisposable), FavoritesMvpPresenter<V> {
    override fun requestFavorites() {
        mvpView?.showLoading() ?: return
        val mRealmDisposableObserver = object : MRealmDisposableObserver<RealmResults<MangaFavoriteRealm>>({ mvpView?.onFailure() }) {
            override fun onNext(t: RealmResults<MangaFavoriteRealm>?) {
                mvpView?.apply {
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
}