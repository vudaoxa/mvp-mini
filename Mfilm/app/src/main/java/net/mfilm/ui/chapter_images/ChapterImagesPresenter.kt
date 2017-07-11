package net.mfilm.ui.chapter_images

import android.content.Context
import com.stfalcon.frescoimageviewer.ImageViewer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import net.mfilm.data.DataManager
import net.mfilm.data.network_retrofit.Chapter
import net.mfilm.data.network_retrofit.ChapterImagesResponse
import net.mfilm.data.network_retrofit.RetrofitService
import net.mfilm.ui.base.BasePresenter
import net.mfilm.utils.MDisposableObserver
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

    private var imageViewer: ImageViewer? = null
    //    how to add more images to the tail of imageviewer
//    override fun showFresco(context: Context, chapter: Chapter?, list: MutableList<String>, startPosition: Int) {
//        imageViewer?.onDismiss()
//        val builder = ImageViewer.Builder(context, list)
//        val overlayView = ImageOverlayView(context)
//        val hierarchyBuilder = GenericDraweeHierarchyBuilder.newInstance(context.resources)
//                .setFailureImage(R.drawable.ic_default_grey)
//                .setProgressBarImage(R.drawable.ic_loading_image)
//                .setPlaceholderImage(R.drawable.ic_default_grey)
//
//        builder.run {
//            setCustomDraweeHierarchyBuilder(hierarchyBuilder)
//            setStartPosition(startPosition)
//            setImageChangeListener { position ->
//                val text = "${chapter?.name} ${position + 1} / ${list.size}"
//                Timber.e("--------------OnImageChangeListener-------------$text")
//                if (position == list.size - 1) {
//                    Timber.e("---------load next chapter-----------")
//                    loadMore()
//                }
//                if (position == 0) {
//                    Timber.e("-----------load prev chapter-------")
//                    loadPrev()
//                }
//                overlayView.setShareText(list[position])
//                overlayView.setDescription(text)
//            }
//            setOverlayView(overlayView)
//            setOnDismissListener {
//                Timber.e("---------------onDismiss------------------")
//            }
//            imageViewer = show()
//        }
//    }

    override fun showFresco(context: Context, chapter: Chapter?, list: MutableList<String>, startPosition: Int) {

    }
    override fun loadMore() {
        mvpView?.loadMoreOnDemand()
    }

    override fun loadPrev() {
        mvpView?.loadPrevOnDemand()
    }
}