package net.mfilm.ui.manga_info

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_manga_info.*
import kotlinx.android.synthetic.main.layout_manga_info_header.*
import kotlinx.android.synthetic.main.layout_manga_info_text.*
import kotlinx.android.synthetic.main.layout_manga_thumb.*
import net.mfilm.R
import net.mfilm.data.network_retrofit.Manga
import net.mfilm.data.network_retrofit.MangaDetailResponse
import net.mfilm.ui.base.stack.BaseStackFragment
import net.mfilm.ui.chapters.ChaptersFragment
import net.mfilm.utils.IndexTags
import net.mfilm.utils.TimeUtils
import net.mfilm.utils.setText
import timber.log.Timber
import java.io.Serializable
import javax.inject.Inject

/**
 * Created by tusi on 5/18/17.
 */
class MangaInfoFragment : BaseStackFragment(), MangaInfoMvpView {
    companion object {
        const val KEY_MANGA = "KEY_MANGA"
        const val KEY_MANGA_ID = "KEY_MANGA_ID"
        fun newInstance(obj: Any?): MangaInfoFragment {
            val fragment = MangaInfoFragment()
            val bundle = Bundle()
            when (obj) {
                is Int -> {
                    bundle.putInt(KEY_MANGA_ID, obj)
                }
                is Serializable -> {
                    bundle.putSerializable(KEY_MANGA, obj)
                }
            }
            fragment.arguments = bundle
            return fragment
        }
    }

    @Inject
    lateinit var mMangaInfoMvpPresenter: MangaInfoMvpPresenter<MangaInfoMvpView>
    private var mChaptersFragment: ChaptersFragment? = null
    private var mManga: Manga? = null
    override val chaptersContainerView: View
        get() = container_chapters
    override val chaptersContainerId: Int
        get() = R.id.container_chapters
    override val thumbsContainerView: View
        get() = container_thumbs
    override val thumbsContainerId: Int
        get() = R.id.container_thumbs
    override val relatedMangasContainerView: View
        get() = container_related_mangas
    override val relatedMangasContainerId: Int
        get() = R.id.container_related_mangas

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityComponent.inject(this)
        mMangaInfoMvpPresenter.onAttach(this)
        obtainManga()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(net.mfilm.R.layout.fragment_manga_info, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        buildManga()
    }
    override fun onDestroy() {
        super.onDestroy()
        mMangaInfoMvpPresenter.onDetach()
    }

    override fun buildManga() {
        mManga?.id?.apply {
            initFields()
            initViews()
        }
    }

    override fun obtainManga() {
        val x = arguments.getSerializable(KEY_MANGA)
        val id = arguments.getInt(KEY_MANGA_ID)
        Timber.e("------obtainManga------------ $x------$id----")
        val mg = x as? Manga?
        mg?.apply {
            mManga = this
        } ?: let {
            requestManga(id)
        }
    }

    override fun initFields() {
        info = true
        back = true
        title = mManga?.name
    }

    override fun initViews() {
        initMangaInfoHeader()
        attachChaptersFragment()
        isFavorite()
    }

    override fun requestManga(id: Int) {
        mMangaInfoMvpPresenter.requestManga(id)
    }

    override fun onMangaDetailResponse(mangaDetailResponse: MangaDetailResponse?) {
        hideLoading()
        mangaDetailResponse?.apply {
            manga?.apply {
                mManga = this
                buildManga()
                baseActivity?.onFragmentEntered(this@MangaInfoFragment)
            } ?: let { onMangaNull() }
        } ?: let { onMangaNull() }
    }

    override fun onMangaNull() {
        Timber.e("-------------onMangaNull------------------")
    }

    override fun isFavorite() {
        mMangaInfoMvpPresenter.isFavorite(mManga?.id!!)
    }

    override fun initMangaInfoHeader() {
        mManga?.apply {
            img_thumb.setImageURI(coverUrl)
            tv_name.text = name
            setText(context, tv_other_name, R.string.title_other_name, otherName)
            setText(context, tv_author, R.string.title_author, author)
            setText(context, tv_categories, R.string.title_categories, categories?.map { it.name }?.joinToString())
//                    setText(context, tv_chaps_count, R.string.title_chaps_count, totalChap?.toString())
            setText(context, tv_view_counts, R.string.title_views_count, views?.toString())
            updatedTime.let { u ->
                u?.apply {
                    setText(context, tv_updated_at, R.string.title_updated_at, TimeUtils.toFbFormatTime(context, u * 1000))
                }
            }
            setText(context, tv_des, -1, summary)
            layout_manga_info.setOnClickListener { viewFullRead() }
            btn_read.setOnClickListener { onReadBtnClicked() }
        }
    }

    override fun viewFullRead() {
        screenManager?.onNewScreenRequested(IndexTags.FRAGMENT_FULL_READ, typeContent = null, obj = mManga)
    }

    override fun toggleFav(): Boolean {
        mManga?.apply {
            return mMangaInfoMvpPresenter.toggleFav(this)
        }
        return false
    }
    override fun attachChaptersFragment() {
        attachChildFragment(chaptersContainerView, chaptersContainerId, obtainChaptersFragment())
    }

    override fun attachThumbsFragment() {

    }

    override fun attachRelatedMangasFragment() {

    }

    override fun obtainChaptersFragment(): Fragment? {

        mChaptersFragment?.apply { return this }
        mChaptersFragment = ChaptersFragment.newInstance(mManga)
//        mChaptersMvpView = mChaptersFragment
        return mChaptersFragment
    }

    override fun obtainThumbsFragment(): Fragment? {
        return null
    }

    override fun obtainRelatedMangasFragment(): Fragment? {
        return null
    }

    override fun saveHistory() {
        mManga?.apply {
            mMangaInfoMvpPresenter.saveHistory(this)
        }
    }

    override fun onReadBtnClicked() {
        saveHistory()
        screenManager?.onNewScreenRequested(IndexTags.FRAGMENT_CHAPTER_IMAGES, fragment = mChaptersFragment,
                obj = null)
    }
}