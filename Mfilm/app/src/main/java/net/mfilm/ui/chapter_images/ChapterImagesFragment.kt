package net.mfilm.ui.chapter_images

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import net.mfilm.data.network_retrofit.Chapter
import net.mfilm.data.network_retrofit.ChapterImage
import net.mfilm.data.network_retrofit.ChapterImagesResponse
import net.mfilm.ui.base.stack.BaseStackFragment
import net.mfilm.ui.chapters.ChaptersFragment
import net.mfilm.utils.AppConstants
import net.mfilm.utils.DebugLog
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
    private var mChaptersFragment: ChaptersFragment? = null
    //    private var mChaptersMvpView: ChaptersMvpView? = null
    //    var chapter: Chapter? = null
    override val prevChapter: Chapter?
        get() = mChaptersFragment?.prevChapter
    override val nextChapter: Chapter?
        get() = mChaptersFragment?.nextChapter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(net.mfilm.R.layout.fragment_chapter_images, container, false)
    }

    override fun initFields() {
//        chapter = arguments.getSerializable(AppConstants.EXTRA_DATA) as? Chapter?
        mChaptersFragment = arguments.getSerializable(AppConstants.EXTRA_DATA) as? ChaptersFragment?
        activityComponent.inject(this)
        mChapterImagesPresenter.onAttach(this)
    }

    override fun initViews() {
        mChaptersFragment?.apply {
            currentReadingChapter.let { c ->
                c?.apply {
                    requestChapterImages(c.id!!)
                }
            }
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
        mChapterImagesPresenter.showFresco(context, images.map { it.url!! }.toMutableList())
    }
}