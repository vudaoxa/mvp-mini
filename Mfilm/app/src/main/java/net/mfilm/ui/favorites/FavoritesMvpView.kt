package net.mfilm.ui.favorites

import net.mfilm.data.db.models.MangaRealm
import net.mfilm.ui.base.MvpView
import net.mfilm.ui.manga.AdapterTracker
import net.mfilm.utils.ICallbackOnClick
import net.mfilm.utils.ICallbackSpanCount

/**
 * Created by MRVU on 6/20/2017.
 */
interface FavoritesMvpView : MvpView, ICallbackOnClick, ICallbackSpanCount {
    val spnFilterTracker: AdapterTracker
    fun initRv()
    fun initSpnFilters()
    fun requestFavorites()
    fun onFavoritesResponse(mangaRealms: List<MangaRealm>?)
    fun onFavoritesNull()
    fun buildFavorites(mangaRealms: List<MangaRealm>)
}