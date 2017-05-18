package net.mfilm.ui.mangas

import net.mfilm.data.network_retrofit.Manga
import net.mfilm.data.network_retrofit.MangasResponse
import net.mfilm.ui.base.MvpView
import net.mfilm.utils.ICallbackOnClick

/**
 * Created by Dieu on 09/03/2017.
 */

interface MangasMVPView : MvpView, ICallbackOnClick {
    fun requestMangas()
    fun onMangasResponse(mangasResponse: MangasResponse?)
    fun onMangasNull()
    fun initMangas(mangas: List<Manga>)
}