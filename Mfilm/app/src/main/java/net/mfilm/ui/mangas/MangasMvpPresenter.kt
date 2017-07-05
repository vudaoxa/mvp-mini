package net.mfilm.ui.mangas

import net.mfilm.ui.base.MvpPresenter
import net.mfilm.utils.ICallbackToggleFav

/**
 * Created by Dieu on 09/03/2017.
 */

interface MangasMvpPresenter<V : MangasMvpView> : MvpPresenter<V>, ICallbackToggleFav {
    fun requestMangas(category: Int?, limit: Int, page: Int
                      , sort: String, search: String?)
}
