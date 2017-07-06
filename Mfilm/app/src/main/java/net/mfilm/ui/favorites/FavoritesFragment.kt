package net.mfilm.ui.favorites

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.joanzapata.iconify.widget.IconTextView
import kotlinx.android.synthetic.main.bottom_fun_view.*
import kotlinx.android.synthetic.main.empty_data_view.*
import kotlinx.android.synthetic.main.fragment_realm.*
import kotlinx.android.synthetic.main.layout_input_text.*
import net.mfilm.R
import net.mfilm.data.db.models.MangaFavoriteRealm
import net.mfilm.ui.base.realm.BaseRealmFragment
import net.mfilm.ui.base.rv.wrappers.StaggeredGridLayoutManagerWrapper
import net.mfilm.ui.custom.SimpleViewAnimator
import net.mfilm.ui.manga.*
import net.mfilm.ui.manga.rv.BaseRvRealmAdapter
import net.mfilm.utils.*
import org.angmarch.views.NiceSpinner
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by MRVU on 6/20/2017.
 */
class FavoritesFragment : BaseRealmFragment<MangaFavoriteRealm>(), FavoritesMvpView {
    companion object {
        fun newInstance(): FavoritesFragment {
            val fragment = FavoritesFragment()
            return fragment
        }
    }

    override val rvMain: RecyclerView
        get() = rv
    override val rvFilter: RecyclerView
        get() = rv_filter
    override var layoutManagerMain: StaggeredGridLayoutManagerWrapper
        get() = mMangasRvLayoutManagerWrapper
        set(value) {
            mMangasRvLayoutManagerWrapper = value
        }
    override var layoutManagerFilter: StaggeredGridLayoutManagerWrapper
        get() = mMangasRvFilterLayoutManagerWrapper
        set(value) {
            mMangasRvFilterLayoutManagerWrapper = value
        }
    override var adapterMain: BaseRvRealmAdapter<MangaFavoriteRealm>?
        get() = mMangasRvAdapter
        set(value) {
            mMangasRvAdapter = value
        }
    override var adapterFilter: BaseRvRealmAdapter<MangaFavoriteRealm>?
        get() = mMangasFilterRvAdapter
        set(value) {
            mMangasFilterRvAdapter = value
        }
    private var mSearchPassByTime: PassByTime? = null
    override var searchPassByTime: PassByTime?
        get() = mSearchPassByTime
        set(value) {
            mSearchPassByTime = value
        }
    override val bottomFunView: SimpleViewAnimator
        get() = bottom_fun
    override val btnSelect: Button
        get() = btn_toggle_select
    private var mUndoBtn: UndoBtn? = null
    override var undoBtn: UndoBtn?
        get() = mUndoBtn
        set(value) {
            mUndoBtn = value
        }
    override val btnUndo: Button
        get() = btn_undo
    override val btnSubmit: Button
        get() = btn_delete
    override val btnDone: Button
        get() = btn_done
    override val mFilters: List<Filter>
        get() = filtersFavorites
    override val spnFilter: NiceSpinner
        get() = spn_filter
    override val mLayoutInputText: LinearLayout
        get() = layout_input_text
    override val edtSearch: EditText
        get() = edt_search
    override val imgClear: IconTextView
        get() = img_clear

    override val actionSearch: Int
        get() = R.id.action_favorites_search
    override val actionSort: Int
        get() = R.id.action_favorites_sort
    override val actionEdit: Int
        get() = R.id.action_favorites_edit
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
        sort()
    })

    @Inject
    lateinit var mFavoritesPresenter: FavoritesMvpPresenter<FavoritesMvpView>
    lateinit var mMangasRvLayoutManagerWrapper: StaggeredGridLayoutManagerWrapper
    lateinit var mMangasRvFilterLayoutManagerWrapper: StaggeredGridLayoutManagerWrapper
    var mMangasRvAdapter: BaseRvRealmAdapter<MangaFavoriteRealm>? = null
    var mMangasFilterRvAdapter: BaseRvRealmAdapter<MangaFavoriteRealm>? = null
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_realm, container, false)
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
        super.initViews()
        requestFavorites()
    }

    override fun done() {
        super.done()
        requestFavorites()
    }

    override fun search(query: String) {
        val favoritesFilter = adapterMain?.mData?.filter { it.name!!.contains(query, true) }
        favoritesFilter.let { ff ->
            ff?.run {
                if (ff.isNotEmpty()) {
                    buildFavoritesFilter(ff)
                } else onRealmFilterNull()
            } ?: let { onRealmFilterNull() }
        }
    }

    //100% NOT MOVE, BECAUSE GENERIC CAN'T BE SORTED
    override fun sort() {
        Timber.e("------------sort---------------------------")
        val filter = mFilters[spnFilterTracker.mPosition]
        when (filter.content) {
            TYPE_FILTER_AZ -> {
                adapterMain?.run {
                    mData?.sortBy { it.name }
                    notifyDataSetChanged()
                }
            }
            TYPE_FILTER_TIME -> {
                adapterMain?.run {
                    mData?.sortByDescending { it.time }
                    notifyDataSetChanged()
                }
            }
        }
    }

    override fun requestFavorites() {
        mFavoritesPresenter.requestFavorites()
    }

    override fun onToggle() {
        selectedItems?.run {
            mFavoritesPresenter.toggleFav(this)
        }
    }

    override fun deleteAll(f: (() -> Unit)?) {
        super.deleteAll({
            mFavoritesPresenter.delete(MangaFavoriteRealm::class.java)
        })
    }
    override fun onFavoritesResponse(mangaFavoriteRealms: List<MangaFavoriteRealm>?) {
//        Timber.e("------onFavoritesResponse-----------$mangaFavoriteRealms---------------------")
        hideLoading()
        mangaFavoriteRealms?.run {
            if (isNotEmpty()) {
                buildFavorites(this)
            } else onFavoritesNull()
        } ?: let { onFavoritesNull() }
    }

    override fun onFavoritesNull() {
        Timber.e("----------------onFavoritesNull------------------")
        adapterMain?.run {
            doByAllSelected(this)
        } ?: let {
            //set it after onFragmentEntered
            handler({
                showEmptyDataView(true)
                setScrollToolbarFlag(true)
            })
        }
    }

    override fun buildFavoritesFilter(mangaFavoriteRealms: List<MangaFavoriteRealm>) {
        Timber.e("----------buildFavoritesFilter-------- ${mangaFavoriteRealms.size}----------------------------")
        adapterFilter?.run {
            doByAllSelected(this, mangaFavoriteRealms)
        } ?: let {
            adapterFilter = BaseRvRealmAdapter(context, mangaFavoriteRealms.toMutableList(), this, this, true)
            rvFilter.adapter = adapterFilter
        }
        showEmptyDataView(false)
        handler({
            rvMain.show(false)
            rvFilter.show(true)
        })
    }

    //it's for rv only
    override fun buildFavorites(mangaFavoriteRealms: List<MangaFavoriteRealm>) {
        Timber.e("---------------buildFavorites--------$context-------${mangaFavoriteRealms.size}")
        context ?: return
        setScrollToolbarFlag(false)
        adapterMain?.run {
            doByAllSelected(this, mangaFavoriteRealms)
        } ?: let {
            //the first build
            adapterMain = BaseRvRealmAdapter(context, mangaFavoriteRealms.toMutableList(), this, this)
            rvMain.adapter = adapterMain
            showEmptyDataView(false)
        }
    }

    override fun adapterClicked(ad: BaseRvRealmAdapter<MangaFavoriteRealm>, position: Int, f: (() -> Unit)?) {
        super.adapterClicked(ad, position, {
            ad.mData?.run {
                screenManager?.onNewScreenRequested(IndexTags.FRAGMENT_MANGA_INFO,
                        typeContent = null, obj = this[position].id)
            }
        })
    }
}