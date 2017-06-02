package net.mfilm.ui.chapters

import net.mfilm.data.network_retrofit.Chapter
import net.mfilm.data.network_retrofit.ChaptersResponse
import net.mfilm.ui.base.MvpView
import net.mfilm.ui.chapter_images.ChapterImagesMvpView
import net.mfilm.utils.ICallbackOnClick

/**
 * Created by tusi on 5/27/17.
 */
interface ChaptersMvpView : MvpView, ICallbackOnClick {
    fun requestChapters()
    fun onChaptersResponse(chaptersResponse: ChaptersResponse?)
    fun onChaptersNull()
    fun initChapters(chapters: List<Chapter>)
    var currentReadingPosition: Int?
    var prevPosition: Int?
    var nextPosition: Int?
    var currentReadingChapter: Chapter?
    var prevChapter: Chapter?
    var nextChapter: Chapter?
    val chapters: List<Chapter>?
    fun nextChapter()
    fun loadMoreOnDemand()
    fun loadMoreOnDemand(chapterImagesMvpView: ChapterImagesMvpView)
    //    var chapterImagesFragment: ChapterImagesFragment?
    var chapterImagesFragment: ChapterImagesMvpView?
//    fun onResumeReading()
}