package net.mfilm.ui.chapter_images

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import net.mfilm.data.network_retrofit.Chapter
import net.mfilm.data.network_retrofit.ChapterImage
import net.mfilm.data.network_retrofit.ChapterImagesResponse
import net.mfilm.ui.base.stack.BaseStackFragment
import net.mfilm.utils.AppConstants
import net.mfilm.utils.DebugLog
import net.mfilm.utils.showFresco
import java.io.Serializable
import javax.inject.Inject

/**
 * Created by tusi on 5/29/17.
 */
class ChapterImagesFragment : BaseStackFragment(), ChapterImagesMvpView {
    companion object {
        fun newInstance(obj: Any?): ChapterImagesFragment {
            val fragment = ChapterImagesFragment()
            val bundle = Bundle()
            bundle.putSerializable(AppConstants.EXTRA_DATA, obj as Serializable)
            fragment.arguments = bundle
            return fragment
        }
    }

    @Inject
    lateinit var mChapterImagesPresenter: ChapterImagesMvpPresenter<ChapterImagesMvpView>
    var chapter: Chapter? = null
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(net.mfilm.R.layout.fragment_chapter_images, container, false)
    }

    override fun initFields() {
        chapter = arguments.getSerializable(AppConstants.EXTRA_DATA) as? Chapter?
        activityComponent.inject(this)
        mChapterImagesPresenter.onAttach(this)
    }

    override fun initViews() {
        chapter?.apply {
            requestChapterImages(id!!)
        }
    }

    override fun requestChapterImages(chapterId: Int) {
        mChapterImagesPresenter.requestChapterImages(chapterId)
    }

    override fun onChapterImagesResponse(chapterImagesResponse: ChapterImagesResponse?) {
        hideLoading()
        chapterImagesResponse.let { cir ->
            cir?.apply {
                cir.data.let { d ->
                    d?.apply {
                        if (d.isNotEmpty()) {
                            initChapterImages(d)
                        } else onChapterImagesNull()
                    } ?: let { onChapterImagesNull() }
                }
            } ?: let { onChapterImagesNull() }
        }
    }

    override fun onChapterImagesNull() {
        DebugLog.e("-------------------onChapterImagesNull---------------------")
    }

    override fun initChapterImages(images: List<ChapterImage>) {
        showFresco(context, images.map { it.url!! })
    }
}