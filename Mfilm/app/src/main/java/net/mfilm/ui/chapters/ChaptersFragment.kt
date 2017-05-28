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
import net.mfilm.utils.AppConstants
import net.mfilm.utils.DebugLog
import net.mfilm.utils.LIMIT
import net.mfilm.utils.show
import java.io.Serializable
import javax.inject.Inject

/**
 * Created by tusi on 5/27/17.
 */
class ChaptersFragment : BaseLoadMoreFragment(), ChaptersMvpView {
    @Inject
    lateinit var mChaptersPresenter: ChaptersMvpPresenter<ChaptersMvpView>
    var mChaptersRvAdatepr: ChaptersRvAdapter<Chapter>? = null
    override var isDataEnd: Boolean
        get() = false
        set(value) {}

    override fun onLoadMore() {
        DebugLog.e("--------------------onLoadMore----------------------")
        mChaptersRvAdatepr?.onAdapterLoadMore { requestChapters() }
    }

    companion object {
        fun newInstance(obj: Any?): ChaptersFragment {
            val fragment = ChaptersFragment()
            val bundle = Bundle()
            bundle.putSerializable(AppConstants.EXTRA_DATA, obj as? Serializable)
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var manga: Manga
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        manga = arguments.getSerializable(AppConstants.EXTRA_DATA) as Manga
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(net.mfilm.R.layout.fragment_chapters, container, false)
    }

    override fun initFields() {
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
        mChaptersRvAdatepr?.apply {
            onAdapterLoadMoreFinished {
                nullByAdapter(true)
            }
        } ?: let { nullByAdapter(false) }
    }

    override fun initChapters(chapters: List<Chapter>) {
        DebugLog.e("----------------initChapters-----------------${chapters.size}-------")
        root_view.show(true)
        mChaptersRvAdatepr?.apply {
            onAdapterLoadMoreFinished {
                mData?.addAll(chapters)
                notifyDataSetChanged()
            }
        } ?: let {
            mChaptersRvAdatepr = ChaptersRvAdapter(context, chapters.toMutableList(), this)
            rv.adapter = mChaptersRvAdatepr
        }
    }

    override fun onClick(position: Int, event: Int) {
        when (event) {
            TYPE_ITEM -> {

            }
        }
    }

//    override fun onConfigurationChanged(newConfig: Configuration?) {
//        super.onConfigurationChanged(newConfig)
//        view?.requestLayout()
//    }
}