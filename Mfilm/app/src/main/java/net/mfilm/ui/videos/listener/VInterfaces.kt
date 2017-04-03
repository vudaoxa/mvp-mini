package net.mfilm.ui.videos.listener

import net.mfilm.ui.videos.model.MPlayer

/**
 * Created by tusi on 3/28/17.
 */
interface ICallbackPlayerView {
    fun requestToggleFullScreen()
    fun resetAutoRotationScreen()
}

interface ICallbackVideoChange {
    fun onChangeVideo(mPlayer: MPlayer)
    fun onPauseVideo()
    fun onStopVideo()
    fun onSetCallbackPlayerView(mCallbackPlayerView: ICallbackPlayerView)
    fun onRequestFullScreenDone(isFullScreen: Boolean)
}

interface ICallbackVideoInfoChange {
    fun onChangeVideo(mPlayer: MPlayer)
}