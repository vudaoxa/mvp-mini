package net.mfilm.ui.favorites

import net.mfilm.data.db.models.MangaFavoriteRealm
import net.mfilm.ui.base.MvpPresenter
import net.mfilm.ui.base.realm.RealmMvpPreseneter
import net.mfilm.utils.ICallbackBus

/**
 * Created by MRVU on 6/20/2017.
 */
interface FavoritesMvpPresenter<V : FavoritesMvpView> : MvpPresenter<V>, RealmMvpPreseneter, ICallbackBus {
    fun requestFavorites()
    fun toggleFav(mangaFavoriteRealms: List<MangaFavoriteRealm>)
}