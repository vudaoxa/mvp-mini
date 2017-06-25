package net.mfilm.ui.manga_info

import net.mfilm.data.network_retrofit.Manga
import net.mfilm.ui.base.MvpPresenter
import net.mfilm.utils.ICallbackBus

/**
 * Created by tusi on 5/18/17.
 */
interface MangaInfoMvpPresenter<V : MangaInfoMvpView> : MvpPresenter<V>, ICallbackBus {
    fun requestManga(id: Int)
    fun isFavorite(id: Int)
    fun saveHistory(manga: Manga)
    fun onTaps(tapCount: Int)
    fun toggleFav(manga: Manga): Boolean
}