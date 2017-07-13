package net.mfilm.ui.manga_info

import android.support.v4.app.Fragment
import android.view.View
import net.mfilm.data.network_retrofit.MangaDetailResponse
import net.mfilm.ui.base.MvpView

/**
 * Created by tusi on 5/18/17.
 */
interface MangaInfoMvpView : MvpView {
    fun obtainManga()
    fun buildManga()
    fun onMangaDetailResponse(mangaDetailResponse: MangaDetailResponse?)
    fun onMangaNull()
    fun initMangaInfoHeader()
    fun isFavorite()
    fun toggleFav(): Boolean
    //    fun saveHistory()
    fun requestManga(id: Int)
    fun viewFullRead()
    fun attachChaptersFragment()

    fun attachThumbsFragment()
    fun attachRelatedMangasFragment()
    fun obtainChaptersFragment(): Fragment?
    fun obtainThumbsFragment(): Fragment?
    fun obtainRelatedMangasFragment(): Fragment?
    val chaptersContainerView: View
    val chaptersContainerId: Int
    val thumbsContainerView: View
    val thumbsContainerId: Int
    val relatedMangasContainerView: View
    val relatedMangasContainerId: Int
//    fun onReadBtnClicked()
}

