package net.mfilm.ui.chapters

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.error_view.*
import kotlinx.android.synthetic.main.fragment_chapters.*
import net.mfilm.R
import net.mfilm.data.db.models.ChapterRealm
import net.mfilm.data.db.models.MangaHistoryRealm
import net.mfilm.data.network_retrofit.Chapter
import net.mfilm.data.network_retrofit.ChaptersResponse
import net.mfilm.data.network_retrofit.Manga
import net.mfilm.ui.base.rv.BaseLoadMoreFragment
import net.mfilm.ui.base.rv.holders.TYPE_ITEM
import net.mfilm.ui.base.rv.wrappers.LinearLayoutManagerWrapper
import net.mfilm.ui.chapter_images.ChapterImagesMvpView
import net.mfilm.ui.chapters.rv.ChaptersRvAdapter
import net.mfilm.ui.manga_info.MangaInfoMvpView
import net.mfilm.utils.*
import timber.log.Timber
import tr.xip.errorview.ErrorView
import java.io.Serializable
import javax.inject.Inject


/**
 * Created by tusi on 5/27/17.
 */
class ChaptersFragment : BaseLoadMoreFragment(), ChaptersMvpView {

    override fun onErrorViewDemand(errorView: ErrorView?) {
        when (errorView) {
            this.errorView -> {
                reset()
                requestChapters()
            }
            errorViewLoadMore -> {
                onLoadMore()
            }
        }
    }

    private var mHistory = false
    override var mangaHistory: Boolean
        get() = mHistory
        set(value) {
            mHistory = value
        }
    override val errorView: ErrorView?
        get() = error_view
    override val subTitle: Int?
        get() = R.string.failed_to_load
    override val errorViewLoadMore: ErrorView?
        get() = error_view_load_more as? ErrorView?
    override val subTitleLoadMore: Int?
        get() = R.string.failed_to_load_more
    override val swipeContainer: SwipeRefreshLayout?
        get() = null

    companion object {
        fun newInstance(obj: Any?): ChaptersFragment {
            val fragment = ChaptersFragment()
            val bundle = Bundle()
            bundle.putSerializable(AppConstants.EXTRA_DATA, obj as? Serializable)
            fragment.arguments = bundle
            return fragment
        }
    }

    @Inject
    lateinit var mChaptersPresenter: ChaptersMvpPresenter<ChaptersMvpView>
    var mChaptersRvAdapter: ChaptersRvAdapter<Chapter>? = null
    //    private var mChapterImagesFragment: ChapterImagesFragment?=null
    private var mChapterImagesFragment: ChapterImagesMvpView? = null

    private var manga: Manga? = null
    private var mCurrentReadingChapter: Chapter? = null
    private var mPrevChapter: Chapter? = null
    private var mNextChapter: Chapter? = null
    override var chapterImagesFragment: ChapterImagesMvpView?
        get() = mChapterImagesFragment
        set(value) {
            mChapterImagesFragment = value
        }
    override var prevChapter: Chapter?
        get() = mPrevChapter
        set(value) {
            mPrevChapter = value
        }
    override var nextChapter: Chapter?
        get() {
            mNextChapter?.run { return this }
            loadMoreOnDemand()
            return null
        }
        set(value) {
            mNextChapter = value
        }
    override var currentReadingChapter: Chapter?
        get() = mCurrentReadingChapter
        set(value) {
            mCurrentReadingChapter = value
            Timber.e("-----------currentReadingChapter---------$mCurrentReadingChapter")
        }

    private var mPrevPosition = 0
    private var mNextPosition = 0
    private var mCurrentPosition = 0
    override var prevPosition: Int?
        get() = mPrevPosition
        set(value) {
            chapters?.run {
                if (value!! >= 0) {
                    mPrevPosition = value
                    prevChapter = get(mPrevPosition)
                }
            }
        }
    override var nextPosition: Int?
        get() = mNextPosition
        set(value) {
            chapters?.run {
                if (value!! <= size - 1) {
                    mNextPosition = value
                    nextChapter = get(mNextPosition)
                }
            }
        }
    override var currentReadingPosition: Int?
        get() = mCurrentPosition
        set(value) {
            chapters?.run {
                Timber.e("-----------currentReadingPosition--------------$value------${size - 1}")
                if (value!! < 0) return
                if (value <= size - 1) {
                    mCurrentPosition = value
                    currentReadingChapter = get(mCurrentPosition)
                    if (mCurrentPosition < size - 1) nextPosition = mCurrentPosition + 1
                } else {
                    loadMoreOnDemand()
                    nextChapter = null
                }
                if (mCurrentPosition > 0) prevPosition = mCurrentPosition - 1
            }
        }
    override val chapters: List<Chapter>?
        get() = mChaptersRvAdapter?.mData

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(net.mfilm.R.layout.fragment_chapters, container, false)
    }

    override fun onResume() {
        super.onResume()
        mChaptersRvAdapter?.run {
            requestMangaHistory()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mChaptersPresenter.onDetach()
    }

    override fun initFields() {
        manga = arguments.getSerializable(AppConstants.EXTRA_DATA) as Manga
        tryOrExit {
            activityComponent?.inject(this)
            mChaptersPresenter.onAttach(this)
        }
    }

    override fun initViews() {
        super.initViews()
        initRv()
        initAds()
        initBtnRead()

        requestChapters()
    }

    override fun initAds() {
        super.initAds()
        pagesPerAds = 4
    }

    override fun initBtnRead() {
        btn_read.setOnClickListener { onReadBtnClicked() }
    }

    override fun initRv() {
        rv.run {
            layoutManager = LinearLayoutManagerWrapper(context)
            setupOnLoadMore(this, mCallBackLoadMore)
        }
    }

    override fun requestMangaHistory() {
        manga?.id?.run {
            mChaptersPresenter.requestMangaHistory(this)
        }
    }

    override fun requestChaptersHistory() {
        if (!mangaHistory) {
            manga?.id?.run {
                mChaptersPresenter.requestChaptersHistory(this)
            }
            mangaHistory = true
        }
    }

    override fun onMangaHistoryResponse(mangaHistoryRealm: MangaHistoryRealm) {
        Timber.e("----onMangaHistoryResponse--------$mangaHistoryRealm----------------------------------")
        mangaHistoryRealm.run {
            requestChaptersHistory()
            mChaptersRvAdapter?.updateReadingChapter(this)
            val text = TimeUtils.toFbFormatTime(context, time)
            tv_read_history.text = text
            btn_read.setText(R.string.resume)
        }
    }

    override fun onChaptersHistoryResponse(chaptersRealm: List<ChapterRealm>?) {
        chaptersRealm?.run {
            val ids = map { it.id }
            Timber.e("---onChaptersHistoryResponse---------$ids----------------------------------------")
            mChaptersRvAdapter?.updateChaptersHistory(ids)
        }
    }

    override fun requestChapters() {
        manga?.id?.run {
            mChaptersPresenter.requestChapters(this, LIMIT, page)
        }
    }

    override fun obtainChapterPagingState(prevPageUrl: String?, nextPageUrl: String?, chapters: List<Chapter>) {
        val prev = prevPageUrl == null
        val next = nextPageUrl == null
        chapters.run {
            if (prev) first().pagingState = PagingState.FIRST
            if (next) last().pagingState = PagingState.LAST
            if (prev && next) {
                first().pagingState = PagingState.SINGLE
                last().pagingState = PagingState.SINGLE
            }
        }
    }

    override fun onChaptersResponse(chaptersResponse: ChaptersResponse?) {
        hideLoading()
        chaptersResponse.let { cr ->
            cr?.run {
                cr.chapters.let { cs ->
                    cs?.run {
                        isDataEnd = TextUtils.isEmpty(cs.nextPageUrl)
                        cs.data.let { dt ->
                            dt?.run {
                                if (dt.isNotEmpty()) {
                                    buildChapters(dt)
                                    obtainChapterPagingState(cs.prevPageUrl, cs.nextPageUrl, dt)
                                } else onChaptersNull()
                            } ?: let { onChaptersNull() }
                        }
                    } ?: let { onChaptersNull() }
                }
            } ?: let { onChaptersNull() }
        }
    }

    override fun onChaptersNull() {
        Timber.e("----------------onChaptersNull------------------")
        mChaptersRvAdapter?.run {
            if (itemCount == 0) {
                adapterEmpty(true)
            } else {
                onAdapterLoadMoreFinished {
                    adapterEmpty(false)
                }
            }
        } ?: let { adapterEmpty(true) }
    }

    override fun buildChapters(chapters: List<Chapter>) {
        Timber.e("----------------buildChapters-----------------${chapters.size}---page --- $page-")

        btn_read.enable(true)
        mChaptersRvAdapter?.run {
            onAdapterLoadMoreFinished {
                val x = mData?.size //xxx readBtnClicked

                mData?.addAll(chapters)
                notifyDataSetChanged()
                chapterImagesFragment?.run {
                    seekCurrentReadingPosition(x)
                }
            }
        } ?: let {
            mChaptersRvAdapter = ChaptersRvAdapter(context, chapters.toMutableList(), this)
            rv.adapter = mChaptersRvAdapter
            seekCurrentReadingPosition(0)
        }
        if (page == PAGE_START)
            requestMangaHistory()
        page++
        notifyViewer()
    }

    override fun onFailure() {
        super.onFailure()
        btn_read.enable(false)
    }

    override fun onNoInternetConnections() {
        super.onNoInternetConnections()
        btn_read.enable(false)
    }

    override fun onClick(position: Int, event: Int) {
        when (event) {
            TYPE_ITEM -> {
                mChaptersRvAdapter?.run {
                    mData?.run {
                        parentFragment?.run {
                            if (this is MangaInfoMvpView) {
                                seekCurrentReadingPosition(position)
                                onReadBtnClicked()
                            }
                        }
                    }
                }
            }
        }
    }

    override var isDataEnd: Boolean
        get() = false
        set(value) {}

    override fun onLoadMore() {
        Timber.e("--------------------onLoadMore----------------------")
        mChaptersRvAdapter?.onAdapterLoadMore { requestChapters() }
    }

    override fun loadMoreOnDemand() {
        Timber.e("------------loadMoreOnDemand---------------")
        mCallBackLoadMore?.onLoadMore()
    }

    override fun loadMoreOnDemand(chapterImagesMvpView: ChapterImagesMvpView) {
        chapterImagesFragment = chapterImagesMvpView
        fun loadMore() {
            Timber.e("-----------------loadMore----------$chapterImagesFragment")
            loadMoreOnDemand()
        }
        nextChapter.let { n ->
            n?.run {
                seekNextChapter()
                chapterImagesFragment?.seekNextChapter()
            } ?: let {
                loadMore()
            }
        }
    }

    override fun loadInterrupted() {
        super.loadInterrupted()
        mChaptersRvAdapter?.onAdapterLoadMoreFinished()
    }

    override fun isDataEmpty(): Boolean {
        return mChaptersRvAdapter?.run {
            itemCount == 0
        } ?: true
    }

    override fun loadPrevOnDemand(chapterImagesMvpView: ChapterImagesMvpView) {
        chapterImagesFragment = chapterImagesMvpView
        seekPrevChapter()
    }

    fun notifyViewer() {
        handler({
            chapterImagesFragment?.run {
                seekNextChapter()
//                if (isVisible())
//                    seekNextChapter()
//                else interAds(page)
            } ?: let {
                Timber.e("--------------chapterImagesFragment null-------------------")
                interAds(page)
            }
        }, 250)
    }

    override fun seekCurrentReadingPosition(newPosition: Int?) {
        currentReadingPosition = newPosition
    }

    override fun seekNextChapter() {
        Timber.e("----------------seekNextChapter---------$currentReadingPosition")
        currentReadingPosition = (currentReadingPosition!! + 1)
    }

    override fun seekPrevChapter() {
        Timber.e("----------------seekPrevChapter---------$currentReadingPosition")
        currentReadingPosition?.run {
            if (this > 0) {
                currentReadingPosition = this - 1
                notifyViewer()
            }
        }
    }

    override fun saveMangaHistory() {
        manga?.run {
            mChaptersPresenter.saveMangaHistory(this)
        }
    }

    override fun onReadBtnClicked() {
        saveMangaHistory()
        requestChaptersHistory()
        screenManager?.onNewFragmentRequested(IndexTags.FRAGMENT_CHAPTER_IMAGES, fragment = this)
    }
}