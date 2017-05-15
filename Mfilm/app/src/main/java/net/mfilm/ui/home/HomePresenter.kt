package net.mfilm.ui.home

import com.icom.xsvietlott.utils.MDisposableObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import net.mfilm.R
import net.mfilm.data.DataMng
import net.mfilm.data.network_retrofit.MangasResponse
import net.mfilm.data.network_retrofit.RetrofitService
import net.mfilm.ui.base.BasePresenter
import javax.inject.Inject

/**
 * Created by Dieu on 09/03/2017.
 */

class HomePresenter<V : HomeMVPView> @Inject
constructor(val retrofitService: RetrofitService,
            dataManager: DataMng, compositeDisposable: CompositeDisposable) :
        BasePresenter<V>(dataManager, compositeDisposable), HomeMvpPresenter<V> {
    override fun requestMangas(category: Int?, limit: Int, page: Int
                               , sort: String, search: String?) {
        if (!isViewAttached) return
        mvpView?.showLoading()
        val d = object : MDisposableObserver<MangasResponse>({ mvpView?.onError(R.string.error_conection) },
                { mvpView?.onError(R.string.internet_no_conection) }) {
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
