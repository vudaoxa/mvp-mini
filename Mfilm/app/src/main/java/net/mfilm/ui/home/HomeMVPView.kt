package net.mfilm.ui.home

import net.mfilm.data.network_retrofit.Manga
import net.mfilm.data.network_retrofit.MangasResponse
import net.mfilm.ui.base.MvpView

/**
 * Created by Dieu on 09/03/2017.
 */

interface HomeMVPView : MvpView {
    fun requestMangas()
    fun onMangasResponse(mangasResponse: MangasResponse?)
    fun onMangasNull()
    fun initMangas(mangas: List<Manga>)
}
