package net.mfilm.ui.categories

import android.content.res.Configuration
import android.os.Bundle
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_categories.*
import net.mfilm.R
import net.mfilm.data.network_retrofit.CategoriesResponse
import net.mfilm.data.network_retrofit.Category
import net.mfilm.ui.base.rv.wrappers.StaggeredGridLayoutManagerWrapper
import net.mfilm.ui.base.stack.BaseStackFragment
import net.mfilm.ui.categories.rv.CategoriesRvAdapter
import net.mfilm.utils.IndexTags
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by tusi on 5/15/17.
 */
class CategoriesFragment : BaseStackFragment(), CategoriesMvpView {
    companion object {
        fun newInstance(): CategoriesFragment {
            val fragment = CategoriesFragment()
            return fragment
        }
    }

    @Inject
    lateinit var mCategoriesPresenter: CategoriesMvpPresenter<CategoriesMvpView>
    var mCategoriesRvAdapter: CategoriesRvAdapter<Category>? = null
    lateinit var mCategoriesRvLayoutManagerWrapper: StaggeredGridLayoutManagerWrapper
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(
                R.layout.fragment_categories, container, false)
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
        initRv()
        requestCategories()
    }

    fun initRv() {
        rv.apply {
            val spanCount = resources.getInteger(R.integer.categories_span_count)
            mCategoriesRvLayoutManagerWrapper = StaggeredGridLayoutManagerWrapper(spanCount,
                    StaggeredGridLayoutManager.VERTICAL)
            layoutManager = mCategoriesRvLayoutManagerWrapper
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        rv.apply {
            val spanCount = resources.getInteger(R.integer.categories_span_count)
            mCategoriesRvLayoutManagerWrapper.spanCount = spanCount
            requestLayout()
        }
    }
    override fun requestCategories() {
        mCategoriesPresenter.requestCategories()
    }

    override fun onCategoriesResponse(categoriesResponse: CategoriesResponse?) {
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

    override fun onClick(position: Int, event: Int) {
        Timber.e("---------------------onClick--------------------$position")
        mCategoriesRvAdapter?.mData?.apply {
            screenManager?.onNewScreenRequested(IndexTags.FRAGMENT_CATEGORY, typeContent = null, obj = get(position))
        }
    }
}