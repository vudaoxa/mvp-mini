package net.mfilm.ui.favorites

import net.mfilm.data.db.models.MangaFavoriteRealm
import net.mfilm.ui.base.MvpView
import net.mfilm.ui.manga.AdapterTracker
import net.mfilm.utils.*

/**
 * Created by MRVU on 6/20/2017.
 */
interface FavoritesMvpView : MvpView, ICallbackOnClick,
        ICallbackSpanCount, ICallbackEmptyDataViewHolder, ICallbackReceiveOptionsMenu,
        ICallbackFilter, ICallbackLocalSearch, ICallbackEdit, ICallbackSort, ICallbackOnLongClick {
    fun buildMangaFavoritesRealmFilter(objs: List<MangaFavoriteRealm>)
    val spnFilterTracker: AdapterTracker
    fun initRv()
    fun initSpnFilters()
    fun requestFavorites()
    fun onFavoritesResponse(mangaFavoriteRealms: List<MangaFavoriteRealm>?)
    fun onFavoritesNull()
    fun buildFavorites(mangaFavoriteRealms: List<MangaFavoriteRealm>)
}