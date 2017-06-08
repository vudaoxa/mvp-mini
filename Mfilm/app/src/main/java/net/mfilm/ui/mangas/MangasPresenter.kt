package net.mfilm.ui.mangas

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import net.mfilm.data.DataMng
import net.mfilm.data.network_retrofit.MangasResponse
import net.mfilm.data.network_retrofit.RetrofitService
import net.mfilm.ui.base.BasePresenter
import net.mfilm.utils.MDisposableObserver
import javax.inject.Inject

/**
 * Created by Dieu on 09/03/2017.
 */

class MangasPresenter<V : MangasMvpView> @Inject
constructor(val retrofitService: RetrofitService,
            dataManager: DataMng, compositeDisposable: CompositeDisposable) :
        BasePresenter<V>(dataManager, compositeDisposable), MangasMvpPresenter<V> {
    override fun requestMangas(category: Int?, limit: Int, page: Int
                               , sort: String, search: String?) {
        if (!isViewAttached) return
        mvpView?.showLoading()
        val d = object : MDisposableObserver<MangasResponse>({ mvpView?.onFailure() },
                { mvpView?.onNoInternetConnections() }) {
            override fun onNext(t: MangasResponse?) {
                if (isViewAttached) {
                    mvpView?.onMangasResponse(t)
                }
            }
        }
        retrofitService.mApisService.requestMangas(category, limit, page, sort, search)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(d)
        compositeDisposable.add(d)
    }
}
