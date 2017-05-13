package net.mfilm.ui.main

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import com.github.pedrovgs.DraggableListener
import com.tieudieu.fragmentstackmanager.BaseFragmentStack
import com.tieudieu.util.DebugLog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import net.mfilm.MApplication
import net.mfilm.R
import net.mfilm.ui.base.stack.BaseStackActivity
import net.mfilm.ui.tabs.TabsFragment
import net.mfilm.ui.videos.base.VideoMvpPresenter
import net.mfilm.ui.videos.base.VideoMvpView
import net.mfilm.ui.videos.model.MPlayer
import net.mfilm.utils.AppConstants
import javax.inject.Inject

class MainActivity : NavigationView.OnNavigationItemSelectedListener, BaseStackActivity(), MainMvpView, VideoMvpView {
    @Inject
    lateinit var mMainPresenter: MainMvpPresenter<MainMvpView>
    @Inject
    lateinit var mVideoPresenter: VideoMvpPresenter<VideoMvpView>
    private var mDoubleBackToExitPressedOnce = false
    internal var fragmentVideo: Fragment? = null
    internal var mOrientation = Configuration.ORIENTATION_PORTRAIT
    private var mHeightFragmentVideoSmall = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityComponent.inject(this)
        mMainPresenter.onAttach(this)
        mVideoPresenter.onAttach(this)
        initView()
    }

    private fun initView() {
        toolbar?.apply {
            setSupportActionBar(this)
            supportActionBar?.setDefaultDisplayHomeAsUpEnabled(true)
        }
        mToggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        mToggle?.apply {
            drawer_layout.addDrawerListener(this)
            syncState()
        }
        nav_view.setNavigationItemSelectedListener(this)
        toolbar_back.setOnClickListener { onBackPressed() }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            if (fragmentStackManager.currentFragment != null && fragmentStackManager.currentFragment.javaClass == homeClass) {
                if (mDoubleBackToExitPressedOnce) {
                    finish()
                    return
                }
                this.mDoubleBackToExitPressedOnce = true
                MApplication.instance.showMessage(AppConstants.TYPE_TOAST_NOMART, R.string.text_all_click_back_exit_app)
                Handler().postDelayed({ mDoubleBackToExitPressedOnce = false }, AppConstants.TIME_DELAY_ON_FINISH)
            } else
                super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_settings) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onNewScreenRequested(indexTag: Int, typeContent: String?, obj: Any?) {
    }

    override fun onNewScreenRequested(indexTag: Int, fragment: Fragment?, obj: Any?) {
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
//        when (item.itemId) {
//            R.id.nav_home -> {
//                fragmentStackManager.clearStack()
////                fragmentStackManager.swapFragment(HomePagerFragment.newInstance())
//            }
//            R.id.nav_acc_info -> {
//
//            }
//            R.id.nav_change_pass -> {
//
//            }
//            R.id.nav_tut -> {
//
//            }
//            R.id.nav_exit -> {
//
//            }
//        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
    /**
     * OVERRIDE FRAGMENT STACK
     */

    override fun getResLayout(): Int {
        return R.layout.activity_main
    }

    override fun getContentFrameId(): Int {
        return R.id.container
    }

    override fun getHomeClass(): Class<*> {
        return TabsFragment::class.java
    }

    override fun onMainScreenRequested() {
        fragmentStackManager.clearStack()
        fragmentStackManager.swapFragment(TabsFragment.newInstance())
    }

    override fun onFragmentEntered(fragment: Fragment?) {
        setToolbarTitle((fragment as BaseFragmentStack).title)
        if (fragment.showBackButton()) {
            showBtnBack()
        } else {
            showDrawer()
        }
        var home = false
        when (fragment) {
            is TabsFragment -> {
                home = true
            }
        }
        mMenu?.apply {
            findItem(R.id.action_settings).isVisible = home
        }
    }

    override fun onSettings() {

    }

    // End fragment stack


    /**
     * OVERRIDE  MAIN VIEW
     */
    override fun openLoginActivity() {

    }

    override fun showAboutFragment() {

    }

    override fun updateUserName(currentUserName: String) {

    }

    override fun updateUserEmail(currentUserEmail: String) {

    }

    override fun updateUserProfilePic(currentUserProfilePicUrl: String) {

    }

    override fun updateAppVersion() {

    }

    override fun onDestroy() {
        super.onDestroy()
        mMainPresenter.onDetach()
        mVideoPresenter.onDetach()
    }

    override fun playVideo(mPlayer: MPlayer?) {
        mVideoPresenter.onPlayVideo(getFakePlayer())
    }

    override fun updateVideoView(videoFragment: Fragment, inforFragment: Fragment) {
        this.fragmentVideo = videoFragment
        draggable_panel.apply {
            setFragmentManager(supportFragmentManager)
            setTopFragment(videoFragment)
            setBottomFragment(inforFragment)
            isClickToMaximizeEnabled = true
            hookDraggablePanelListeners()
            initializeView()
            setOrientationOfMain(ActivityInfo.SCREEN_ORIENTATION_SENSOR)
        }
    }

    override fun onShowVideo(isFirst: Boolean) {
        draggable_panel.maximize()
        setOrientationOfMain(ActivityInfo.SCREEN_ORIENTATION_SENSOR)
    }

    override fun onToggleFullScreen() {
        DebugLog.d("xyz--onToggleFullScreen~IsFullScreen-" + isFullScreen())
        if (isFullScreen()) {
            setOrientationOfMain(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        } else
            setOrientationOfMain(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
    }

    override fun resetAutoRotationScreen() {
        setOrientationOfMain(ActivityInfo.SCREEN_ORIENTATION_SENSOR)
    }

    private fun getFakePlayer(): MPlayer {
        //
        //        http://103.21.148.46:1935/live/smil:todaytv_1.smil/playlist.m3u8?DVR
        //                .setUri("https://storage.googleapis.com/exoplayer-test-media-1/gen-3/screens/dash-vod-single-segment/video-avc-baseline-480.mp4")
        val url = "http://tvplay.vn:9999/truongquayao1/smil:29032017.smil/playlist.m3u8?DVR"
        val mPlayer = MPlayer.Builder()
                .setAction(AppConstants.ACTION_VIEW)
                .setName("Dizzy")
                .setUri(url)
                .setExtensionExtra(null)
                .setPrerExtensisonDecodes(false).buidler()
        return mPlayer
    }

    private fun hookDraggablePanelListeners() {
        draggable_panel.setDraggableListener(object : DraggableListener {
            override fun onMaximized() {
                setOrientationOfMain(ActivityInfo.SCREEN_ORIENTATION_SENSOR)
                DebugLog.d("xyz--draggable_panel_" + draggable_panel.width + " - " + draggable_panel.height)
                DebugLog.d("xyz--draggable_panel_UIVISIBILITY" + draggable_panel.systemUiVisibility)
            }

            override fun onMinimized() {
                DebugLog.d("xyz--onMinimized--")
                setOrientationOfMain(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

            }

            override fun onClosedToLeft() {
                DebugLog.d("xyz--onClosedToLeft--")
                closeVideoPlayer()

            }

            override fun onClosedToRight() {
                DebugLog.d("xyz--onClosedToRight--")
                closeVideoPlayer()

            }
        })
    }

    private fun closeVideoPlayer() {
        mVideoPresenter.onCloseVideo()
        setOrientationOfMain(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

    }

    private fun isFullScreen(): Boolean {
        return mOrientation == Configuration.ORIENTATION_LANDSCAPE
    }

    @Synchronized private fun setOrientationOfMain(requestedOrientation: Int) {
        setRequestedOrientation(requestedOrientation)
    }

    @Synchronized private fun exitFullScreenVideo(): Boolean {
        val playerView = fragmentVideo?.getView()
//        container_video_full_screen.removeView(playerView)
//        container_video_full_screen.setVisibility(View.INVISIBLE)
        val playerParent = findViewById(R.id.drag_view) as ViewGroup
        val width = playerParent.width

        DebugLog.d("xyz--player~PORTRAIT-WIDTH-" + width)
        DebugLog.d("xyz--player~PORTRAIT-HEIGHT-" + mHeightFragmentVideoSmall)

        playerView!!.layoutParams.width = width
        playerView.layoutParams.height = mHeightFragmentVideoSmall
        playerView.requestLayout()
        playerParent.addView(playerView, playerView.layoutParams)
        return true
    }


    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        DebugLog.d("xyz----onConfigurationChanged-----")
        if (fragmentVideo == null) return
        this.mOrientation = newConfig.orientation
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mVideoPresenter.onSetFullScreenDone(true)
            if (mHeightFragmentVideoSmall <= 0)
                mHeightFragmentVideoSmall = fragmentVideo?.getView()!!.height
            Handler().postDelayed({ setFullScreenVideo() }, 150)

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            mVideoPresenter.onSetFullScreenDone(false)
            Handler().postDelayed({ exitFullScreenVideo() }, 150)

        }
    }

    @Synchronized private fun setFullScreenVideo(): Boolean {
        val playerView = fragmentVideo?.getView()
        val playerParent = findViewById(R.id.drag_view) as ViewGroup
        playerParent.removeView(playerView)

//        container_video_full_screen.setVisibility(View.VISIBLE)
//        val width = container_video_full_screen.getWidth()
//        val height = container_video_full_screen.getHeight()
//        DebugLog.d("xyz---player~LANDSCAPE-WIDTH-" + width)
//        DebugLog.d("xyz---player~LANDSCAPE-HEIGHT-" + height)
//        playerView!!.layoutParams.width = width
//        playerView.layoutParams.height = height
//        playerView.requestLayout()
//        container_video_full_screen.addView(playerView, playerView.layoutParams)
//        container_video_full_screen.requestLayout()
        return true
    }

    companion object {
        fun getStartIntent(splashActivity: Activity): Intent {
            val intent = Intent(splashActivity, MainActivity::class.java)
            return intent
        }
    }
}
