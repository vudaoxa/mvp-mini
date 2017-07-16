package net.mfilm.ui.chapters

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.realm.RealmList
import net.mfilm.data.DataManager
import net.mfilm.data.db.models.ChapterRealm
import net.mfilm.data.network_retrofit.ChaptersResponse
import net.mfilm.data.network_retrofit.Manga
import net.mfilm.data.network_retrofit.RetrofitService
import net.mfilm.ui.base.BasePresenter
import net.mfilm.utils.MDisposableObserver
import net.mfilm.utils.MRealmDisposableObserver
import javax.inject.Inject

/**
 * Created by tusi on 5/27/17.
 */
class ChaptersPresenter<V : ChaptersMvpView>
@Inject constructor(val retrofitService: RetrofitService,
                    dataManager: DataManager, compositeDisposable: CompositeDisposable) :
        BasePresenter<V>(dataManager, compositeDisposable), ChaptersMvpPresenter<V> {
    override fun requestChapters(mangaId: Int, limit: Int, page: Int) {
        mvpView?.showLoading() ?: return
        val d = object : MDisposableObserver<ChaptersResponse>({ mvpView?.onFailure() },
                { mvpView?.onNoInternetConnections() }) {
            override fun onNext(t: ChaptersResponse?) {
                mvpView?.onChaptersResponse(t)
            }
        }
        retrofitService.mApisService.requestChapters(mangaId, limit, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(d)
        compositeDisposable.add(d)
    }

    override fun saveMangaHistory(manga: Manga) {
        manga.run {
            //            val newHistoryRealm = MangaHistoryRealm(id, name, coverUrl, System.currentTimeMillis(), true)
            dataManager.saveMangaHistory(manga)
        }
    }

    override fun requestMangaHistory(id: Int) {
        val x = dataManager.requestMangaHistory(id)
        x?.run {
            mvpView?.onMangaHistoryResponse(this)
        }
    }

    override fun requestChaptersHistory(id: Int) {
        val mRealmDisposableObserver = object : MRealmDisposableObserver<RealmList<ChapterRealm>>() {
            override fun onNext(t: RealmList<ChapterRealm>?) {
                mvpView?.run {
                    onChaptersHistoryResponse(t)
                    t?.addChangeListener { t, _ ->
                        onChaptersHistoryResponse(t)
                    }
                }
            }
        }
        dataManager.requestChaptersHistory(id, mRealmDisposableObserver)?.run {
            compositeDisposable.add(this)
        }
        compositeDisposable.add(mRealmDisposableObserver)
    }
}