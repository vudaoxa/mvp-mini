package net.mfilm.ui.manga_info

import net.mfilm.data.network_retrofit.Manga
import net.mfilm.ui.base.MvpPresenter
import net.mfilm.utils.ICallbackBus
import net.mfilm.utils.ICallbackToggleFav

/**
 * Created by tusi on 5/18/17.
 */
interface MangaInfoMvpPresenter<V : MangaInfoMvpView> : MvpPresenter<V>,
        ICallbackBus, ICallbackToggleFav {
    fun requestManga(id: Int)
    fun isFavorite(id: Int)
    fun saveHistory(manga: Manga)
    fun onTaps(tapCount: Int)
}