package net.mfilm.ui.manga_info

import net.mfilm.data.network_retrofit.Manga
import net.mfilm.ui.base.MvpPresenter

/**
 * Created by tusi on 5/18/17.
 */
interface MangaInfoMvpPresenter<V : MangaInfoMvpView> : MvpPresenter<V> {
    fun initIBus()
    fun onTaps(tapCount: Int)
    fun toggleFav(manga: Manga): Boolean
}