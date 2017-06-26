package net.mfilm.ui.history

import io.reactivex.disposables.CompositeDisposable
import io.realm.RealmResults
import net.mfilm.data.DataManager
import net.mfilm.data.db.models.MangaHistoryRealm
import net.mfilm.ui.base.realm.BaseRealmPresenter
import net.mfilm.utils.MRealmDisposableObserver
import javax.inject.Inject

/**
 * Created by tusi on 6/25/17.
 */
class HistoryPresenter<V : HistoryMvpView> @Inject
constructor(dataManager: DataManager, compositeDisposable: CompositeDisposable) :
        BaseRealmPresenter<V>(dataManager, compositeDisposable), HistoryMvpPresenter<V> {
    override fun requestHistory() {
        mvpView?.showLoading() ?: return
        val mRealmDisposableObserver = object : MRealmDisposableObserver<RealmResults<MangaHistoryRealm>>({ mvpView?.onFailure() }) {
            override fun onNext(t: RealmResults<MangaHistoryRealm>?) {
                mvpView?.apply {
                    onHistoryResponse(t)
                    t?.addChangeListener { t, _ ->
                        onHistoryResponse(t)
                    }
                }
            }
        }

        val disposable = dataManager.loadHistory(mRealmDisposableObserver)
        compositeDisposable.add(disposable)
        compositeDisposable.add(mRealmDisposableObserver)
    }
}