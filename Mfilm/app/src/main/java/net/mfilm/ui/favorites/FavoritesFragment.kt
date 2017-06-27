package net.mfilm.ui.favorites

import android.content.res.Configuration
import android.os.Bundle
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import kotlinx.android.synthetic.main.empty_data_view.*
import kotlinx.android.synthetic.main.fragment_favorites.*
import net.mfilm.R
import net.mfilm.data.db.models.MangaFavoriteRealm
import net.mfilm.ui.base.rv.holders.TYPE_ITEM
import net.mfilm.ui.base.rv.wrappers.StaggeredGridLayoutManagerWrapper
import net.mfilm.ui.base.stack.BaseStackFragment
import net.mfilm.ui.manga.AdapterTracker
import net.mfilm.ui.manga.EmptyDataView
import net.mfilm.ui.manga.rv.MangasRealmRvAdapter
import net.mfilm.utils.IndexTags
import net.mfilm.utils.filtersFavorites
import net.mfilm.utils.handler
import net.mfilm.utils.show
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by MRVU on 6/20/2017.
 */
class FavoritesFragment : BaseStackFragment(), FavoritesMvpView {
    companion object {
        fun newInstance(): FavoritesFragment {
            val fragment = FavoritesFragment()
            return fragment
        }
    }

    override val optionsMenuId: Int
        get() = R.menu.favorites
    override val layoutEmptyData: View?
        get() = layout_empty_data
    override val tvDesEmptyData: TextView?
        get() = tv_des
    override val emptyDesResId: Int
        get() {
            return R.string.empty_data_favorite
        }
    private var mEmptyDataView: EmptyDataView? = null
    override var emptyDataView: EmptyDataView?
        get() = mEmptyDataView
        set(value) {
            mEmptyDataView = value
        }
    override val spanCount: Int
        get() = resources.getInteger(R.integer.mangas_span_count)
    override val spnFilterTracker = AdapterTracker({
        Timber.e("--------------spnFilterTracker---------")
    })

    @Inject
    lateinit var mFavoritesPresenter: FavoritesMvpPresenter<FavoritesMvpView>
    lateinit var mMangasRvLayoutManagerWrapper: StaggeredGridLayoutManagerWrapper
    var mMangasRvAdapter: MangasRealmRvAdapter<MangaFavoriteRealm>? = null
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_favorites, container, false)
    }

    override fun onDestroy() {
        super.onDestroy()
        mFavoritesPresenter.onDetach()
    }
    override fun initFields() {
        searchable = true
        activityComponent.inject(this)
        mFavoritesPresenter.onAttach(this)
        title = getString(R.string.favorites)
    }

    override fun initViews() {
        initSpnFilters()
        initEmptyDataView()
        initRv()
        requestFavorites()
    }

    override fun initEmptyDataView() {
        emptyDataView = EmptyDataView(context, spn_filter, layoutEmptyData, tvDesEmptyData, emptyDesResId)
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

    override fun onReceiveOptionsMenuItem(item: MenuItem) {
        Timber.e("-----onReceiveOptionsMenuItem-----$isVisible---- ${item.title}--------------------------------------------")
        if (!isVisible) return
        when (item.itemId) {
            R.id.action_favorites_search -> {

            }
            R.id.action_favorites_sort -> {

            }
            R.id.action_favorites_edit -> {

            }
        }
    }

    override fun requestFavorites() {
        mFavoritesPresenter.requestFavorites()
    }

    override fun onFavoritesResponse(mangaFavoriteRealms: List<MangaFavoriteRealm>?) {
        hideLoading()
        mangaFavoriteRealms.let { mr ->
            mr?.apply {
                if (mr.isNotEmpty()) {
                    buildFavorites(mr)
                } else onFavoritesNull()
            } ?: let { onFavoritesNull() }
        }
    }

    override fun onFavoritesNull() {
        Timber.e("----------------onFavoritesNull------------------")
        mMangasRvAdapter?.clear()
        emptyDataView?.apply {
            hideSomething()
            showEmptyDataView(true)
        }
    }

    override fun buildFavorites(mangaFavoriteRealms: List<MangaFavoriteRealm>) {
        Timber.e("---------------buildFavorites---------------${mangaFavoriteRealms.size}")
        context ?: return
        spn_filter.show(true)
        emptyDataView?.showEmptyDataView(false)
        mMangasRvAdapter?.apply {
            clear()
            mData?.addAll(mangaFavoriteRealms)
            notifyDataSetChanged()
        } ?: let {
            mMangasRvAdapter = MangasRealmRvAdapter(context, mangaFavoriteRealms.toMutableList(), this)
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