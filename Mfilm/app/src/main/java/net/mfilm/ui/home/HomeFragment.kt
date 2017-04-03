package net.mfilm.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        return inflater!!.inflate(R.layout.fragment_scrolling, container, false)
    }

    override fun initView() {
    }

    override fun initField() {
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