package net.mfilm.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_settings.*
import net.mfilm.R
import net.mfilm.data.prefs.MangaSources
import net.mfilm.data.prefs.SwitchItem
import net.mfilm.ui.base.rv.holders.TYPE_ITEM
import net.mfilm.ui.base.rv.holders.TYPE_ITEM_HEADER
import net.mfilm.ui.base.rv.wrappers.LinearLayoutManagerWrapper
import net.mfilm.ui.base.stack.BaseStackFragment
import net.mfilm.ui.settings.rv.adapters.SettingsRvAdapter
import net.mfilm.utils.tryOrExit
import timber.log.Timber
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
    var mSettingRvAdapter: SettingsRvAdapter<Any?>? = null
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_settings, container, false)
    }
    override fun initFields() {
        tryOrExit {
            activityComponent?.inject(this)
            mSettingsPresenter.onAttach(this)
        }
        back = true
        title = getString(R.string.action_settings)
    }

    override fun initViews() {
        initRv()
        requestSettings()
    }

    override fun initRv() {
        rv.layoutManager = LinearLayoutManagerWrapper(context)
    }
    override fun requestSettings() {
        mSettingsPresenter.requestSettings()
    }

    override fun onMangaSourcesResponse(mangaSources: MangaSources) {
        mSettingRvAdapter?.run {

        } ?: let {
            val settingItems = mutableListOf<Any?>()
            settingItems.add(mangaSources)
            mSettingRvAdapter = SettingsRvAdapter(context, settingItems, this)
            rv.adapter = mSettingRvAdapter
        }
    }

    override fun onSwitchItemsResponse(switchItems: List<SwitchItem>) {
        mSettingRvAdapter?.run {
            addAll(switchItems)
            notifyDataSetChanged()
        } ?: let {

        }
    }
    override fun onClick(position: Int, event: Int) {
        Timber.e("---------------------onClick--------------------$position")
        when (event) {
            TYPE_ITEM_HEADER -> {
                mSettingsPresenter.onMangaSourceSelected(position)
            }
            TYPE_ITEM -> {
                when (position) {
                    1 -> mSettingsPresenter.onToggleHistoryEnabled()
                    2 -> mSettingsPresenter.onToggleSearchHistoryEnabled()
                }
            }
        }
    }
}