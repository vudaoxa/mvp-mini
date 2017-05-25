package net.mfilm.ui.main

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.Menu
import android.view.MenuItem
import com.afollestad.materialdialogs.MaterialDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import net.mfilm.R
import net.mfilm.ui.base.stack.BaseStackActivity
import net.mfilm.ui.home.HomePagerFragment
import net.mfilm.ui.manga_info.MangaInfoFragment
import net.mfilm.utils.*
import vn.tieudieu.fragmentstackmanager.BaseFragmentStack
import javax.inject.Inject

class MainActivity : BaseStackActivity(), NavigationView.OnNavigationItemSelectedListener, MainMvpView{
    override val resLayout: Int
        get() = R.layout.activity_main

    override val contentFrameId: Int
        get() = R.id.container

    override val homeClass: Class<*>
        get() = HomePagerFragment::class.java

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        onNewScreenRequested(navs.filter { it.id == item.itemId }[0].indexTag, typeContent = null, obj = null)
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onSettings() {
        DebugLog.e("---------------onSettings-----------")
    }

    @Inject
    lateinit var mMainPresenter: MainMvpPresenter<MainMvpView>
    private var mDoubleBackToExitPressedOnce = false
    internal var mOrientation = Configuration.ORIENTATION_PORTRAIT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityComponent.inject(this)
        mMainPresenter.onAttach(this)
        initViews()
        initFilters()
        obtainTabletSize(this)
    }

    override fun initViews() {
        toolbar?.apply {
            setSupportActionBar(this)
        }
        mToggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        mToggle?.apply {
            drawer_layout.addDrawerListener(this)
            syncState()
        }
        nav_view.setNavigationItemSelectedListener(this)

        toolbar_back.setOnClickListener { onBackPressed() }
        btn_search.setImageDrawable(icon_search)
        btn_search.setOnClickListener { goSearch() }
    }


    fun initSearch() {
//        search_view.apply {
//            setVoiceSearch(true)
//            setOnQueryTextListener(object : SearchView.OnQueryTextListener {})
//        }
    }
    override fun goSearch() {
        DebugLog.e("---------goSearch-------------")
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            if (fragmentStackManager.currentFragment.javaClass == homeClass) {
                showConfirmExit()
            } else super.onBackPressed()
        }
    }

    private fun showConfirmExit() {
        DialogUtil.showMessageConfirm(this, R.string.notifications, R.string.confirm_exit,
                MaterialDialog.SingleButtonCallback { _, _ -> finish() })
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onNewScreenRequested(indexTag: Int, typeContent: String?, obj: Any?) {
        when (indexTag) {
            IndexTags.FRAGMENT_HOME -> {
                onMainScreenRequested()
            }
            IndexTags.FRAGMENT_MANGA -> {

            }
            IndexTags.FRAGMENT_MANGA_INFO -> {
                fragmentStackManager.swapFragment(MangaInfoFragment.newInstance(obj))
            }
            IndexTags.FRAGMENT_CHAPTER_INFO -> {

            }
            IndexTags.FRAGMENT_CHAPTER_IMAGES -> {

            }
            IndexTags.FRAGMENT_FAV -> {

            }
            IndexTags.FRAGMENT_HISTORY -> {

            }
        }
    }

    override fun onNewScreenRequested(indexTag: Int, fragment: Fragment?, obj: Any?) {
    }

    /**
     * OVERRIDE FRAGMENT STACK
     */



    override fun onMainScreenRequested() = fragmentStackManager.run {
        clearStack()
        swapFragment(HomePagerFragment.newInstance())
    }

    override fun onFragmentEntered(fragment: Fragment?) {
        setToolbarTitle((fragment as BaseFragmentStack).title)
        if (fragment.back) {
            showBtnBack()
        } else {
            showDrawer()
        }
        mMenu?.apply {
            findItem(R.id.action_settings).isVisible = fragment.javaClass == homeClass
        }
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
