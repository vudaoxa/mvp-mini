package net.mfilm.ui.chapter_images

import android.content.Context
import com.stfalcon.frescoimageviewer.ImageViewer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import net.mfilm.R
import net.mfilm.data.DataMng
import net.mfilm.data.network_retrofit.ChapterImagesResponse
import net.mfilm.data.network_retrofit.RetrofitService
import net.mfilm.ui.base.BasePresenter
import net.mfilm.ui.chapter_images.views.ImageOverlayView
import net.mfilm.utils.DebugLog
import net.mfilm.utils.MDisposableObserver
import javax.inject.Inject

/**
 * Created by tusi on 5/29/17.
 */
class ChapterImagesPresenter<V : ChapterImagesMvpView>
@Inject constructor(val retrofitService: RetrofitService,
                    dataManager: DataMng, compositeDisposable: CompositeDisposable) :
        BasePresenter<V>(dataManager, compositeDisposable), ChapterImagesMvpPresenter<V> {
    override fun requestChapterImages(chapterId: Int) {
        if (!isViewAttached) return
        mvpView?.showLoading()
        val d = object : MDisposableObserver<ChapterImagesResponse>({ mvpView?.onError(R.string.error_conection) },
                { mvpView?.onError(R.string.internet_no_conection) }) {
            override fun onNext(t: ChapterImagesResponse?) {
                if (isViewAttached) {
                    mvpView?.onChapterImagesResponse(t)
                }
            }
        }
        retrofitService.mApisService.requestChapterImages(chapterId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(d)
        compositeDisposable.add(d)
    }

    override fun showFresco(context: Context, list: MutableList<String>, startPosition: Int) {
        val builder = ImageViewer.Builder(context, list)
        val overlayView = ImageOverlayView(context)
        builder.apply {
            setStartPosition(startPosition)
            setImageChangeListener { position ->
                val text = "${position} / ${list.size}"
                DebugLog.e("--------------OnImageChangeListener-------------$text")
                if (list.size - position == 3) {
                    DebugLog.e("---------load more-----------")
                }
                overlayView.setShareText(list[position])
                overlayView.setDescription(text)
            }
            setOverlayView(overlayView)
            show()
        }
    }

    override fun loadMore() {

    }
}