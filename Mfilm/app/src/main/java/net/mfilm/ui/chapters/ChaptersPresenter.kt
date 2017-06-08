package net.mfilm.ui.chapters

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import net.mfilm.data.DataMng
import net.mfilm.data.network_retrofit.ChaptersResponse
import net.mfilm.data.network_retrofit.RetrofitService
import net.mfilm.ui.base.BasePresenter
import net.mfilm.utils.MDisposableObserver
import javax.inject.Inject

/**
 * Created by tusi on 5/27/17.
 */
class ChaptersPresenter<V : ChaptersMvpView>
@Inject constructor(val retrofitService: RetrofitService,
                    dataManager: DataMng, compositeDisposable: CompositeDisposable) :
        BasePresenter<V>(dataManager, compositeDisposable), ChaptersMvpPresenter<V> {
    override fun requestChapters(mangaId: Int, limit: Int, page: Int) {
        if (!isViewAttached) return
//        mvpView?.showLoading()
        val d = object : MDisposableObserver<ChaptersResponse>({ mvpView?.onFailure() },
                { mvpView?.onNoInternetConnections() }) {
            override fun onNext(t: ChaptersResponse?) {
                if (isViewAttached) {
                    mvpView?.onChaptersResponse(t)
                }
            }
        }
        retrofitService.mApisService.requestChapters(mangaId, limit, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(d)
        compositeDisposable.add(d)
    }
}