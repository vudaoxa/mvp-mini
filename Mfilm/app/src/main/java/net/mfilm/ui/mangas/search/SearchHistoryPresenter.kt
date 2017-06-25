package net.mfilm.ui.mangas.search

import io.reactivex.disposables.CompositeDisposable
import io.realm.RealmResults
import net.mfilm.data.DataManager
import net.mfilm.data.db.models.SearchQueryRealm
import net.mfilm.ui.base.realm.BaseRealmPresenter
import net.mfilm.ui.mangas.MangasMvpView
import net.mfilm.utils.MRealmDisposableObserver
import javax.inject.Inject

/**
 * Created by tusi on 6/25/17.
 */
class SearchHistoryPresenter<V : MangasMvpView> @Inject
constructor(dataManager: DataManager, compositeDisposable: CompositeDisposable) :
        BaseRealmPresenter<V>(dataManager, compositeDisposable), SearchHistoryMvpPresenter<V> {
    override fun saveQuery(query: String) {
        dataManager.saveObject(SearchQueryRealm(query, System.currentTimeMillis()))
    }

    override fun requestSearchHistory() {
        if (!isViewAttached) return
        mvpView?.showLoading()
        val mRealmDisposableObserver = object : MRealmDisposableObserver<RealmResults<SearchQueryRealm>>({ mvpView?.onFailure() }) {
            override fun onNext(t: RealmResults<SearchQueryRealm>?) {
                if (isViewAttached) {
                    mvpView?.onSearchHistoryResponse(t)
                    t?.addChangeListener { t, _ ->
                        mvpView?.onSearchHistoryResponse(t)
                    }
                }
            }
        }

        val disposable = dataManager.loadSearchHistory(mRealmDisposableObserver)
        compositeDisposable.add(disposable)
        compositeDisposable.add(mRealmDisposableObserver)
    }
}