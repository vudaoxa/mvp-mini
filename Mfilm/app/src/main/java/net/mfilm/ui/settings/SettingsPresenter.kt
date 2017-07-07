package net.mfilm.ui.settings

import io.reactivex.disposables.CompositeDisposable
import net.mfilm.data.DataManager
import net.mfilm.data.prefs.MangaSources
import net.mfilm.ui.base.BasePresenter
import net.mfilm.utils.mangaSources
import javax.inject.Inject

/**
 * Created by MRVU on 7/7/2017.
 */
class SettingsPresenter<V : SettingsMvpView> @Inject
constructor(dataManager: DataManager, compositeDisposable: CompositeDisposable)
    : BasePresenter<V>(dataManager, compositeDisposable), SettingsMvpPresenter<V> {
    override val mMangaSources: MangaSources?
        get() = mangaSources
    override fun requestSettings() {
        initMangaSources()
    }

    override fun initMangaSources() {
        mMangaSources?.run {
            arrange(dataManager.mangaSourceIndex)
            mvpView?.onMangaSourcesResponse(this)
        }
    }


    override fun onMangaSourceSelected(position: Int) {

    }

    override fun onToggleHistoryEnabled() {

    }

    override fun onToggleSearchHistoryEnabled() {

    }
}