package net.mfilm.ui.chapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_chapters.*
import net.mfilm.data.network_retrofit.Chapter
import net.mfilm.data.network_retrofit.ChaptersResponse
import net.mfilm.data.network_retrofit.Manga
import net.mfilm.ui.base.rv.BaseLoadMoreFragment
import net.mfilm.ui.base.rv.holders.TYPE_ITEM
import net.mfilm.ui.base.rv.wrappers.LinearLayoutManagerWrapper
import net.mfilm.ui.chapters.rv.ChaptersRvAdapter
import net.mfilm.ui.manga_info.MangaInfoMvpView
import net.mfilm.utils.AppConstants
import net.mfilm.utils.DebugLog
import net.mfilm.utils.LIMIT
import net.mfilm.utils.show
import java.io.Serializable
import javax.inject.Inject


/**
 * Created by tusi on 5/27/17.
 */
class ChaptersFragment : BaseLoadMoreFragment(), ChaptersMvpView, Serializable {
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

    private lateinit var manga: Manga
    private var mCurrentReadingChapter: Chapter? = null
    private var mPrevChapter: Chapter? = null
    private var mNextChapter: Chapter? = null
    override var prevChapter: Chapter?
        get() = mPrevChapter
        set(value) {
            mPrevChapter = value
        }
    override var nextChapter: Chapter?
        get() {
            mNextChapter?.apply { return this }
            loadMoreOnDemand()
            xx
            return null
        }
        set(value) {
            mNextChapter = value
        }
    override var currentReadingChapter: Chapter?
        get() = mCurrentReadingChapter
        set(value) {
            mCurrentReadingChapter = value
        }

    private var mPrevPosition = 0
    private var mNextPosition = 0
    private var mCurrentPosition = 0
    override var prevPosition: Int?
        get() = mPrevPosition
        set(value) {
            mPrevPosition = value!!
            chapters?.apply {
                prevChapter = get(mPrevPosition)
            }
        }
    override var nextPosition: Int?
        get() = mNextPosition
        set(value) {
            mNextPosition = value!!
            chapters?.apply {
                nextChapter = get(mNextPosition)
            }
        }
    override var currentReadingPosition: Int?
        get() = mCurrentPosition
        set(value) {
            mCurrentPosition = value!!
            chapters?.apply {
                currentReadingChapter = get(mCurrentPosition)
                if (mCurrentPosition < size) nextPosition = mCurrentPosition + 1
                if (mCurrentPosition > 0) prevPosition = mCurrentPosition - 1
            }
        }
    override val chapters: List<Chapter>?
        get() = mChaptersRvAdapter?.mData

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(net.mfilm.R.layout.fragment_chapters, container, false)
    }

    override fun initFields() {
        manga = arguments.getSerializable(AppConstants.EXTRA_DATA) as Manga
        activityComponent.inject(this)
        mChaptersPresenter.onAttach(this)
    }

    override fun initViews() {
        initRv()
        requestChapters()
    }

    fun initRv() {
        rv.apply {
            layoutManager = LinearLayoutManagerWrapper(context)
            setupOnLoadMore(this, mCallBackLoadMore)
        }
    }

    override fun requestChapters() {
        mChaptersPresenter.requestChapters(manga.id!!, LIMIT, page++)
    }

    override fun onChaptersResponse(chaptersResponse: ChaptersResponse?) {
        chaptersResponse.let { cr ->
            cr?.apply {
                cr.chapters.let { cs ->
                    cs?.apply {
                        cs.data.let { dt ->
                            dt?.apply {
                                if (dt.isNotEmpty()) {
                                    initChapters(dt)
                                } else onChaptersNull()
                            } ?: let { onChaptersNull() }
                        }
                    } ?: let { onChaptersNull() }
                }
            } ?: let { onChaptersNull() }
        }
    }

    override fun onChaptersNull() {
        DebugLog.e("----------------onChaptersNull------------------")
        mChaptersRvAdapter?.apply {
            onAdapterLoadMoreFinished {
                nullByAdapter(true)
            }
        } ?: let { nullByAdapter(false) }
    }

    override fun initChapters(chapters: List<Chapter>) {
        DebugLog.e("----------------initChapters-----------------${chapters.size}-------")
        root_view.show(true)
        mChaptersRvAdapter?.apply {
            onAdapterLoadMoreFinished {
                mData?.addAll(chapters)
                notifyDataSetChanged()
            }
        } ?: let {
            mChaptersRvAdapter = ChaptersRvAdapter(context, chapters.toMutableList(), this)
            rv.adapter = mChaptersRvAdapter
            currentReadingPosition = 0
//            currentReadingChapter = chapters[0]
        }
    }

    override fun onClick(position: Int, event: Int) {
        when (event) {
            TYPE_ITEM -> {
                mChaptersRvAdapter?.apply {
                    mData.let { d ->
                        d?.apply {
                            val chapter = d[position]
                            parentFragment?.apply {
                                if (this is MangaInfoMvpView) {
                                    currentReadingPosition = position
//                                    currentReadingChapter = chapter
                                    onReadBtnClicked()
                                }
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
        DebugLog.e("--------------------onLoadMore----------------------")
        mChaptersRvAdapter?.onAdapterLoadMore { requestChapters() }
    }

    override fun loadMoreOnDemand() {
        mCallBackLoadMore?.onLoadMore()
    }
//    override fun onConfigurationChanged(newConfig: Configuration?) {
//        super.onConfigurationChanged(newConfig)
//        view?.requestLayout()
//    }


}