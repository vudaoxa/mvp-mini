package net.mfilm.ui.categories

import net.mfilm.data.network_retrofit.CategoriesResponse
import net.mfilm.data.network_retrofit.Category
import net.mfilm.ui.base.MvpView
import net.mfilm.utils.ICallbackOnClick
import net.mfilm.utils.ICallbackRv
import net.mfilm.utils.ICallbackSpanCount

/**
 * Created by tusi on 5/15/17.
 */
interface CategoriesMvpView : MvpView, ICallbackOnClick, ICallbackSpanCount, ICallbackRv {
    fun requestCategories()
    fun onCategoriesResponse(categoriesResponse: CategoriesResponse?)
    fun buildCategories(data: List<Category>)
    fun onCategoriesNull()
}