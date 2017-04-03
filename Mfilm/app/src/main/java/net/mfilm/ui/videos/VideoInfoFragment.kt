package net.mfilm.ui.videos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import net.mfilm.R
import net.mfilm.ui.base.stack.BaseStackFragment
import net.mfilm.ui.videos.listener.ICallbackVideoInfoChange
import net.mfilm.ui.videos.model.MPlayer
import net.mfilm.utils.AppConstants

/**
 * Created by Dieu on 15/03/2017.
 */

class VideoInfoFragment : BaseStackFragment(), ICallbackVideoInfoChange {

    private var mPlayer: MPlayer? = null


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.vfragment_info_video, container, false)
    }

    override fun initView() {


    }

    override fun initField() {
        mPlayer = arguments.getSerializable(AppConstants.EXTRA_DATA) as MPlayer
    }

    override fun onChangeVideo(mPlayer: MPlayer) {

    }

    companion object {
        fun newInstance(mPlayer: MPlayer): VideoInfoFragment {
            val args = Bundle()
            args.putSerializable(AppConstants.EXTRA_DATA, mPlayer)
            val fragment = VideoInfoFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
