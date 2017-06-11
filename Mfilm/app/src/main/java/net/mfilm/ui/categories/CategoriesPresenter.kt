package net.mfilm.ui.categories

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import net.mfilm.data.DataMng
import net.mfilm.data.network_retrofit.CategoriesResponse
import net.mfilm.data.network_retrofit.RetrofitService
import net.mfilm.ui.base.BasePresenter
import net.mfilm.utils.MDisposableObserver
import javax.inject.Inject

/**
 * Created by tusi on 6/12/17.
 */
class CategoriesPresenter<V : CategoriesMvpView> @Inject
constructor(val retrofitService: RetrofitService,
            dataManager: DataMng, compositeDisposable: CompositeDisposable) :
        BasePresenter<V>(dataManager, compositeDisposable), CategoriesMvpPresenter<V> {
    override fun requestCategories() {
        if (!isViewAttached) return
        mvpView?.showLoading()
        val d = object : MDisposableObserver<CategoriesResponse>({ mvpView?.onFailure() },
                { mvpView?.onNoInternetConnections() }) {
            override fun onNext(t: CategoriesResponse?) {
                if (isViewAttached) {
                    mvpView?.onCategoriesResponse(t)
                }
            }
        }
        retrofitService.mApisService.requestCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(d)
        compositeDisposable.add(d)
    }
}