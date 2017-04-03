package net.mfilm.ui.videos.base

import android.support.v4.app.Fragment
import io.reactivex.disposables.CompositeDisposable
import net.mfilm.data.DataMng
import net.mfilm.ui.base.BasePresenter
import net.mfilm.ui.videos.VideoInfoFragment
import net.mfilm.ui.videos.VideoPlayerFragment
import net.mfilm.ui.videos.listener.ICallbackPlayerView
import net.mfilm.ui.videos.listener.ICallbackVideoChange
import net.mfilm.ui.videos.listener.ICallbackVideoInfoChange
import net.mfilm.ui.videos.model.MPlayer
import javax.inject.Inject

/**
 * Created by Dieu on 16/03/2017.
 */

class VideoPresenter<V : VideoMvpView> @Inject
constructor(dataManager: DataMng, compositeDisposable: CompositeDisposable)
    : BasePresenter<V>(dataManager, compositeDisposable), VideoMvpPresenter<V> {
    private var mCallbackVideoChange: ICallbackVideoChange? = null
    private var mCallbackVideoInfoChange: ICallbackVideoInfoChange? = null
    private var mCallbackPlayerView: ICallbackPlayerView? = null

    override fun onPlayVideo(mPlayer: MPlayer) {
        mPlayer.apply {
            if (mCallbackVideoChange == null) {
                mCallbackVideoChange = VideoPlayerFragment.newInstance(this)
                mCallbackVideoInfoChange = VideoInfoFragment.newInstance(this)
                initPlayerViewListener()
                mCallbackVideoChange!!.onSetCallbackPlayerView(mCallbackPlayerView!!)
                mvpView!!.updateVideoView((mCallbackVideoChange as Fragment?)!!, (mCallbackVideoInfoChange as Fragment?)!!)
            } else {
                mCallbackVideoChange!!.onChangeVideo(this)
                mCallbackVideoInfoChange!!.onChangeVideo(this)
                mvpView!!.onShowVideo(false)
            }
        }
    }

    private fun initPlayerViewListener() {
        mCallbackPlayerView = object : ICallbackPlayerView {
            override fun requestToggleFullScreen() {
                if (isViewAttached) {
                    mvpView!!.onToggleFullScreen()
                }
            }

            override fun resetAutoRotationScreen() {
                if (isViewAttached) {
                    mvpView!!.resetAutoRotationScreen()
                }
            }
        }
    }

    override fun onCloseVideo() {
        if (isViewAttached) {
            if (mCallbackVideoChange != null) {
                mCallbackVideoChange!!.onStopVideo()
            }
        }
    }

    override fun onSetFullScreenDone(isFullScreen: Boolean) {
        mCallbackVideoChange!!.onRequestFullScreenDone(isFullScreen)
    }
}
