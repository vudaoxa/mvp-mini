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
        initAds()
        initBtnViewContinue()

        requestChapterImages()
    }

    override fun onDestroy() {
        super.onDestroy()
        mChapterImagesPresenter.onDetach()
    }
    override fun initRv() {
        context ?: return
        rv_pager_horizontal.run {
            mLayoutWrapper = LinearLayoutManagerWrapper(context, LinearLayoutManager.HORIZONTAL, false)
            layoutManager = mLayoutWrapper
            addOnPageChangedListener(mPageChangedListener)
        }
    }

    fun next() {
        next = true
        loadMoreOnDemand()
    }

    fun prev() {
        next = false
        loadPrevOnDemand()
    }

    override fun initBtnViewContinue() {
        btn_continue.setOnClickListener {
            Timber.e("-----------btn_continue---------------------------")
            if (next) {
                loadMoreOnDemand()
            } else loadPrevOnDemand()
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

    override fun initPageChange() {
        currentPage = 0
        mChapterImagesRvAdapter?.run {
            tv_page_count.text = "${1}/$itemCount"
        }
        btn_left.setImageDrawable(icon_left_small)
        btn_right.setImageDrawable(icon_right_small)
        btn_left.setOnClickListener { prev() }
        btn_right.setOnClickListener { next() }
    }
    private var next = false
    private val mPageChangedListener = RecyclerViewPager.OnPageChangedListener { p0, p1 ->
        Timber.e("-----OnPageChangedListener--------------- $p0------ $p1---------------")
        mChapterImagesRvAdapter?.run {
            currentPage = p1
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

    fun request() {
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

    private var countRequestChapterImages = 0
    override fun requestChapterImages() {
        btn_continue.show(false)
        if (countRequestChapterImages++ > 0) {
            mAds(null, { request() })
        } else request()
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
                        if (f.isNotEmpty()) {
                            buildChapterImages(f)
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
            if (!next && countRequestChapterImages > 0) {
                rv_pager_horizontal.scrollToPosition(itemCount - 1)
            }
        } ?: let {
            mChapterImagesRvAdapter = ChapterImagesRvAdapter(context, images.toMutableList(), this, this)
            rv_pager_horizontal.adapter = mChapterImagesRvAdapter
        }
        initPageChange()
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
        requestChapterImages()
    }

    override fun seekPrevChapter() {
        requestChapterImages()
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