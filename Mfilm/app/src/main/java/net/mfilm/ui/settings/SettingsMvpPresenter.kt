package net.mfilm.ui.settings

import net.mfilm.ui.base.MvpPresenter

/**
 * Created by MRVU on 7/7/2017.
 */
interface SettingsMvpPresenter<V : SettingsMvpView> : MvpPresenter<V> {
    fun requestSettings()
    fun initMangaSources()
    fun initSwitchItems()
    fun onMangaSourceSelected(position: Int)
    fun onToggleHistoryEnabled()
    fun onToggleSearchHistoryEnabled()
}