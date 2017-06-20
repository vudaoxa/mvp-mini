package net.mfilm.ui.mangas

import net.mfilm.data.network_retrofit.Manga
import net.mfilm.data.network_retrofit.MangasResponse
import net.mfilm.ui.base.MvpView
import net.mfilm.ui.manga.AdapterTracker
import net.mfilm.utils.ICallbackOnClick
import net.mfilm.utils.ICallbackRefresh
import net.mfilm.utils.ICallbackSpanCount

/**
 * Created by Dieu on 09/03/2017.
 */

interface MangasMvpView : MvpView, ICallbackOnClick, ICallbackRefresh, ICallbackSpanCount {
    val spnFilterTracker: AdapterTracker
    fun initRv()
    fun initSpnFilters()
    fun requestMangas()
    fun onMangasResponse(mangasResponse: MangasResponse?)
    fun onMangasNull()
    fun buildMangas(mangas: List<Manga>)
}
