package net.mfilm.ui.manga_info

import android.support.v4.app.Fragment
import android.view.View
import net.mfilm.ui.base.MvpView

/**
 * Created by tusi on 5/18/17.
 */
interface MangaInfoMvpView : MvpView {
    //    var manga: Manga
    fun initMangaInfoHeader()

    fun initIBus()
    fun toggleFav(): Boolean
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
    //    fun onChapterClicked(chapter: Chapter)
    fun onReadBtnClicked()
}