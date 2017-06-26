package net.mfilm.ui.favorites

import net.mfilm.data.db.models.MangaFavoriteRealm
import net.mfilm.ui.base.MvpView
import net.mfilm.ui.manga.AdapterTracker
import net.mfilm.utils.ICallbackEmptyDataView
import net.mfilm.utils.ICallbackOnClick
import net.mfilm.utils.ICallbackSpanCount

/**
 * Created by MRVU on 6/20/2017.
 */
interface FavoritesMvpView : MvpView, ICallbackOnClick, ICallbackSpanCount, ICallbackEmptyDataView {
    val spnFilterTracker: AdapterTracker
    fun initRv()
    fun initSpnFilters()
    fun requestFavorites()
    fun onFavoritesResponse(mangaFavoriteRealms: List<MangaFavoriteRealm>?)
    fun onFavoritesNull()
    fun buildFavorites(mangaFavoriteRealms: List<MangaFavoriteRealm>)
}