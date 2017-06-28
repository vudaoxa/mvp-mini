package net.mfilm.ui.favorites

import net.mfilm.data.db.models.MangaFavoriteRealm
import net.mfilm.ui.base.MvpPresenter
import net.mfilm.utils.ICallbackBus

/**
 * Created by MRVU on 6/20/2017.
 */
interface FavoritesMvpPresenter<V : FavoritesMvpView> : MvpPresenter<V>, ICallbackBus {
    fun requestFavorites()
    fun toggleFav(mangaFavoriteRealms: List<MangaFavoriteRealm>)
}