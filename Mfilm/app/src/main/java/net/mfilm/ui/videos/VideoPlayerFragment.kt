package net.mfilm.ui.videos

import android.annotation.TargetApi
import android.app.Activity
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.drm.*
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.mediacodec.MediaCodecRenderer
import com.google.android.exoplayer2.mediacodec.MediaCodecUtil
import com.google.android.exoplayer2.source.*
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.source.smoothstreaming.DefaultSsChunkSource
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.MappingTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.DebugTextViewHelper
import com.google.android.exoplayer2.ui.PlaybackControlView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.HttpDataSource
import com.google.android.exoplayer2.util.Util
import com.tieudieu.util.DebugLog
import kotlinx.android.synthetic.main.vfragment_video_player.*
import kotlinx.android.synthetic.main.vlayout_controller_player.*
import net.mfilm.MApplication
import net.mfilm.R
import net.mfilm.ui.videos.listener.ICallbackPlayerView
import net.mfilm.ui.videos.listener.ICallbackVideoChange
import net.mfilm.ui.videos.model.MPlayer
import net.mfilm.ui.videos.player.EventLogger
import net.mfilm.ui.videos.player.TrackSelectionHelper
import net.mfilm.utils.AppConstants
import java.net.CookieHandler
import java.net.CookieManager
import java.net.CookiePolicy
import java.util.*


/**
 * Created by Dieu on 14/02/2017.
 */

class VideoPlayerFragment : Fragment(), View.OnClickListener, ExoPlayer.EventListener, PlaybackControlView.VisibilityListener, ICallbackVideoChange {
    internal val BANDWIDTH_METER = DefaultBandwidthMeter()

    private var mMainHandler: Handler? = null
    private var mEventLogger: EventLogger? = null

    private var mediaaDataSourceFactory: DataSource.Factory? = null
    private var player: SimpleExoPlayer? = null
    private var trackSelector: DefaultTrackSelector? = null
    private var trackSelectionHelper: TrackSelectionHelper? = null
    private var debug_text_viewHelper: DebugTextViewHelper? = null
    private var playerNeedsSource: Boolean = false

    private var shoulAutoPlay = false
    private var resumeWindow = 0
    private var resumePosition = 0L

    private lateinit var mMPlayer: MPlayer
    private lateinit var mCallbackPlayerView: ICallbackPlayerView

    private var isFullScreen = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mMPlayer = arguments.getSerializable(AppConstants.EXTRA_DATA) as MPlayer
        shoulAutoPlay = true
        clearResumePosition()
        mediaaDataSourceFactory = buildDataSourceFactory(true)
        mMainHandler = Handler()
        if (CookieHandler.getDefault() !== DEFAULT_COOKIE_MANAGER) {
            CookieHandler.setDefault(DEFAULT_COOKIE_MANAGER)
        }
        //        exitFullscreen(getActivity());
        if (Build.VERSION.SDK_INT > 10) {
            registerSystemUiVisibility()
        }
    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.vfragment_video_player, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        root_video_view.setOnClickListener(this)
        updateStateFullScreen()
        img_full_screen.setOnClickListener {
            Log.d(TAG, "xyz--img_full_screen-Click")
            mCallbackPlayerView.requestToggleFullScreen()
        }
        player_view.apply {
            setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL)
            setControllerVisibilityListener(this@VideoPlayerFragment)
            requestFocus()
        }
        retry_button.setOnClickListener(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (Util.SDK_INT <= 23) {
            initializePlayer()
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "xyz--onResume--")
        if (Util.SDK_INT <= 23 || player == null) {
            initializePlayer()
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        Log.d(TAG, "xyz--onViewStateRestored--")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "xyz--onPause--")
        if (Util.SDK_INT <= 23) {
            releasePlayer()
        }
    }

    override fun onStop() {
        _handler.removeCallbacks(mRunnable)
        super.onStop()
        Log.d(TAG, "xyz--onStop--")
        if (Util.SDK_INT <= 23) {
            releasePlayer()
        }
    }

    private fun initializePlayer() {
        if (player == null) {
            val preferExtensionDecoders = mMPlayer.isPrerExtensisonDecodes
            val drmSchemeUuid = if (TextUtils.isEmpty(mMPlayer.drmSchemeUuiddExtra)) null else UUID.fromString(mMPlayer.drmSchemeUuiddExtra)
            var drmSessionManager: DrmSessionManager<FrameworkMediaCrypto>? = null
            if (drmSchemeUuid != null) {
                val drmLicenseUrl = mMPlayer.drmLicenseUrl
                val keyRequestPropertiesArray = mMPlayer.keyRequestPropertiesArray
                val ketRequestProerties: MutableMap<String, String>?
                if (keyRequestPropertiesArray == null || keyRequestPropertiesArray.size < 2) {
                    ketRequestProerties = null
                } else {
                    ketRequestProerties = HashMap<String, String>()
                    var i = 0
                    while (i < keyRequestPropertiesArray.size - 1) {
                        ketRequestProerties.put(keyRequestPropertiesArray[i], keyRequestPropertiesArray[i + 1])
                        i += 2
                    }
                }
                try {
                    drmSessionManager = buildDrmSessionManager(drmSchemeUuid, drmLicenseUrl!!, ketRequestProerties!!)

                } catch (e: UnsupportedDrmException) {
                    val errorStringId = if (Util.SDK_INT < 18)
                        R.string.error_drm_not_supported
                    else
                        if (e.reason == UnsupportedDrmException.REASON_UNSUPPORTED_SCHEME)
                            R.string.error_drm_unsupported_scheme
                        else
                            R.string.error_drm_unknown
                    MApplication.instance.showMessage(AppConstants.TYPE_TOAST_ERROR, errorStringId)
                    return
                }

            }

            @SimpleExoPlayer.ExtensionRendererMode val extensionRendererMode = if (MApplication.instance.useExtensionRenderers())
                if (preferExtensionDecoders)
                    SimpleExoPlayer.EXTENSION_RENDERER_MODE_PREFER
                else
                    SimpleExoPlayer.EXTENSION_RENDERER_MODE_ON
            else
                SimpleExoPlayer.EXTENSION_RENDERER_MODE_OFF
            val videoTrackSelectionFactory = AdaptiveVideoTrackSelection.Factory(BANDWIDTH_METER)
            trackSelector = DefaultTrackSelector(videoTrackSelectionFactory)
            trackSelectionHelper = TrackSelectionHelper(trackSelector, videoTrackSelectionFactory)
            player = ExoPlayerFactory.newSimpleInstance(context, trackSelector!!, DefaultLoadControl(),
                    drmSessionManager, extensionRendererMode)

            player?.apply {
                addListener(this@VideoPlayerFragment)
                mEventLogger = EventLogger(trackSelector)
                addListener(mEventLogger)
                setAudioDebugListener(mEventLogger)
                setVideoDebugListener(mEventLogger)
                setMetadataOutput(mEventLogger)
                player_view.player = player
                playWhenReady = shoulAutoPlay
                debug_text_viewHelper = DebugTextViewHelper(player, debug_text_view)
                debug_text_viewHelper!!.start()
                playerNeedsSource = true
            }
        }
        if (playerNeedsSource) {
            val action = mMPlayer.action
            val uris: MutableList<Uri>
            var extensions: Array<String>? = null
            if (AppConstants.ACTION_VIEW == (action)) {
                uris = mutableListOf(mMPlayer.uri)
                mMPlayer.extensionExtra?.apply {
                    extensions = arrayOf<String>(this)
                }
            } else if (AppConstants.ACTION_VIEW_LIST == (action)) {
                val uriStrings = mMPlayer.uriListExtra
                uris = mutableListOf<Uri>()
                for (i in uriStrings!!.indices) {
                    uris.add(Uri.parse(uriStrings[i]))
                }
                extensions = mMPlayer.uriListExtra
                if (extensions == null) {
                    extensions = arrayOf<String>()
                }
            } else {
                MApplication.instance.showMessage(AppConstants.TYPE_TOAST_ERROR, getString(R.string.unexpected_intent_action, action))
                return
            }
            if (Util.maybeRequestReadExternalStoragePermission(activity, *uris.toTypedArray())) {
                // The player will be reinitialized if the permission is granted.
                return
            }
            val mediaSources = arrayOfNulls<MediaSource>(uris.size)
            for (i in uris.indices) {
                Log.d(TAG, "xyz--uris-[" + i + "]" + uris[i].toString())
                val ext = if (extensions != null && extensions!!.size > i) extensions!![i] else null
                mediaSources[i] = buildMediaSource(uris[i], ext)
            }
            val mediaSource = if (mediaSources.size == 1)
                mediaSources[0]
            else
                ConcatenatingMediaSource(*mediaSources)
            player?.apply {
                val haveResumePosition = resumeWindow != C.INDEX_UNSET
                if (haveResumePosition) {
                    seekTo(resumeWindow, resumePosition)
                }
                prepare(mediaSource, !haveResumePosition, false)
                playerNeedsSource = false
            }

            updateButtonVisibilities()
        }
    }


    private fun releasePlayer() {
        player?.apply {
            debug_text_viewHelper!!.stop()
            debug_text_viewHelper = null
            shoulAutoPlay = playWhenReady
            updateResumePosition()
            release()
            player = null
            trackSelector = null
            trackSelectionHelper = null
            mEventLogger = null
        }
        if (player != null) {

        }
    }


    override fun onClick(v: View) {
        if (v.parent === controls_root) {
            val mappedTrackInfo = trackSelector!!.currentMappedTrackInfo
            if (mappedTrackInfo != null) {
                trackSelectionHelper!!.showSelectionDialog(activity, (v as Button).text,
                        trackSelector!!.currentMappedTrackInfo, v.getTag() as Int)
            }
        }

    }

    /**
     * Support
     */
    private fun buildDataSourceFactory(useBandWidthMeter: Boolean): DataSource.Factory {
        return MApplication.instance.buildDataSourceFactory(if (useBandWidthMeter) BANDWIDTH_METER else null)
    }

    private fun clearResumePosition() {
        resumePosition = C.INDEX_UNSET.toLong()
        resumeWindow = C.INDEX_UNSET
    }

    @Throws(UnsupportedDrmException::class)
    private fun buildDrmSessionManager(uuid: UUID, licenseUrl: String, keyRequestProperties: Map<String, String>): DrmSessionManager<FrameworkMediaCrypto>? {
        if (Util.SDK_INT < 18) {
            return null
        }
        val drmCallback = HttpMediaDrmCallback(licenseUrl,
                buildHttpDataSourceFactory(false), keyRequestProperties)
        return DefaultDrmSessionManager(uuid,
                FrameworkMediaDrm.newInstance(uuid), drmCallback, null, mMainHandler, mEventLogger)
    }

    private fun buildHttpDataSourceFactory(useBandwidthMeter: Boolean): HttpDataSource.Factory {
        return MApplication.instance.buildHttpDataSourceFactory(if (useBandwidthMeter) BANDWIDTH_METER else null)
    }

    private fun buildMediaSource(uri: Uri, overrideExtension: String?): MediaSource {
        val type = Util.inferContentType(if (!TextUtils.isEmpty(overrideExtension))
            "." + overrideExtension
        else
            uri.lastPathSegment)
        when (type) {
            C.TYPE_SS -> return SsMediaSource(uri, buildDataSourceFactory(false),
                    DefaultSsChunkSource.Factory(mediaaDataSourceFactory), mMainHandler, mEventLogger)
            C.TYPE_DASH -> return DashMediaSource(uri, buildDataSourceFactory(false),
                    DefaultDashChunkSource.Factory(mediaaDataSourceFactory), mMainHandler, mEventLogger)
            C.TYPE_HLS -> return HlsMediaSource(uri, mediaaDataSourceFactory, mMainHandler, mEventLogger)
            C.TYPE_OTHER -> return ExtractorMediaSource(uri, mediaaDataSourceFactory, DefaultExtractorsFactory(),
                    mMainHandler, mEventLogger)
            else -> {
                throw IllegalStateException("Unsupported type: " + type)
            }
        }
    }


    private fun updateButtonVisibilities() {
        controls_root.removeAllViews()

        retry_button.visibility = if (playerNeedsSource) View.VISIBLE else View.GONE
        controls_root.addView(retry_button)
        if (player == null) {
            return
        }

        val mappedTrackInfo = trackSelector!!.currentMappedTrackInfo ?: return

        loop@ for (i in 0..mappedTrackInfo.length - 1) {
            val trackGroups = mappedTrackInfo.getTrackGroups(i)
            if (trackGroups.length != 0) {
                val button = Button(context)
                val label: Int
                when (player!!.getRendererType(i)) {
                    C.TRACK_TYPE_AUDIO -> label = R.string.audio
                    C.TRACK_TYPE_VIDEO -> label = R.string.video
                    C.TRACK_TYPE_TEXT -> label = R.string.text
                    else -> continue@loop
                }
                button.setText(label)
                button.tag = i
                button.setOnClickListener(this)
                controls_root.addView(button, controls_root.childCount - 1)
            }
        }
    }

    private fun updateResumePosition() {
        player?.apply {
            resumeWindow = currentWindowIndex
            resumePosition = if (isCurrentWindowSeekable)
                Math.max(0, currentPosition)
            else
                C.TIME_UNSET
        }
    }

    private fun showControls() {
        controls_root.visibility = View.VISIBLE
    }


    /**
     * Media Player
     */

    override fun onTimelineChanged(timeline: Timeline?, manifest: Any?) {

    }

    override fun onTracksChanged(trackGroups: TrackGroupArray, trackSelections: TrackSelectionArray) {
        updateButtonVisibilities()
        val mappedTrackInfo = trackSelector!!.currentMappedTrackInfo
        if (mappedTrackInfo != null) {
            if (mappedTrackInfo.getTrackTypeRendererSupport(C.TRACK_TYPE_VIDEO) == MappingTrackSelector.MappedTrackInfo.RENDERER_SUPPORT_UNSUPPORTED_TRACKS) {
                MApplication.instance.showMessage(AppConstants.TYPE_TOAST_ERROR, R.string.error_unsupported_video)
            }
            if (mappedTrackInfo.getTrackTypeRendererSupport(C.TRACK_TYPE_AUDIO) == MappingTrackSelector.MappedTrackInfo.RENDERER_SUPPORT_UNSUPPORTED_TRACKS) {
                MApplication.instance.showMessage(AppConstants.TYPE_TOAST_ERROR, R.string.error_unsupported_audio)
            }
        }
    }

    override fun onLoadingChanged(isLoading: Boolean) {

    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        if (playbackState == ExoPlayer.STATE_ENDED) {
            showControls()
        }
        updateButtonVisibilities()
    }

    override fun onPlayerError(e: ExoPlaybackException) {
        var errorString: String? = null
        if (e.type == ExoPlaybackException.TYPE_RENDERER) {
            val cause = e.rendererException
            if (cause is MediaCodecRenderer.DecoderInitializationException) {
                // Special case for decoder initialization failures.
                cause.apply {
                    if (decoderName == null) {
                        if (cause is MediaCodecUtil.DecoderQueryException) {
                            errorString = getString(R.string.error_querying_decoders)
                        } else if (secureDecoderRequired) {
                            errorString = getString(R.string.error_no_secure_decoder,
                                    mimeType)
                        } else {
                            errorString = getString(R.string.error_no_decoder,
                                    mimeType)
                        }
                    } else {
                        errorString = getString(R.string.error_instantiating_decoder,
                                decoderName)
                    }
                }
            }
        }
        if (errorString != null) {
            MApplication.instance.showMessage(AppConstants.TYPE_TOAST_ERROR, errorString!!)
        }
        playerNeedsSource = true
        if (isBehindLiveWindow(e)) {
            clearResumePosition()
            initializePlayer()
        } else {
            updateResumePosition()
            updateButtonVisibilities()
            showControls()
        }
    }

    override fun onPositionDiscontinuity() {
        if (playerNeedsSource) {
            // This will only occur if the user has performed a seek whilst in the error state. Update the
            // resume position so that if the user then retries, playback will resume from the position to
            // which they seeked.
            updateResumePosition()
        }
    }

    override fun onVisibilityChange(visibility: Int) {
        DebugLog.d("xyz--onVisibilityChange--" + visibility)
        controls_root.visibility = visibility
        if (visibility == View.VISIBLE) {
            mCallbackPlayerView.resetAutoRotationScreen()
        }
    }

    override fun onChangeVideo(mPlayer: MPlayer) {
        this.mMPlayer = mPlayer
        releasePlayer()
        initializePlayer()

    }

    override fun onPauseVideo() {

    }

    override fun onStopVideo() {
        releasePlayer()
    }

    override fun onSetCallbackPlayerView(mCallbackPlayerView: ICallbackPlayerView) {
        this.mCallbackPlayerView = mCallbackPlayerView
    }

    override fun onRequestFullScreenDone(isFullScreen: Boolean) {
        this.isFullScreen = isFullScreen
        DebugLog.d("xyz--onRequestFullScreenDone" + isFullScreen)
        if (isFullScreen)
            setFullscreen()
        else
            exitFullscreen(activity)
        updateStateFullScreen()
    }


    private fun updateStateFullScreen() {
        if (isFullScreen) {
            img_full_screen.setImageResource(R.drawable.ic_fullscreen_exit_black_24dp)
        } else {
            img_full_screen.setImageResource(R.drawable.ic_fullscreen_black_24dp)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (Build.VERSION.SDK_INT > 10) {
            unregisterSystemUiVisibility()
        }
        exitFullscreen(activity)
    }

    //Full Screen
    private val _handler = Handler()
    private val mRunnable = {
        setFullscreen()
    }

    fun setFullscreen() {
        if (isAdded) {
            setFullscreen(activity)
        }
    }

    fun setFullscreen(activity: Activity) {
        DebugLog.e("xyz--setFullscreen--")
        if (Build.VERSION.SDK_INT > 10) {
            getActivity().window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN or WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

            val flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or // hide nav bar
                    View.SYSTEM_UI_FLAG_FULLSCREEN or // hide status bar
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            root_video_view.setSystemUiVisibility(flags)
        }
    }

    fun exitFullscreen(activity: Activity) {
        DebugLog.e("xyz--exitFullscreen--")
        if (Build.VERSION.SDK_INT > 10) {
            getActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN or WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

            val flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

            //            activity.getWindow().getDecorView().setSystemUiVisibility(flags);
            root_video_view?.systemUiVisibility = flags
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private fun registerSystemUiVisibility() {
        val decorView = activity.window.decorView
        decorView.setOnSystemUiVisibilityChangeListener { visibility ->
            if (visibility and View.SYSTEM_UI_FLAG_FULLSCREEN == 0) {
                if (isFullScreen)
                    setFullscreen()
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private fun unregisterSystemUiVisibility() {
        val decorView = activity.window.decorView
        decorView.setOnSystemUiVisibilityChangeListener(null)
    }

    companion object {
        private val TAG = "VideoPayerFragment"
        private var DEFAULT_COOKIE_MANAGER: CookieManager? = null

        init {
            DEFAULT_COOKIE_MANAGER = CookieManager()
            DEFAULT_COOKIE_MANAGER!!.setCookiePolicy(CookiePolicy.ACCEPT_ORIGINAL_SERVER)
        }

        private fun isBehindLiveWindow(e: ExoPlaybackException): Boolean {
            if (e.type != ExoPlaybackException.TYPE_SOURCE) {
                return false
            }
            var cause: Throwable? = e.sourceException
            while (cause != null) {
                if (cause is BehindLiveWindowException) {
                    return true
                }
                cause = cause.cause
            }
            return false
        }

        fun newInstance(mMPlayer: MPlayer): VideoPlayerFragment {
            val fragment = VideoPlayerFragment()
            val arg = Bundle()
            arg.putSerializable(AppConstants.EXTRA_DATA, mMPlayer)
            fragment.arguments = arg
            return fragment
        }

        val isImmersiveAvailable: Boolean
            get() = Build.VERSION.SDK_INT >= 19
    }
}
