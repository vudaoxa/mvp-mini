package net.mfilm.ui.main

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.item_main_action_btns.*
import net.mfilm.R
import net.mfilm.ui.base.stack.BaseStackActivity
import net.mfilm.ui.base.stack.BaseStackFragment
import net.mfilm.ui.filmy.FullReadFragment
import net.mfilm.ui.home.HomePagerFragment
import net.mfilm.ui.manga_info.MangaInfoFragment
import net.mfilm.utils.*
import javax.inject.Inject

class MainActivity : BaseStackActivity(), NavigationView.OnNavigationItemSelectedListener, MainMvpView{
    override val mToolbar: Toolbar
        get() = toolbar
    override val mToolbarBack: ImageButton
        get() = toolbar_back
    override val mToolbarTitle: TextView
        get() = toolbar_title
    override val mDrawerLayout: DrawerLayout
        get() = drawer_layout
    override val mBtnSearch: ImageButton
        get() = btn_search
    override val mBtnFollow: ImageButton
        get() = btn_star
    override val mBtnShare: ImageButton
        get() = btn_twitter
    override val mNavView: NavigationView
        get() = nav_view
    override val mLayoutBtnsInfo: LinearLayout
        get() = layout_btns_info
    override val actionSettingsId: Int
        get() = R.id.action_settings
    override val actionAboutId: Int
        get() = R.id.action_about
    override val optionsMenuId: Int
        get() = R.menu.main
    override val resLayout: Int
        get() = R.layout.activity_main

    override val contentFrameId: Int
        get() = R.id.container

    override val homeClass: Class<*>
        get() = HomePagerFragment::class.java

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        onNewScreenRequested(navs.filter { it.id == item.itemId }[0].indexTag, typeContent = null, obj = null)
        mDrawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onSettings() {
        DebugLog.e("---------------onSettings-----------")
    }

    override fun onAbout() {

    }

    override fun onSearch() {

    }

    override fun onShare() {

    }

    override fun onFollow() {

    }
    @Inject
    lateinit var mMainPresenter: MainMvpPresenter<MainMvpView>
    internal var mOrientation = Configuration.ORIENTATION_PORTRAIT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityComponent.inject(this)
        mMainPresenter.onAttach(this)
        initFilters()
        obtainTabletSize(this)
    }

    override fun initViews(savedInstanceState: Bundle?) {
        super.initViews(savedInstanceState)
        mNavView.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START)
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

    override fun onNewScreenRequested(indexTag: Int, typeContent: String?, obj: Any?) {
        when (indexTag) {
            IndexTags.FRAGMENT_HOME -> {
                onMainScreenRequested()
            }
        //search, category
            IndexTags.FRAGMENT_MANGA -> {

            }
            IndexTags.FRAGMENT_MANGA_INFO -> {
                fragmentStackManager.swapFragment(MangaInfoFragment.newInstance(obj))
            }
            IndexTags.FRAGMENT_FULL_READ -> {
                fragmentStackManager.swapFragment(FullReadFragment.newInstance(obj), true)
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

    override fun onFragmentEntered(f: Fragment?) {
        super.onFragmentEntered(f)
        val fragment = f as BaseStackFragment
        val home = fragment.javaClass == homeClass
        var info = false
//        DebugLog.e("-----------onFragmentEntered-------${fragment.javaClass}------ $homeClass------$home")
        when (fragment) {
            is MangaInfoFragment -> {
                info = true
            }
        }
        mLayoutBtnsInfo.show(info)
        mBtnSearch.show(!info)
        mMenu?.apply {
            findItem(actionSettingsId).isVisible = home
            findItem(actionAboutId).isVisible = home
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
