package net.mfilm.ui.history

import android.view.MenuItem
import io.reactivex.disposables.CompositeDisposable
import io.realm.RealmResults
import net.mfilm.data.DataManager
import net.mfilm.data.db.models.MangaHistoryRealm
import net.mfilm.ui.base.realm.BaseRealmPresenter
import net.mfilm.utils.IBus
import net.mfilm.utils.MRealmDisposableObserver
import javax.inject.Inject

/**
 * Created by tusi on 6/25/17.
 */
class HistoryPresenter<V : HistoryMvpView> @Inject
constructor(val iBus: IBus, dataManager: DataManager, compositeDisposable: CompositeDisposable) :
        BaseRealmPresenter<V>(dataManager, compositeDisposable), HistoryMvpPresenter<V> {
    override fun onAttach(mvpView: V) {
        super.onAttach(mvpView)
        initIBus()
    }

    override fun initIBus() {
        val flowable = iBus.asFlowable().filter { it is MenuItem }
        val favEventEmitter = flowable.publish()
        compositeDisposable.run {
            add(favEventEmitter.subscribe { mvpView?.onReceiveOptionsMenuItem((it as MenuItem)) })
            add(favEventEmitter.connect())
        }
    }
    override fun requestHistory() {
        if (!dataManager.historyEnabled) {
            mvpView?.onHistoryEnabled(false)
            return
        }
        mvpView?.showLoading() ?: return
        val mRealmDisposableObserver = object : MRealmDisposableObserver<RealmResults<MangaHistoryRealm>>({ mvpView?.onFailure() }) {
            override fun onNext(t: RealmResults<MangaHistoryRealm>?) {
                mvpView?.run {
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

    override fun toggleHistory(mangaHistoryRealms: List<MangaHistoryRealm>) {
        mangaHistoryRealms.run {
            val newMangaFavoriteRealms = map {
                MangaHistoryRealm(it.id, it.name,
                        it.coverUrl, System.currentTimeMillis(), !it.history)
            }
            dataManager.saveObjects(newMangaFavoriteRealms)
        }
    }
}