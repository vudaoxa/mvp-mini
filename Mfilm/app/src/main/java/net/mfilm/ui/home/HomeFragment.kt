package net.mfilm.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.fragment_home.*
import net.mfilm.R
import net.mfilm.ui.base.stack.BaseStackFragment
import javax.inject.Inject

/**
 * Created by tusi on 4/2/17.
 */
class HomeFragment : BaseStackFragment(), HomeMVPView {
    @Inject
    lateinit var mPresenter: HomeMvpPresenter<HomeMVPView>

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_home, container, false)
    }

    override fun initViews() {
        buildSpnBanks(listOf(R.string.az, R.string.hottest, R.string.newest))
    }

    override fun initFields() {
    }

    fun buildSpnBanks(banks: List<Int>) {
        val banksAdapter = ArrayAdapter(activity, R.layout.item_spn_filter, banks.map { getString(it) })
//        banksAdapter.setDropDownViewResource(R.layout.row_spn_dropdown)
        spn_filter.setAdapter(banksAdapter)
    }
    companion object {
        fun newInstance(): HomeFragment {
            val args = Bundle()
            val fragment = HomeFragment()
            fragment.arguments = args
            return fragment
        }
    }
}