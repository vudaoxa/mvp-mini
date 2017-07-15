package net.mfilm.ui.chapters

import net.mfilm.data.db.models.ChapterRealm
import net.mfilm.data.db.models.MangaHistoryRealm
import net.mfilm.data.network_retrofit.Chapter
import net.mfilm.data.network_retrofit.ChaptersResponse
import net.mfilm.ui.base.MvpView
import net.mfilm.ui.chapter_images.ChapterImagesMvpView
import net.mfilm.utils.ICallbackOnClick
import net.mfilm.utils.ICallbackRv

/**
 * Created by tusi on 5/27/17.
 */
interface ChaptersMvpView : MvpView, ICallbackOnClick, ICallbackRv {
    fun initBtnRead()
    fun requestMangaHistory()
    fun requestChaptersHistory()
    fun onMangaHistoryResponse(mangaHistoryRealm: MangaHistoryRealm)
    fun onChaptersRealmResponse(chaptersRealm: List<ChapterRealm>? = null)
    fun saveMangaHistory()
    fun onReadBtnClicked()
    fun requestChapters()
    fun onChaptersResponse(chaptersResponse: ChaptersResponse?)
    fun onChaptersNull()

    fun obtainChapterPagingState(prevPageUrl: String? = null, nextPageUrl: String? = null, chapters: List<Chapter>)
    fun buildChapters(chapters: List<Chapter>)
    var currentReadingPosition: Int?
    var prevPosition: Int?
    var nextPosition: Int?
    var currentReadingChapter: Chapter?
    var prevChapter: Chapter?
    var nextChapter: Chapter?
    val chapters: List<Chapter>?
    fun seekCurrentReadingPosition(newPosition: Int?)
    fun seekNextChapter()
    fun seekPrevChapter()
    fun loadMoreOnDemand()
    fun loadMoreOnDemand(chapterImagesMvpView: ChapterImagesMvpView)
    fun loadPrevOnDemand(chapterImagesMvpView: ChapterImagesMvpView)
    //    var chapterImagesFragment: ChapterImagesFragment?
    var chapterImagesFragment: ChapterImagesMvpView?
//    fun onResumeReading()

}