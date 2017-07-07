package net.mfilm.ui.settings

import net.mfilm.data.prefs.MangaSources
import net.mfilm.ui.base.MvpPresenter

/**
 * Created by MRVU on 7/7/2017.
 */
interface SettingsMvpPresenter<V : SettingsMvpView> : MvpPresenter<V> {
    val mMangaSources: MangaSources?
    fun requestSettings()
    fun initMangaSources()
    fun onMangaSourceSelected(position: Int)
    fun onToggleHistoryEnabled()
    fun onToggleSearchHistoryEnabled()
}