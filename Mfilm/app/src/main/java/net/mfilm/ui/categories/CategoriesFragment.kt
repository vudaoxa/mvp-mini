package net.mfilm.ui.categories

import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.error_view.*
import kotlinx.android.synthetic.main.fragment_categories.*
import net.mfilm.R
import net.mfilm.data.network_retrofit.CategoriesResponse
import net.mfilm.data.network_retrofit.Category
import net.mfilm.ui.base.error_view.BasePullRefreshFragment
import net.mfilm.ui.base.rv.wrappers.StaggeredGridLayoutManagerWrapper
import net.mfilm.ui.categories.rv.CategoriesRvAdapter
import net.mfilm.utils.IndexTags
import timber.log.Timber
import tr.xip.errorview.ErrorView
import javax.inject.Inject

/**
 * Created by tusi on 5/15/17.
 */
class CategoriesFragment : BasePullRefreshFragment(), CategoriesMvpView {
    companion object {
        fun newInstance(): CategoriesFragment {
            val fragment = CategoriesFragment()
            return fragment
        }
    }

    override val swipeContainer: SwipeRefreshLayout?
        get() = swipe

    override val errorView: ErrorView?
        get() = error_view
    override val subTitle: Int?
        get() = R.string.failed_to_load
    override val spanCount: Int
        get() = resources.getInteger(R.integer.categories_span_count)
    @Inject
    lateinit var mCategoriesPresenter: CategoriesMvpPresenter<CategoriesMvpView>
    var mCategoriesRvAdapter: CategoriesRvAdapter<Category>? = null
    lateinit var mCategoriesRvLayoutManagerWrapper: StaggeredGridLayoutManagerWrapper
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_categories, container, false)
    }

    override fun onDestroy() {
        super.onDestroy()
        mCategoriesPresenter.onDetach()
    }
    override fun initFields() {
        activityComponent.inject(this)
        mCategoriesPresenter.onAttach(this)
    }

    override fun initViews() {
        super.initViews()
        initRv()
        requestCategories()
    }

    fun initRv() {
        rv.apply {
            mCategoriesRvLayoutManagerWrapper = StaggeredGridLayoutManagerWrapper(spanCount,
                    StaggeredGridLayoutManager.VERTICAL)
            layoutManager = mCategoriesRvLayoutManagerWrapper
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        rv.apply {
            mCategoriesRvLayoutManagerWrapper.spanCount = spanCount
            requestLayout()
        }
    }
    override fun requestCategories() {
        mCategoriesPresenter.requestCategories()
    }

    override fun onCategoriesResponse(categoriesResponse: CategoriesResponse?) {
        hideLoading()
        categoriesResponse.let { cr ->
            cr?.apply {
                cr.data.let { dt ->
                    dt?.apply {
                        if (dt.isNotEmpty()) {
                            buildCategories(dt)
                        } else onCategoriesNull()
                    } ?: let { onCategoriesNull() }
                }
            } ?: let { onCategoriesNull() }
        }
    }

    override fun onCategoriesNull() {
        Timber.e("------------------------onCategoriesNull-------------------------")
    }

    override fun buildCategories(data: List<Category>) {
        Timber.e("------buildCategories-----------${data.size}-----------------")
        mCategoriesRvAdapter = CategoriesRvAdapter(context, data.toMutableList(), this)
        rv.adapter = mCategoriesRvAdapter
    }

    override fun onErrorViewDemand(errorView: ErrorView?) {
        when (errorView) {
            this.errorView -> {
                requestCategories()
            }
        }
    }

    override fun isDataEmpty(): Boolean {
        mCategoriesRvAdapter?.apply {
            return itemCount == 0
        }
        return true
    }
    override fun onClick(position: Int, event: Int) {
        Timber.e("---------------------onClick--------------------$position")
        mCategoriesRvAdapter?.mData?.apply {
            screenManager?.onNewScreenRequested(IndexTags.FRAGMENT_CATEGORY, typeContent = null, obj = get(position))
        }
    }
}