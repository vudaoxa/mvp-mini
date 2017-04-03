/*
 * Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://mindorks.com/license/apache-v2
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package net.mfilm.ui.videos.base

import net.mfilm.di.PerActivity
import net.mfilm.ui.base.MvpPresenter
import net.mfilm.ui.videos.model.MPlayer

/**
 * Created by janisharali on 27/01/17.
 */
@PerActivity
interface VideoMvpPresenter<V : VideoMvpView> : MvpPresenter<V> {
    fun onPlayVideo(mPlayer: MPlayer)
    fun onCloseVideo()
    fun onSetFullScreenDone(isFullScreen: Boolean)
}
