package net.mfilm.ui.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import com.joanzapata.iconify.widget.IconTextView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.item_main_action_btns.*
import kotlinx.android.synthetic.main.layout_input_text.*
import net.mfilm.R
import net.mfilm.ui.base.stack.BaseStackActivity
import net.mfilm.ui.chapter_images.ChapterImagesFragment
import net.mfilm.ui.favorites.FavoritesFragment
import net.mfilm.ui.filmy.FullReadFragment
import net.mfilm.ui.history.HistoryFragment
import net.mfilm.ui.home.HomePagerFragment
import net.mfilm.ui.manga.PassByTime
import net.mfilm.ui.manga_info.MangaInfoFragment
import net.mfilm.ui.mangas.MangasFragment
import net.mfilm.ui.settings.SettingsFragment
import net.mfilm.utils.*
import timber.log.Timber
import javax.inject.Inject

class MainActivity : BaseStackActivity(), NavigationView.OnNavigationItemSelectedListener, MainMvpView {
    @Inject
    lateinit var mMainPresenter: MainMvpPresenter<MainMvpView>

    private var mScreenRequestPassByTime: PassByTime? = null
    override var screenRequestPassByTime: PassByTime?
        get() = mScreenRequestPassByTime
        set(value) {
            mScreenRequestPassByTime = value
        }
    override val mCallbackSearchView: ICallbackSearchView?
        get() = MangasFragment.getSearchInstance()
    override val edtSearch: EditText
        get() = edt_search
    override val imgClear: IconTextView
        get() = img_clear
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
    override val mLayoutInputText: LinearLayout
        get() = layout_input_text
    override val actionSettingsId: Int
        get() = R.id.action_settings
    override val actionAboutId: Int
        get() = R.id.action_about
    override val resLayout: Int
        get() = R.layout.activity_main

    override val contentFrameId: Int
        get() = R.id.container

    override val containerView: View
        get() = container
    override val homeClass: Class<*>
        get() = HomePagerFragment::class.java

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        onNewScreenRequested(navs.filter { it.id == item.itemId }[0].indexTag)
        mDrawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun sendOptionsMenuItem(item: MenuItem) {
        mMainPresenter.sendOptionsMenuItem(item)
    }

    override fun onSettings() {
        Timber.e("---------------onSettings-----------")
        onNewScreenRequested(IndexTags.FRAGMENT_SETTINGS)
    }

    override fun onAbout() {
        Timber.e("---------------onAbout-----------")
    }

    override fun onShare() {
        Timber.e("---------------onShare-----------")
    }

    override fun onFollow() {
        Timber.e("---------------onFollow-----------")
        mMainPresenter.onFollow()
    }

    override fun isFavorite(fav: Boolean?) {
        Timber.e("------------isFavorite-------$fav-----------------")
        var icon = icon_star
        fav?.run {
            if (this) icon = icon_star_blue
        }
        mBtnFollow.setImageDrawable(icon)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tryIt {
            activityComponent.inject(this)
            mMainPresenter.onAttach(this)
        }
        initFilters()
        initMangaSources(this)
    }

    override fun initViews(savedInstanceState: Bundle?) {
        super.initViews(savedInstanceState)
        initScreenRequestPassByTime()
        mNavView.setNavigationItemSelectedListener(this)
    }

    override fun initScreenRequestPassByTime() {
        screenRequestPassByTime = PassByTime(SCREEN_DURATION)
    }

    override fun showConfirmExit() {
        DialogUtil.showMessageConfirm(this, R.string.notifications, R.string.confirm_exit,
                MaterialDialog.SingleButtonCallback { _, _ -> finish() })
    }

    override fun onNewScreenRequested(indexTag: Any?, typeContent: String?, obj: Any?) {
        screenRequestPassByTime?.passByTime {
            when (indexTag) {
                IndexTags.FRAGMENT_HOME -> {
                    onMainScreenRequested()
                }
            //searching, category
                IndexTags.FRAGMENT_SEARCH -> {
                    fragmentStackManager.swapFragment(MangasFragment.newInstance(null, true))
                }
                IndexTags.FRAGMENT_CATEGORY -> {
                    fragmentStackManager.swapFragment(MangasFragment.newInstance(obj, false))
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
                    fragmentStackManager.swapFragment(FavoritesFragment.newInstance())
                }
                IndexTags.FRAGMENT_HISTORY -> {
                    fragmentStackManager.swapFragment(HistoryFragment.newInstance())
                }
                IndexTags.FRAGMENT_SETTINGS -> {
                    fragmentStackManager.swapFragment(SettingsFragment.newInstance())
                }
                IndexTags.FRAGMENT_ABOUT -> {

                }
            }
        }
    }

    override fun onNewFragmentRequested(indexTag: Any?, fragment: Fragment?, obj: Any?) {
        screenRequestPassByTime?.passByTime {
            when (indexTag) {
                IndexTags.FRAGMENT_CHAPTER_IMAGES -> {
                    fragmentStackManager.swapFragment(ChapterImagesFragment.newInstance(fragment), true)
                }
            }
        }
    }

    override fun onSearchScreenRequested() {
        Timber.e("----onSearchScreenRequested------------------------------------")
        onNewScreenRequested(IndexTags.FRAGMENT_SEARCH)
    }

    override fun onMainScreenRequested() = fragmentStackManager.run {
        clearStack()
        swapFragment(HomePagerFragment.newInstance())
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

    companion object {
        fun getStartIntent(splashActivity: Activity): Intent {
            val intent = Intent(splashActivity, MainActivity::class.java)
            return intent
        }
    }
}
