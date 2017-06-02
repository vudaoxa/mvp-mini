package net.mfilm.ui.chapter_images

import android.content.Context
import net.mfilm.data.network_retrofit.Chapter
import net.mfilm.ui.base.MvpPresenter

/**
 * Created by tusi on 5/29/17.
 */
interface ChapterImagesMvpPresenter<V : ChapterImagesMvpView> : MvpPresenter<V> {
    fun requestChapterImages(chapterId: Int)
    fun showFresco(context: Context, chapter: Chapter, list: MutableList<String>, startPosition: Int = 0)
    fun loadMore()
}