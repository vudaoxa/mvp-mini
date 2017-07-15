package net.mfilm.ui.mangas

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import net.mfilm.data.DataManager
import net.mfilm.data.db.models.MangaFavoriteRealm
import net.mfilm.data.network_retrofit.Manga
import net.mfilm.data.network_retrofit.MangasResponse
import net.mfilm.data.network_retrofit.RetrofitService
import net.mfilm.ui.base.BasePresenter
import net.mfilm.utils.MDisposableObserver
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Dieu on 09/03/2017.
 */

class MangasPresenter<V : MangasMvpView> @Inject
constructor(val retrofitService: RetrofitService,
            dataManager: DataManager, compositeDisposable: CompositeDisposable) :
        BasePresenter<V>(dataManager, compositeDisposable), MangasMvpPresenter<V> {

    override fun requestMangas(category: Int?, limit: Int, page: Int
                               , sort: String, search: String?) {
        mvpView?.showLoading() ?: return
        val d = object : MDisposableObserver<MangasResponse>({ mvpView?.onFailure() },
                { mvpView?.onNoInternetConnections() }) {
            override fun onNext(t: MangasResponse?) {
                    mvpView?.onMangasResponse(t)
            }
        }
        obtainHl(page)
        retrofitService.mApisService.requestMangas(category, limit, page, sort, search, hl)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(d)
        compositeDisposable.add(d)
    }

    override fun toggleFav(manga: Manga): Boolean {
        manga.run {
            val mangaFavRealm = dataManager.isFavorite(id!!)
            val x = mangaFavRealm?.favorite
            Timber.e("----------toggleFav----------$x---------------")
            var fav = false
            x?.run {
                if (this) {
                    fav = true
                    mvpView?.onToggleFavResponse(true)
                    return false
                }
            }
            val newFavRealm = MangaFavoriteRealm(id, name, coverUrl, System.currentTimeMillis(), !fav)
            dataManager.saveObject(newFavRealm)
            mvpView?.onToggleFavResponse(true)
        }
        return false
    }
}
