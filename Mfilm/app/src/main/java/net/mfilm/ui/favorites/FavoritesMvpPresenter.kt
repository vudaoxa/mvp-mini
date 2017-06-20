package net.mfilm.ui.favorites

import net.mfilm.ui.base.MvpPresenter

/**
 * Created by MRVU on 6/20/2017.
 */
interface FavoritesMvpPresenter<V : FavoritesMvpView> : MvpPresenter<V> {
    fun requestFavorites()
}