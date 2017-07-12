package net.mfilm.ui.chapter_images

import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.afollestad.materialdialogs.MaterialDialog
import com.github.piasy.biv.BigImageViewer
import com.lsjwzh.widget.recyclerviewpager.RecyclerViewPager
import kotlinx.android.synthetic.main.fragment_chapter_images.*
import kotlinx.android.synthetic.main.viewer_footer.*
import kotlinx.android.synthetic.main.viewer_header.*
import net.mfilm.R
import net.mfilm.data.network_retrofit.ChapterImage
import net.mfilm.data.network_retrofit.ChapterImagesResponse
import net.mfilm.ui.base.rv.wrappers.LinearLayoutManagerWrapper
import net.mfilm.ui.base.stack.BaseStackFragment
import net.mfilm.ui.chapter_images.rv.ChapterImagesRvAdapter
import net.mfilm.ui.chapters.ChaptersMvpView
import net.mfilm.utils.*
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
    private var mLayoutWrapper: LinearLayoutManagerWrapper? = null
    private var mCurrentPage = 0
    override var currentPage: Int
        get() = mCurrentPage
        set(value) {
            mCurrentPage = value
        }

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
        initBtnShare()
        requestChapterImages()
    }

    override fun initRv() {
        rv_pager_horizontal.run {
            mLayoutWrapper = LinearLayoutManagerWrapper(context, LinearLayoutManager.HORIZONTAL, false)
            layoutManager = mLayoutWrapper
            addOnPageChangedListener(mPageChangedListener)
        }
    }

    override fun showBtnViewContinue() {
        mLayoutWrapper?.run {
            val icon = if (!next) {
                if (orientation == LinearLayoutManager.HORIZONTAL) icon_left else icon_up
            } else {
                if (orientation == LinearLayoutManager.HORIZONTAL) icon_right else icon_down
            }
            btn_continue.show(true)
            btn_continue.setImageDrawable(icon)
        }
    }

    override fun initBtnShare() {
        btn_share.setOnClickListener {
            sendShareIntent(context, mChapterImagesRvAdapter?.mData?.getOrNull(currentPage)?.url)
        }
    }

    private var next = false
    private val mPageChangedListener = RecyclerViewPager.OnPageChangedListener { p0, p1 ->
        Timber.e("-----OnPageChangedListener--------------- $p0------ $p1---------------")
        mChapterImagesRvAdapter?.run {
            val itemCount = itemCount
            tv_page_count.text = "${p1 + 1}/$itemCount"
            when (p1) {
                0 -> {
                    next = false
                    showBtnViewContinue()
                }
                itemCount - 1 -> {
                    next = true
                    showBtnViewContinue()
                }
                else -> {
                    btn_continue.show(false)
                }
            }
        }
    }

    override fun requestChapterImages() {
        mChaptersFragment?.run {
            currentReadingChapter.let { c ->
                c?.run {
                    Timber.e("----------initViews---------$c---------")
                    requestChapterImages(c.id!!)
                    tv_chapter_name.text = c.name
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
            cir?.run {
                cir.data.let { d ->
                    d?.run {
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
        mChapterImagesPresenter.requestBitmapSize(images.getOrNull(3)?.url)
        BigImageViewer.prefetch(*(images.map { Uri.parse(it.url) }.toTypedArray()))
        mChapterImagesRvAdapter?.run {
            clear()
            addAll(images)
            notifyDataSetChanged()
        } ?: let {
            mChapterImagesRvAdapter = ChapterImagesRvAdapter(context, images.toMutableList(), this, this)
            rv_pager_horizontal.adapter = mChapterImagesRvAdapter
        }
    }

    fun webtoon() {
        mLayoutWrapper?.run {
            orientation = LinearLayoutManager.VERTICAL
            rv_pager_horizontal.requestLayout()
        }
//        rv_pager_horizontal.show(false)
//        rv_vertical.run {
//            adapter = mChapterImagesRvAdapter
//            show(true)
//        }
    }

    override fun onBitmapSizeResponse(webtoon: Boolean) {
        context ?: return
        if (webtoon) {
            DialogUtil.showMessageConfirm(context, R.string.webtoon_title, R.string.webtoon_content,
                    MaterialDialog.SingleButtonCallback { _, _ -> webtoon() })
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

    override fun onRvFailure(position: Int) {
        mChapterImagesRvAdapter?.removeAt(position)
    }

    override fun onClick(position: Int, event: Int) {
        Timber.e("---------------------onClick--------------------$position")
        viewer_header.toggleShow()
        viewer_footer.toggleShow()
    }
}