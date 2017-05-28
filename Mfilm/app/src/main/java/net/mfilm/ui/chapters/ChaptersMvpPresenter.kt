package net.mfilm.ui.chapters

import net.mfilm.ui.base.MvpPresenter

/**
 * Created by tusi on 5/27/17.
 */
interface ChaptersMvpPresenter<V : ChaptersMvpView> : MvpPresenter<V> {
    fun requestChapters(mangaId: Int, limit: Int, page: Int)
}