package net.mfilm.ui.chapters

import net.mfilm.data.network_retrofit.Manga
import net.mfilm.ui.base.MvpPresenter

/**
 * Created by tusi on 5/27/17.
 */
interface ChaptersMvpPresenter<V : ChaptersMvpView> : MvpPresenter<V> {
    fun saveMangaHistory(manga: Manga)
    fun requestChapters(mangaId: Int, limit: Int, page: Int)
    fun requestMangaHistory(id: Int)
    fun requestChaptersHistory(id: Int)
}