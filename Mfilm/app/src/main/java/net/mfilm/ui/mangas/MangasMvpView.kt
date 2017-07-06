package net.mfilm.ui.mangas

import android.view.View
import net.mfilm.data.network_retrofit.Manga
import net.mfilm.data.network_retrofit.MangasResponse
import net.mfilm.ui.base.MvpView
import net.mfilm.ui.search_history.SearchHistoryMvpView
import net.mfilm.utils.*

/**
 * Created by Dieu on 09/03/2017.
 */

interface MangasMvpView : MvpView, ICallbackOnClick, ICallbackOnLongClick,
        ICallbackRefresh, ICallbackSpanCount, ICallbackSort, ICallbackDialogPlus,
        ICallbackDialogItemClicked, ICallbackFavInput, ICallbackSearchView, ICallbackSearchHistoryContainer {
    val mSearchHistoryView: SearchHistoryMvpView
    fun initRv()
    fun requestMangas()
    fun onMangasResponse(mangasResponse: MangasResponse?)
    fun onMangasNull()
    fun buildMangas(mangas: List<Manga>)
}

interface ICallbackSearchHistoryContainer {
    fun attachSearchHistoryFragment()
    val searchHistoryContainerView: View
    val searchHistoryContainerId: Int
}