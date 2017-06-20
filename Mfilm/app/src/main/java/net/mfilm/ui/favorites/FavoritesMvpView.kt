package net.mfilm.ui.favorites

import net.mfilm.data.db.models.MangaRealm
import net.mfilm.ui.base.MvpView
import net.mfilm.utils.ICallbackOnClick

/**
 * Created by MRVU on 6/20/2017.
 */
interface FavoritesMvpView : MvpView, ICallbackOnClick {
    fun requestFavorites()
    fun onFavoritesResponse(mangaRealms: List<MangaRealm>)
    fun onFavoritesNull()
    fun buildFavorites()
}