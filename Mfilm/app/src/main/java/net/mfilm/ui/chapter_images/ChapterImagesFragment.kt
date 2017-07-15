package net.mfilm.ui.chapter_images

import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.afollestad.materialdialogs.MaterialDialog
import com.github.piasy.biv.BigImageViewer
import com.joanzapata.iconify.IconDrawable
import com.lsjwzh.widget.recyclerviewpager.RecyclerViewPager
import kotlinx.android.synthetic.main.error_view.*
import kotlinx.android.synthetic.main.fragment_chapter_images.*
import kotlinx.android.synthetic.main.viewer_footer.*
import kotlinx.android.synthetic.main.viewer_header.*
import net.mfilm.R
import net.mfilm.data.network_retrofit.Chapter
import net.mfilm.data.network_retrofit.ChapterImage
import net.mfilm.data.network_retrofit.ChapterImagesResponse
import net.mfilm.ui.base.error_view.BaseErrorViewFragment
import net.mfilm.ui.base.rv.holders.TYPE_ITEM
import net.mfilm.ui.base.rv.holders.TYPE_ITEM_PREVIEW
import net.mfilm.ui.base.rv.wrappers.LinearLayoutManagerWrapper
import net.mfilm.ui.chapter_images.rv.ChapterImagesRvAdapter
import net.mfilm.ui.chapters.ChaptersMvpView
import net.mfilm.utils.*
import timber.log.Timber
import tr.xip.errorview.ErrorView
import javax.inject.Inject

/**
 * Created by tusi on 5/29/17.
 */
class ChapterImagesFragment(private var mChaptersFragment: ChaptersMvpView? = null)
    : BaseErrorViewFragment(), ChapterImagesMvpView {
    companion object {
        fun newInstance(mChaptersFragment: Any?): ChapterImagesFragment {
            val fragment = ChapterImagesFragment(mChaptersFragment as? ChaptersMvpView?)
            return fragment
        }
    }

    override val rvPreview: RecyclerView
        get() = rv_preview
    private var mWebtoon = false
    override var webtoon: Boolean
        get() = mWebtoon
        set(value) {
            mWebtoon = value
        }
    override val errorView: ErrorView?
        get() = error_view
    override val subTitle: Int?
        get() = R.string.failed_to_load

    override fun onErrorViewDemand(errorView: ErrorView?) {
        request()
    }

    @Inject
    lateinit var mChapterImagesPresenter: ChapterImagesMvpPresenter<ChapterImagesMvpView>
    private var mChapterImagesRvAdapter: ChapterImagesRvAdapter<ChapterImage>? = null
    private var mChapterImagesPreviewRvAdapter: ChapterImagesRvAdapter<ChapterImage>? = null
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
        super.initViews()
        initRv()
        initHeader()
        initAds()
        initBtnViewContinue()
        initPageChange()
        requestChapterImages()
    }

    override fun onDestroy() {
        mChapterImagesPresenter.onDetach()
        super.onDestroy()
    }

    override fun initRv() {
        context ?: return
        rv_pager_horizontal.run {
            mLayoutWrapper = LinearLayoutManagerWrapper(context, LinearLayoutManager.HORIZONTAL, false)
            layoutManager = mLayoutWrapper
            addOnPageChangedListener(mPageChangedListener)
        }
//        rv_vertical.run {
//            layoutManager = LinearLayoutManagerWrapper(context)
//        }
        initRvPreview()
    }

    override fun initRvPreview() {
        rvPreview.run {
            layoutManager = LinearLayoutManagerWrapper(context, LinearLayoutManager.HORIZONTAL, false)
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
            val icon: IconDrawable?
            val show: Boolean
            if (!next) {
                icon = if (orientation == LinearLayoutManager.HORIZONTAL) icon_left else icon_up
                show = btn_left.isEnabled
            } else {
                icon = if (orientation == LinearLayoutManager.HORIZONTAL) icon_right else icon_down
                show = btn_right.isEnabled
            }
            btn_continue.show(show)
            btn_continue.setImageDrawable(icon)
        }
    }

    override fun initHeader() {
        btn_share.setOnClickListener {
            sendShareIntent(context, mChapterImagesRvAdapter?.mData?.getOrNull(currentPage)?.url)
        }
        btn_back.setOnClickListener { baseActivity?.onBackPressed() }
    }

    override fun initPageChange() {

        btn_left.setImageDrawable(icon_left_small)
        btn_right.setImageDrawable(icon_right_small)
        btn_left.setOnClickListener { prev() }
        btn_right.setOnClickListener { next() }
        btn_eye.setImageDrawable(icon_eye)
        btn_eye.setOnClickListener { preview() }
    }

    fun pageChange() {
        currentPage = 0
        mChapterImagesRvAdapter?.run {
            tv_page_count.text = "${currentPage + 1}/$itemCount"
        }
    }

    override fun preview() {
        rv_pager_horizontal.toggleShow()
        rvPreview.toggleShow()
        btn_continue.show(false)
    }
    private var next = false
    private val mPageChangedListener = RecyclerViewPager.OnPageChangedListener { p0, p1 ->
        Timber.e("-----OnPageChangedListener--------------- $p0------ $p1---------------")
        mChapterImagesRvAdapter?.run {
            currentPage = p1
            val itemCount = itemCount
            tv_page_count.text = "${p1 + 1}/$itemCount"
            configBtnContinue(p0, p1, itemCount)
            mChaptersFragment?.run {
                currentReadingChapter.let { c ->
                    c?.run {
                        saveReadingPage(c, p1)
                    }
                }
            }
        }
    }

    fun configBtnContinue(p0: Int, p1: Int, size: Int) {
        if (Math.abs(p0 - p1) != 1) return
        when (p1) {
            0 -> {
                next = false
                showBtnViewContinue()
            }
            size - 1 -> {
                next = true
                showBtnViewContinue()
            }
            else -> {
                btn_continue.show(false)
            }
        }
    }

    override fun saveHistoryChapter(chapter: Chapter) {
        mChapterImagesPresenter.saveHistoryChapter(chapter)
    }

    override fun saveReadingPage(chapter: Chapter, page: Int) {
        mChapterImagesPresenter.saveReadingPage(chapter, page)
    }
    private fun request() {
        mChaptersFragment?.run {
            currentReadingChapter.let { c ->
                c?.run {
                    Timber.e("----------initViews---------$c---------")
                    requestChapterImages(c.id!!)
                    tv_chapter_name?.text = c.name ?: return
                    saveHistoryChapter(c)
                    initPagingState(c)
                } ?: let { onFailure() }
            }
        }
    }

    override fun initPagingState(chapter: Chapter) {
        chapter.run {
            btn_left.enable(pagingState !in listOf<Any?>(PagingState.FIRST, PagingState.SINGLE))
            btn_right.enable(pagingState !in listOf<Any?>(PagingState.LAST, PagingState.SINGLE))
        }
    }

    private var countRequestChapterImages = 0
    override fun requestChapterImages() {
        btn_continue.show(false)
        if (countRequestChapterImages++ > 0) {
            interAds(null, { request() })
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
//        btn_eye.enable(false)
    }

    override fun buildChapterImages(images: List<ChapterImage>) {
        Timber.e("-----buildChapterImages------------------${images.size}------------------------------")
        if (!webtoon)
            mChapterImagesPresenter.requestBitmapSize(images.getOrNull(3)?.url)
        BigImageViewer.prefetch(*(images.map { Uri.parse(it.url) }.toTypedArray()))
        mChapterImagesRvAdapter?.run {
            clear()
            addAll(images)
            notifyDataSetChanged()
            handler({
                val x = if (!next && countRequestChapterImages > 0) itemCount - 1 else 0
//                if (webtoon)
//                    rv_vertical.scrollToPosition(x)
//                else
                    rv_pager_horizontal.scrollToPosition(x)
            })
        } ?: let {
            mChapterImagesRvAdapter = ChapterImagesRvAdapter(context, images.toMutableList(), this, this)
            rv_pager_horizontal.adapter = mChapterImagesRvAdapter
        }
        pageChange()
        buildChapterImagesPreview(images)
    }

    override fun buildChapterImagesPreview(images: List<ChapterImage>) {
        mChapterImagesPreviewRvAdapter?.run {
            clear()
            addAll(images)
            notifyDataSetChanged()
        } ?: let {
            mChapterImagesPreviewRvAdapter = ChapterImagesRvAdapter(context, images.toMutableList(), this, this, true)
            rvPreview.adapter = mChapterImagesPreviewRvAdapter
        }
    }
    override fun showWebtoon() {
        mLayoutWrapper?.run {
            orientation = LinearLayoutManager.VERTICAL
            rv_pager_horizontal.requestLayout()
        }
        webtoon = true
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
                    MaterialDialog.SingleButtonCallback { _, _ -> showWebtoon() })
        }
    }

    override fun isDataEmpty(): Boolean {
        return mChapterImagesRvAdapter?.run {
            itemCount == 0
        } ?: true
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
//        if (isVisiOk())
//            requestChapterImages()
//        else interAds(null, {})
        requestChapterImages()
    }

    override fun seekPrevChapter() {
        requestChapterImages()
    }

    override fun onRvFailure(position: Int) {
        mChapterImagesRvAdapter?.removeAt(position)
    }

    override fun onClick(position: Int, event: Int) {
        Timber.e("---------------------onClick----------$event----------$position")
        viewer_header.toggleShow()
        viewer_footer.toggleShow()
        when (event) {
            TYPE_ITEM -> {
                mChapterImagesRvAdapter?.run {
                    when (position) {
                        0 -> {
                            if (btn_left.isEnabled)
                                btn_continue.toggleShow()
                        }
                        itemCount - 1 -> {
                            if (btn_right.isEnabled)
                                btn_continue.toggleShow()
                        }
                    }
                }
            }
            TYPE_ITEM_PREVIEW -> {
                rvPreview.toggleShow()
                rv_pager_horizontal.run {
                    toggleShow()
                    scrollToPosition(position)
                }
            }
        }

    }
}