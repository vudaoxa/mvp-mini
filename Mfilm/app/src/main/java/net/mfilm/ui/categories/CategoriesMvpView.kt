package net.mfilm.ui.categories

import net.mfilm.data.network_retrofit.CategoriesResponse
import net.mfilm.ui.base.MvpView

/**
 * Created by tusi on 5/15/17.
 */
interface CategoriesMvpView : MvpView {
    fun requestCategories()
    fun onCategoriesResponse(categoriesResponse: CategoriesResponse?)
}