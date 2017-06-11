package net.mfilm.ui.chapter_images

import net.mfilm.data.network_retrofit.Chapter
import net.mfilm.data.network_retrofit.ChapterImage
import net.mfilm.data.network_retrofit.ChapterImagesResponse
import net.mfilm.ui.base.MvpView

/**
 * Created by tusi on 5/29/17.
 */
interface ChapterImagesMvpView : MvpView {
    fun requestChapterImages(chapterId: Int)
    fun onChapterImagesResponse(chapterImagesResponse: ChapterImagesResponse?)
    fun onChapterImagesNull()
    fun initChapterImages(images: List<ChapterImage>)
    //    val seekPrevChapter: Chapter?
//    val seekNextChapter: Chapter?
    fun loadPrevOnDemand()
    fun loadMoreOnDemand()
    //    fun onChaptersResponse()
//    var chapters: MutableList<Chapter>
    fun addChapter(chapter: Chapter, f: () -> Unit)
    fun seekNextChapter()
    fun seekPrevChapter()

}