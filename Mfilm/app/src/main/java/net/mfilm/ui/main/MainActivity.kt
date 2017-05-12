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
import android.view.View
import android.view.ViewGroup
import com.github.pedrovgs.DraggableListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import net.mfilm.MApplication
import net.mfilm.R
import net.mfilm.ui.base.stack.BaseStackActivity
import net.mfilm.ui.home.HomeFragment
import net.mfilm.ui.tabs.TabsFragment
import net.mfilm.utils.AppConstants
import net.mfilm.utils.DebugLog
import javax.inject.Inject

class MainActivity : BaseStackActivity(), NavigationView.OnNavigationItemSelectedListener, MainMvpView{
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onSettings() {
        DebugLog.e("---------------onSettings-----------")
    }

    @Inject
    lateinit var mMainPresenter: MainMvpPresenter<MainMvpView>
//    @Inject
//    lateinit var mVideoPresenter: VideoMvpPresenter<VideoMvpView>
    //    @Inject
//    lateinit var mLoginPresenter: LoginMvpPresenter<LoginMvpView>
    private var mDoubleBackToExitPressedOnce = false
    internal var fragmentVideo: Fragment? = null
    internal var mOrientation = Configuration.ORIENTATION_PORTRAIT
    private var mHeightFragmentVideoSmall = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityComponent.inject(this)
        mMainPresenter.onAttach(this)
        initViews()
    }

    override fun initViews() {
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
            if (fragmentStackManager.currentFragment.javaClass == homeClass) {
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

    /**
     * OVERRIDE FRAGMENT STACK
     */

    override val resLayout: Int
        get() = R.layout.activity_main

    override val contentFrameId: Int
        get() = R.id.container

    override val homeClass: Class<*>
        get() = HomeFragment::class.java

    override fun onMainScreenRequested() {
        fragmentStackManager.clearStack()
        fragmentStackManager.swapFragment(HomeFragment.newInstance())
    }

    override fun onFragmentEntered(fragment: Fragment?) {

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
    }

    private fun isFullScreen(): Boolean {
        return mOrientation == Configuration.ORIENTATION_LANDSCAPE
    }

    @Synchronized private fun setOrientationOfMain(requestedOrientation: Int) {
        setRequestedOrientation(requestedOrientation)
    }

    @Synchronized private fun exitFullScreenVideo(): Boolean {
        return true
    }


    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        DebugLog.d("xyz----onConfigurationChanged-----")
    }

    @Synchronized private fun setFullScreenVideo(): Boolean {
        return true
    }

    companion object {
        fun getStartIntent(splashActivity: Activity): Intent {
            val intent = Intent(splashActivity, MainActivity::class.java)
            return intent
        }
    }
}
