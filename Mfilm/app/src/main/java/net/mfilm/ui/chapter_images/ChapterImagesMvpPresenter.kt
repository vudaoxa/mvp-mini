package net.mfilm.ui.chapter_images

import net.mfilm.ui.base.MvpPresenter

/**
 * Created by tusi on 5/29/17.
 */
interface ChapterImagesMvpPresenter<V : ChapterImagesMvpView> : MvpPresenter<V> {
    fun requestChapterImages(chapterId: Int)
}