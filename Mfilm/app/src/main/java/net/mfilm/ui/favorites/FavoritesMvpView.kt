package net.mfilm.ui.favorites

import net.mfilm.data.db.models.MangaFavoriteRealm
import net.mfilm.ui.base.realm.RealmMvpView

/**
 * Created by MRVU on 6/20/2017.
 */
interface FavoritesMvpView : RealmMvpView<MangaFavoriteRealm> {
    fun requestFavorites()
    fun onFavoritesResponse(mangaFavoriteRealms: List<MangaFavoriteRealm>?)
    fun onFavoritesNull()
    fun buildFavorites(mangaFavoriteRealms: List<MangaFavoriteRealm>)
    fun buildFavoritesFilter(mangaFavoriteRealms: List<MangaFavoriteRealm>)
}