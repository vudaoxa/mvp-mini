package net.mfilm.ui.chapter_images

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import net.mfilm.data.DataManager
import net.mfilm.data.network_retrofit.Chapter
import net.mfilm.data.network_retrofit.ChapterDetailResponse
import net.mfilm.data.network_retrofit.ChapterImagesResponse
import net.mfilm.data.network_retrofit.RetrofitService
import net.mfilm.ui.base.BasePresenter
import net.mfilm.utils.MDisposableObserver
import timber.log.Timber
import java.net.URL
import javax.inject.Inject


/**
 * Created by tusi on 5/29/17.
 */
class ChapterImagesPresenter<V : ChapterImagesMvpView>
@Inject constructor(val retrofitService: RetrofitService,
                    dataManager: DataManager, compositeDisposable: CompositeDisposable) :
        BasePresenter<V>(dataManager, compositeDisposable), ChapterImagesMvpPresenter<V> {
    override fun requestChapterImages(chapterId: Int) {
        mvpView?.showLoading() ?: return
        val d = object : MDisposableObserver<ChapterImagesResponse>({ mvpView?.onFailure() },
                { mvpView?.onNoInternetConnections() }) {
            override fun onNext(t: ChapterImagesResponse?) {
                mvpView?.onChapterImagesResponse(t)
            }
        }
        retrofitService.mApisService.requestChapterImages(chapterId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(d)
        compositeDisposable.add(d)
    }

    override fun saveChapterHistory(chapter: Chapter) {
        dataManager.saveChapterHistory(chapter)
    }

    override fun saveReadingPage(chapter: Chapter, page: Int) {
        dataManager.saveReadingPage(chapter, page)
    }
    override fun loadMore() {
        mvpView?.loadMoreOnDemand()
    }

    override fun loadPrev() {
        mvpView?.loadPrevOnDemand()
    }

    override fun requestChapterDetail(chapterId: Int) {
        mvpView?.showLoading() ?: return
        val d = object : MDisposableObserver<ChapterDetailResponse>({ mvpView?.onFailure() },
                { mvpView?.onNoInternetConnections() }) {
            override fun onNext(t: ChapterDetailResponse?) {
                mvpView?.onChapterDetailResponse(t)
            }
        }
        retrofitService.mApisService.requestChapterDetail(chapterId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(d)
        compositeDisposable.add(d)
    }
    override fun requestBitmapSize(url: String?) {
        url ?: return
        mvpView ?: return

        val d = object : MDisposableObserver<Bitmap>({}, {}) {
            override fun onNext(t: Bitmap?) {
                t?.run {
                    Timber.e("----requestBitmapSize-------$url------ $width-----$height-------------------")
                    mvpView?.onBitmapSizeResponse(height / width >= 2.5)
                }
            }
        }
        try {
            Observable.fromCallable { BitmapFactory.decodeStream(URL(url).openStream()) }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(d)
        } catch (e: Exception) {
        }
//        tryIt({
//            Observable.fromCallable { BitmapFactory.decodeStream(URL(url).openStream()) }
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(d)
//        })
        compositeDisposable.add(d)
    }
}