package net.mfilm.ui.chapter_images

import net.mfilm.data.network_retrofit.Chapter
import net.mfilm.data.network_retrofit.ChapterImage
import net.mfilm.data.network_retrofit.ChapterImagesResponse
import net.mfilm.ui.base.MvpView
import net.mfilm.utils.*

/**
 * Created by tusi on 5/29/17.
 */
interface ChapterImagesMvpView : MvpView, ICallbackRv,
        ICallbackOnClick, ICallbackRvFailure, ICallbackPageChange,
        ICallbackViewContinue, ICallbackWebtoon, ICallbackPreview {
    fun saveHistoryChapter(chapter: Chapter)
    fun saveReadingPage(chapter: Chapter, page: Int)
    fun initPagingState(chapter: Chapter)
    fun requestChapterImages()
    fun initHeader()
    fun requestChapterImages(chapterId: Int)
    fun onChapterImagesResponse(chapterImagesResponse: ChapterImagesResponse?)
    fun onChapterImagesNull()
    fun buildChapterImages(images: List<ChapterImage>)
    fun loadPrevOnDemand()
    fun loadMoreOnDemand()
    fun seekNextChapter()
    fun seekPrevChapter()
    fun onBitmapSizeResponse(webtoon: Boolean)
}