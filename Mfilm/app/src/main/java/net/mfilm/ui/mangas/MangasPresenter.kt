package net.mfilm.ui.mangas

import net.mfilm.data.network_retrofit.MangasResponse

/**
 * Created by Dieu on 09/03/2017.
 */

class MangasPresenter<V : MangasMVPView> @javax.inject.Inject
constructor(val retrofitService: net.mfilm.data.network_retrofit.RetrofitService,
            dataManager: net.mfilm.data.DataMng, compositeDisposable: io.reactivex.disposables.CompositeDisposable) :
        net.mfilm.ui.base.BasePresenter<V>(dataManager, compositeDisposable), MangasMvpPresenter<V> {
    override fun requestMangas(category: Int?, limit: Int, page: Int
                               , sort: String, search: String?) {
        if (!isViewAttached) return
//        mvpView?.showLoading()
        val d = object : com.icom.xsvietlott.utils.MDisposableObserver<MangasResponse>({ mvpView?.onError(net.mfilm.R.string.error_conection) },
                { mvpView?.onError(net.mfilm.R.string.internet_no_conection) }) {
            override fun onNext(t: net.mfilm.data.network_retrofit.MangasResponse?) {
                if (isViewAttached) {
                    mvpView?.onMangasResponse(t)
                }
            }
        }
        retrofitService.mApisService.requestMangas(category, limit, page, sort, search)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
                .subscribe(d)
        compositeDisposable.add(d)
    }
}
