package net.mfilm.ui.history

import android.content.res.Configuration
import android.os.Bundle
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.fragment_history.*
import net.mfilm.R
import net.mfilm.data.db.models.MangaHistoryRealm
import net.mfilm.ui.base.rv.holders.TYPE_ITEM
import net.mfilm.ui.base.rv.wrappers.StaggeredGridLayoutManagerWrapper
import net.mfilm.ui.base.stack.BaseStackFragment
import net.mfilm.ui.manga.AdapterTracker
import net.mfilm.ui.manga.rv.MangasRealmRvAdapter
import net.mfilm.utils.IndexTags
import net.mfilm.utils.filtersFavorites
import net.mfilm.utils.handler
import net.mfilm.utils.show
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by tusi on 6/25/17.
 */
class HistoryFragment : BaseStackFragment(), HistoryMvpView {
    companion object {
        fun newInstance(): HistoryFragment {
            val fragment = HistoryFragment()
            return fragment
        }
    }

    override val spanCount: Int
        get() = resources.getInteger(R.integer.mangas_span_count)
    override val spnFilterTracker = AdapterTracker({
        Timber.e("--------------spnFilterTracker---------")
    })
    @Inject
    lateinit var mHistoryPresenter: HistoryMvpPresenter<HistoryMvpView>
    lateinit var mMangasRvLayoutManagerWrapper: StaggeredGridLayoutManagerWrapper
    var mMangasRvAdapter: MangasRealmRvAdapter<MangaHistoryRealm>? = null
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_history, container, false)
    }

    override fun onDestroy() {
        super.onDestroy()
        mHistoryPresenter.onDetach()
    }

    override fun initFields() {
        activityComponent.inject(this)
        mHistoryPresenter.onAttach(this)
        title = getString(R.string.history)
    }

    override fun initViews() {
        initSpnFilters()
        initRv()
        requestHistory()
    }

    override fun initSpnFilters() {
        val banksAdapter = ArrayAdapter(activity,
                R.layout.item_spn_filter, filtersFavorites.map { getString(it.resId) })
        spn_filter.apply {
            setAdapter(banksAdapter)
            setOnItemSelectedListener(spnFilterTracker)
        }
    }

    override fun initRv() {
        rv.apply {
            mMangasRvLayoutManagerWrapper = StaggeredGridLayoutManagerWrapper(spanCount,
                    StaggeredGridLayoutManager.VERTICAL)
            layoutManager = mMangasRvLayoutManagerWrapper
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        handler({
            rv.apply {
                mMangasRvLayoutManagerWrapper.spanCount = spanCount
                requestLayout()
            }
        })
    }

    override fun requestHistory() {
        mHistoryPresenter.requestHistory()
    }

    override fun onHistoryResponse(mangaHistoryRealms: List<MangaHistoryRealm>?) {
        hideLoading()
        mangaHistoryRealms.let { mhr ->
            mhr?.apply {
                if (mhr.isNotEmpty()) {
                    buildHistory(mhr)
                } else onHistoryNull()
            } ?: let { onHistoryNull() }
        }
    }

    override fun onHistoryNull() {
        Timber.e("----------------onHistoryNull------------------")
    }

    override fun buildHistory(mangaHistoryRealms: List<MangaHistoryRealm>) {
        Timber.e("---------------buildHistory---------------${mangaHistoryRealms.size}")
        spn_filter.show(true)
        mMangasRvAdapter?.apply {
            mData?.clear()
            mData?.addAll(mangaHistoryRealms)
            notifyDataSetChanged()
        } ?: let {
            mMangasRvAdapter = MangasRealmRvAdapter(context, mangaHistoryRealms.toMutableList(), this)
            rv.adapter = mMangasRvAdapter
        }
    }

    override fun onClick(position: Int, event: Int) {
        Timber.e("---------------------onClick--------------------$position")
        if (event != TYPE_ITEM) return
        mMangasRvAdapter?.mData?.apply {
            screenManager?.onNewScreenRequested(IndexTags.FRAGMENT_MANGA_INFO, typeContent = null, obj = this[position].id)
        }
    }
}