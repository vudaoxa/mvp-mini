package net.mfilm.ui.filmy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.layout_full_read.*
import net.mfilm.R
import net.mfilm.data.network_retrofit.Manga
import net.mfilm.ui.animations.RevealAnimation
import net.mfilm.ui.base.stack.BaseStackFragment
import net.mfilm.utils.AppConstants
import net.mfilm.utils.handler
import net.mfilm.utils.icon_close
import java.io.Serializable

class FullReadFragment : BaseStackFragment() {
    companion object {
        fun newInstance(manga: Any?): FullReadFragment {
            val fragment = FullReadFragment()
            val bundle = Bundle()
            bundle.putSerializable(AppConstants.EXTRA_DATA, manga as? Serializable)
            fragment.arguments = bundle
            return fragment
        }
    }

    var manga: Manga? = null
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.layout_full_read, container, false)
    }

    override fun initFields() {
        fullScreen = true
        manga = arguments.getSerializable(AppConstants.EXTRA_DATA) as? Manga?
    }

    override fun initViews() {
        manga?.apply {
            tv_Title.text = name
            tv_des.text = summary
        }
        cross.apply {
            setImageDrawable(icon_close)
            setOnClickListener {
                handler({ baseActivity?.onBackPressed() })
            }
        }
        RevealAnimation.performReveal(all_details_container)
    }
}
