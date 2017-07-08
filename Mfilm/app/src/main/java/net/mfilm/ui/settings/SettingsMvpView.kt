package net.mfilm.ui.settings

import net.mfilm.data.prefs.MangaSources
import net.mfilm.data.prefs.SwitchItem
import net.mfilm.ui.base.MvpView
import net.mfilm.utils.ICallbackOnClick
import net.mfilm.utils.ICallbackRv

/**
 * Created by MRVU on 7/7/2017.
 */
interface SettingsMvpView : MvpView, ICallbackOnClick, ICallbackRv {
    fun requestSettings()
    fun onMangaSourcesResponse(mangaSources: MangaSources)
    fun onSwitchItemsResponse(switchItems: List<SwitchItem>)
}