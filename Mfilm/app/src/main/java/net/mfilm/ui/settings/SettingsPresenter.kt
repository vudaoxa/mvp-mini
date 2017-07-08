package net.mfilm.ui.settings

import io.reactivex.disposables.CompositeDisposable
import net.mfilm.R
import net.mfilm.data.DataManager
import net.mfilm.data.prefs.SwitchItem
import net.mfilm.ui.base.BasePresenter
import javax.inject.Inject

/**
 * Created by MRVU on 7/7/2017.
 */
class SettingsPresenter<V : SettingsMvpView> @Inject
constructor(dataManager: DataManager, compositeDisposable: CompositeDisposable)
    : BasePresenter<V>(dataManager, compositeDisposable), SettingsMvpPresenter<V> {
    override fun requestSettings() {
        initMangaSources()
        initSwitchItems()
    }

    override fun initMangaSources() {
        mMangaSources?.run {
            arrange(dataManager.mangaSourceIndex)
            mvpView?.onMangaSourcesResponse(this)
        }
    }

    override fun initSwitchItems() {
        val switchItems = listOf<SwitchItem>(SwitchItem(R.string.enable_history, dataManager.historyEnabled),
                SwitchItem(R.string.enable_search_history, dataManager.searchHistoryEnabled))
        mvpView?.onSwitchItemsResponse(switchItems)
    }
    override fun onMangaSourceSelected(position: Int) {
        dataManager.mangaSourceIndex = position
    }

    override fun onToggleHistoryEnabled() {
        dataManager.run {
            historyEnabled = !historyEnabled
        }
    }

    override fun onToggleSearchHistoryEnabled() {
        dataManager.run {
            searchHistoryEnabled = !searchHistoryEnabled
        }
    }
}