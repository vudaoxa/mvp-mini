package net.mfilm.ui.settings

import net.mfilm.R
import net.mfilm.data.prefs.MangaSources
import net.mfilm.ui.base.stack.BaseStackFragment
import javax.inject.Inject

/**
 * Created by MRVU on 7/7/2017.
 */
class SettingsFragment : BaseStackFragment(), SettingsMvpView {
    companion object {
        fun newInstance(): SettingsFragment {
            val fragment = SettingsFragment()
            return fragment
        }
    }

    @Inject
    lateinit var mSettingsPresenter: SettingsMvpPresenter<SettingsMvpView>

    override fun initFields() {
        tryIt {
            activityComponent?.inject(this)
            mSettingsPresenter.onAttach(this)
        }
        back = true
        title = getString(R.string.action_settings)
    }

    override fun initViews() {
        requestSettings()
    }

    override fun requestSettings() {
        mSettingsPresenter.requestSettings()
    }

    override fun onMangaSourcesResponse(mangaSources: MangaSources) {

    }

    override fun onClick(position: Int, event: Int) {

    }
}