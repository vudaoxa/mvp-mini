package net.mfilm.ui.categories

import net.mfilm.ui.base.MvpPresenter

/**
 * Created by tusi on 6/12/17.
 */
interface CategoriesMvpPresenter<V : CategoriesMvpView> : MvpPresenter<V> {
    fun requestCategories()
}