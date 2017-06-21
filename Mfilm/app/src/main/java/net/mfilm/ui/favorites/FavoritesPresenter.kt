package net.mfilm.ui.favorites

import io.reactivex.disposables.CompositeDisposable
import net.mfilm.data.DataManager
import net.mfilm.ui.base.BasePresenter
import javax.inject.Inject

/**
 * Created by MRVU on 6/20/2017.
 */
class FavoritesPresenter<V : FavoritesMvpView> @Inject
constructor(dataManager: DataManager, compositeDisposable: CompositeDisposable) :
        BasePresenter<V>(dataManager, compositeDisposable), FavoritesMvpPresenter<V> {
    override fun requestFavorites() {
        dataManager.loadFavorites()
    }
}