package net.mfilm.ui.chapter_images

import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.piasy.biv.BigImageViewer
import com.lsjwzh.widget.recyclerviewpager.RecyclerViewPager
import kotlinx.android.synthetic.main.fragment_chapter_images.*
import net.mfilm.data.network_retrofit.ChapterImage
import net.mfilm.data.network_retrofit.ChapterImagesResponse
import net.mfilm.ui.base.rv.wrappers.LinearLayoutManagerWrapper
import net.mfilm.ui.base.stack.BaseStackFragment
import net.mfilm.ui.chapter_images.rv.ChapterImagesRvAdapter
import net.mfilm.ui.chapters.ChaptersMvpView
import net.mfilm.utils.tryOrExit
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by tusi on 5/29/17.
 */
class ChapterImagesFragment(private var mChaptersFragment: ChaptersMvpView? = null) : BaseStackFragment(), ChapterImagesMvpView {
    companion object {
        fun newInstance(mChaptersFragment: Any?): ChapterImagesFragment {
            val fragment = ChapterImagesFragment(mChaptersFragment as? ChaptersMvpView?)
            return fragment
        }
    }

    @Inject
    lateinit var mChapterImagesPresenter: ChapterImagesMvpPresenter<ChapterImagesMvpView>
    private var mChapterImagesRvAdapter: ChapterImagesRvAdapter<ChapterImage>? = null
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(net.mfilm.R.layout.fragment_chapter_images, container, false)
    }

    override fun initFields() {
        fullScreen = true
        tryOrExit {
            activityComponent?.inject(this)
            mChapterImagesPresenter.onAttach(this)
        }
    }

    override fun initViews() {
        initRv()
        mChaptersFragment?.run {
            currentReadingChapter.let { c ->
                c?.id?.run {
                    Timber.e("----------initViews---------$c---------")
                    requestChapterImages(this)
                }
            }
        }
    }

    override fun initRv() {
        rvPager.run {
            layoutManager = LinearLayoutManagerWrapper(context, LinearLayoutManager.HORIZONTAL, false)
            addOnPageChangedListener(mPageChangedListener)
        }
    }

    private val mPageChangedListener = RecyclerViewPager.OnPageChangedListener { p0, p1 ->
        Timber.e("-----OnPageChangedListener--------------- $p0------ $p1---------------")
    }
    override fun requestChapterImages(chapterId: Int) {
        mChapterImagesPresenter.requestChapterImages(chapterId)
    }

    override fun onChapterImagesResponse(chapterImagesResponse: ChapterImagesResponse?) {
        hideLoading()
        chapterImagesResponse.let { cir ->
            cir?.run {
                cir.data.let { d ->
                    d?.run {
                        val f = d.filter { it.isImage() }
                        if (d.isNotEmpty()) {
                            buildChapterImages(d)
                        } else onChapterImagesNull()
                    } ?: let { onChapterImagesNull() }
                }
            } ?: let { onChapterImagesNull() }
        }
    }

    override fun onChapterImagesNull() {
        Timber.e("-------------------onChapterImagesNull---------------------")
    }

    override fun buildChapterImages(images: List<ChapterImage>) {
//        context?.run {
//            mChapterImagesPresenter.showFresco(this, mChaptersFragment?.currentReadingChapter,
//                    images.map { it.url!! }.toMutableList(), images.size - 2)
//        }

        BigImageViewer.prefetch(*(images.map { Uri.parse(it.url) }.toTypedArray()))
        mChapterImagesRvAdapter?.run {
            clear()
            addAll(images)
            notifyDataSetChanged()
        } ?: let {
            mChapterImagesRvAdapter = ChapterImagesRvAdapter(context, images.toMutableList(), this)
            rvPager.adapter = mChapterImagesRvAdapter
        }
    }

    override fun loadMoreOnDemand() {
        mChaptersFragment?.run {
            loadMoreOnDemand(this@ChapterImagesFragment)
        }
    }

    override fun loadPrevOnDemand() {
        //it's must be synchronized with mChaptersFragment, because:
        //read btn behavior
        //get currentReadingPosition from mChaptersFragment, to reload images in $chapters
        mChaptersFragment?.run {
            loadPrevOnDemand(this@ChapterImagesFragment)
        }
    }

    override fun seekNextChapter() {
        initViews()
    }

    override fun seekPrevChapter() {
        initViews()
    }

    override fun onClick(position: Int, event: Int) {
        Timber.e("---------------------onClick--------------------$position")
    }
}