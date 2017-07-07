package net.mfilm.ui.settings

import net.mfilm.data.prefs.MangaSources
import net.mfilm.ui.base.MvpView
import net.mfilm.utils.ICallbackOnClick

/**
 * Created by MRVU on 7/7/2017.
 */
interface SettingsMvpView : MvpView, ICallbackOnClick {
    fun requestSettings()
    fun onMangaSourcesResponse(mangaSources: MangaSources)
}