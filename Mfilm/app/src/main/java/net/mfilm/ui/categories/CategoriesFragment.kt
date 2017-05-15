package net.mfilm.ui.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import net.mfilm.R
import net.mfilm.ui.base.stack.BaseStackFragment

/**
 * Created by tusi on 5/15/17.
 */
class CategoriesFragment : BaseStackFragment(), CategoriesMvpView {
    companion object {
        fun newInstance(): CategoriesFragment {
            val f = CategoriesFragment()
            return f
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(
                R.layout.fragment_categories, container, false)
    }

    override fun initFields() {

    }

    override fun initViews() {

    }

    override fun requestCategories() {

    }
}