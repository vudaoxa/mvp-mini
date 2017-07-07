package net.mfilm.ui.settings

import io.reactivex.disposables.CompositeDisposable
import net.mfilm.R
import net.mfilm.data.DataManager
import net.mfilm.data.prefs.MangaSource
import net.mfilm.data.prefs.MangaSources
import net.mfilm.ui.base.BasePresenter
import javax.inject.Inject

/**
 * Created by MRVU on 7/7/2017.
 */
class SettingsPresenter<V : SettingsMvpView> @Inject
constructor(dataManager: DataManager, compositeDisposable: CompositeDisposable)
    : BasePresenter<V>(dataManager, compositeDisposable), SettingsMvpPresenter<V> {
    override fun requestSettings() {

    }

    override fun initMangaSources() {
        val vn = MangaSource(R.string.title_vn)
        val en = MangaSource(R.string.title_en)
        mvpView?.onMangaSourcesResponse(MangaSources(listOf(vn, en), dataManager.mangaSourceIndex))
    }

    override fun onMangaSourceSelected(position: Int) {

    }

    override fun onToggleHistoryEnabled() {

    }

    override fun onToggleSearchHistoryEnabled() {

    }
}