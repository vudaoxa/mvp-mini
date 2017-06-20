package net.mfilm.ui.favorites

import io.reactivex.disposables.CompositeDisposable
import net.mfilm.data.DataMng
import net.mfilm.data.network_retrofit.RetrofitService
import net.mfilm.ui.base.BasePresenter
import javax.inject.Inject

/**
 * Created by MRVU on 6/20/2017.
 */
class FavoritesPresenter<V : FavoritesMvpView> @Inject
constructor(val retrofitService: RetrofitService,
            dataManager: DataMng, compositeDisposable: CompositeDisposable) :
        BasePresenter<V>(dataManager, compositeDisposable), FavoritesMvpPresenter<V> {
    override fun requestFavorites() {

    }
}