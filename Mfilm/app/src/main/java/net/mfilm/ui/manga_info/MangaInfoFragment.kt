package net.mfilm.ui.manga_info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import net.mfilm.ui.base.stack.BaseStackFragment

/**
 * Created by tusi on 5/18/17.
 */
class MangaInfoFragment : BaseStackFragment(), MangaInfoMvpView {
    companion object {
        fun newInstance(): MangaInfoFragment {
            val fragment = MangaInfoFragment()
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(net.mfilm.R.layout.fragment_manga_info, container, false)
    }

    override fun initFields() {

    }

    override fun initViews() {

    }
}