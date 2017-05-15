package net.mfilm.ui.home

import net.mfilm.ui.base.MvpPresenter

/**
 * Created by Dieu on 09/03/2017.
 */

interface HomeMvpPresenter<V : HomeMVPView> : MvpPresenter<V> {
    fun requestMangas(category: Int?, limit: Int, page: Int
                      , sort: String, search: String?)
}
