package net.mfilm.ui.manga_info

import net.mfilm.data.network_retrofit.Manga
import net.mfilm.ui.base.MvpView

/**
 * Created by tusi on 5/18/17.
 */
interface MangaInfoMvpView : MvpView {
    var manga: Manga
}