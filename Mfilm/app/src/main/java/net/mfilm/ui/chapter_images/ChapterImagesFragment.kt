package net.mfilm.ui.chapter_images

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import net.mfilm.data.network_retrofit.Chapter
import net.mfilm.data.network_retrofit.ChapterImage
import net.mfilm.data.network_retrofit.ChapterImagesResponse
import net.mfilm.ui.base.stack.BaseStackFragment
import net.mfilm.ui.chapters.ChaptersMvpView
import net.mfilm.utils.DebugLog
import javax.inject.Inject

/**
 * Created by tusi on 5/29/17.
 */
class ChapterImagesFragment(private var mChaptersFragment: ChaptersMvpView? = null) : BaseStackFragment(), ChapterImagesMvpView {
    companion object {
        fun newInstance(mChaptersFragment: Any?): ChapterImagesFragment {
            val fragment = ChapterImagesFragment(mChaptersFragment as? ChaptersMvpView?)
//            val bundle = Bundle()
//            bundle.putSerializable(AppConstants.EXTRA_DATA, obj as Serializable)
//            fragment.arguments = bundle
            return fragment
        }
    }

    @Inject
    lateinit var mChapterImagesPresenter: ChapterImagesMvpPresenter<ChapterImagesMvpView>
    //    private var mChaptersFragment: ChaptersFragment? = null

    //    var chapter: Chapter? = null
    override val prevChapter: Chapter?
        get() = mChaptersFragment?.prevChapter
    override val nextChapter: Chapter?
        get() = mChaptersFragment?.nextChapter
    private var mChapters = mutableListOf<Chapter>()
    override var chapters: MutableList<Chapter>
        get() = mChapters
        set(value) {
            mChapters = value
        }

//    override fun addChapter(chapter: Chapter) {
//        chapters.add(chapter)
//    }

    override fun addChapter(chapter: Chapter, f: () -> Unit) {

        chapters.apply {
            fun doIt() {
                add(chapter)
                f()
            }
            if (isEmpty()) {
                doIt()
            } else {
                if (last().id != chapter.id) {
                    DebugLog.e("--------------next chapter ----- $chapter")
                    doIt()
                } else {
                    DebugLog.e("--------------same--------------")
                    loadMoreOnDemand()
                }
            }

        }
    }
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(net.mfilm.R.layout.fragment_chapter_images, container, false)
    }

    override fun initFields() {
//        chapter = arguments.getSerializable(AppConstants.EXTRA_DATA) as? Chapter?
//        mChaptersFragment = arguments.getSerializable(AppConstants.EXTRA_DATA) as? ChaptersFragment?
        activityComponent.inject(this)
        mChapterImagesPresenter.onAttach(this)
    }

    override fun initViews() {
        mChaptersFragment?.apply {
            currentReadingChapter.let { c ->
                c?.apply {
                    DebugLog.e("----------initViews---------$c---------")
                    addChapter(c, { requestChapterImages(c.id!!) })
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
        context?.apply {
            mChapterImagesPresenter.showFresco(this, chapters.last(), images.map { it.url!! }.toMutableList(), images.size - 2)
        }
    }

    override fun loadMoreOnDemand() {
        mChaptersFragment?.apply {
            loadMoreOnDemand(this@ChapterImagesFragment)
        }
    }

    override fun nextChapter() {
        initViews()
    }

    //it's called from ChaptersFragment
    override fun onChaptersResponse() {
        DebugLog.e("----------------onChaptersResponse--------------")
        initViews()
    }
}