package net.mfilm.ui.favorites

import android.content.res.Configuration
import android.os.Bundle
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.fragment_favorites.*
import net.mfilm.R
import net.mfilm.data.db.models.MangaRealm
import net.mfilm.ui.base.rv.wrappers.StaggeredGridLayoutManagerWrapper
import net.mfilm.ui.base.stack.BaseStackFragment
import net.mfilm.ui.favorites.rv.MangasRealmRvAdapter
import net.mfilm.ui.manga.AdapterTracker
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

    override val spanCount: Int
        get() = resources.getInteger(R.integer.mangas_span_count)
    override val spnFilterTracker = AdapterTracker({
        Timber.e("--------------spnFilterTracker---------")
    })

    @Inject
    lateinit var mFavoritesPresenter: FavoritesMvpPresenter<FavoritesMvpView>
    lateinit var mMangasRvLayoutManagerWrapper: StaggeredGridLayoutManagerWrapper
    var mMangasRvAdapter: MangasRealmRvAdapter<MangaRealm>? = null
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_favorites, container, false)
    }

    override fun onDestroy() {
        super.onDestroy()
        mFavoritesPresenter.onDetach()
    }
    override fun initFields() {
        activityComponent.inject(this)
        mFavoritesPresenter.onAttach(this)
        title = getString(R.string.favorites)
    }

    override fun initViews() {
        initSpnFilters()
        initRv()
        requestFavorites()
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

    override fun requestFavorites() {
        mFavoritesPresenter.requestFavorites()
    }

    override fun onFavoritesResponse(mangaRealms: List<MangaRealm>?) {
        hideLoading()
        mangaRealms.let { mr ->
            mr?.apply {
                if (mr.isNotEmpty()) {
                    buildFavorites(mr)
                } else onFavoritesNull()
            } ?: let { onFavoritesNull() }
        }
    }

    override fun onFavoritesNull() {
        Timber.e("----------------onFavoritesNull------------------")
    }

    override fun buildFavorites(mangaRealms: List<MangaRealm>) {
        Timber.e("---------------buildFavorites---------------${mangaRealms.size}")
        spn_filter.show(true)
        mMangasRvAdapter?.apply {
            mData?.addAll(mangaRealms)
            notifyDataSetChanged()
        } ?: let {
            mMangasRvAdapter = MangasRealmRvAdapter(context, mangaRealms.toMutableList(), this)
            rv.adapter = mMangasRvAdapter
        }
    }

    override fun onClick(position: Int, event: Int) {
        Timber.e("---------------------onClick--------------------$position")
    }

}