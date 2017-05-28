package net.mfilm.data.network_retrofit

import io.reactivex.Observable
import net.mfilm.data.network_retrofit.support.NetConstants
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by tusi on 4/2/17.
 */
interface ApisService {
    @GET(NetConstants.API_CATEGORIES)
    fun requestCategories(): Observable<CategoriesResponse>

    @GET(NetConstants.API_MANGA)
    fun requestMangas(@Query("category") category: Int?, @Query("limit") limit: Int, @Query("page") page: Int
                      , @Query("sort") sort: String, @Query("search") search: String?): Observable<MangasResponse>

    //not use
    @GET(NetConstants.API_MANGA_DETAIL)
    fun requestMangaDetail(@Path("id") id: Int): Observable<MangaDetailResponse>

    @GET(NetConstants.API_CHAPTERS)
    fun requestChapters(@Query("manga") mangaId: Int, @Query("limit") limit: Int, @Query("page") page: Int): Observable<ChaptersResponse>

    @GET(NetConstants.API_CHAPTER_DETAIL)
    fun requestChapterDetail(@Path("id") id: Int): Observable<ChapterDetailResponse>

    @GET(NetConstants.API_CHAPTER_IMAGES)
    fun requestChapterImages(@Query("chapter") chapter: Int): Observable<ChapterImagesResponse>
}