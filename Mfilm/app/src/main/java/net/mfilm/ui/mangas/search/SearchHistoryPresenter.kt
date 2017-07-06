package net.mfilm.ui.mangas.search

import io.reactivex.disposables.CompositeDisposable
import io.realm.RealmResults
import net.mfilm.data.DataManager
import net.mfilm.data.db.models.SearchQueryRealm
import net.mfilm.ui.base.realm.BaseRealmPresenter
import net.mfilm.ui.search_history.SearchHistoryMvpView
import net.mfilm.utils.MRealmDisposableObserver
import javax.inject.Inject

/**
 * Created by tusi on 6/25/17.
 */
class SearchHistoryPresenter<V : SearchHistoryMvpView> @Inject
constructor(dataManager: DataManager, compositeDisposable: CompositeDisposable) :
        BaseRealmPresenter<V>(dataManager, compositeDisposable), SearchHistoryMvpPresenter<V> {
    override fun saveQuery(query: String) {
        dataManager.saveObject(SearchQueryRealm(query, System.currentTimeMillis(), true))
    }

    override fun requestSearchHistory() {
        mvpView?.showLoading() ?: return
        val mRealmDisposableObserver = object : MRealmDisposableObserver<RealmResults<SearchQueryRealm>>({ mvpView?.onFailure() }) {
            override fun onNext(t: RealmResults<SearchQueryRealm>?) {
                mvpView?.run {
                    onSearchHistoryResponse(t)
                    t?.addChangeListener { t, _ ->
                        onSearchHistoryResponse(t)
                    }
                }
            }
        }
        val disposable = dataManager.loadSearchHistory(mRealmDisposableObserver)
        compositeDisposable.add(disposable)
        compositeDisposable.add(mRealmDisposableObserver)
    }

    override fun toggleSearchHistory(searchQueryRealms: List<SearchQueryRealm>) {
        searchQueryRealms.run {
            val newSearchQueryRealms = map { SearchQueryRealm(it.query, it.time, !it.status) }
            dataManager.saveObjects(newSearchQueryRealms)
        }
    }
}