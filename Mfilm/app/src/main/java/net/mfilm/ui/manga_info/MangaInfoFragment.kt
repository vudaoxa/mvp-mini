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
import net.mfilm.ui.base.stack.BaseStackFragment
import net.mfilm.ui.chapters.ChaptersFragment
import net.mfilm.ui.chapters.ChaptersMvpView
import net.mfilm.utils.IndexTags
import net.mfilm.utils.TimeUtils
import net.mfilm.utils.handler
import net.mfilm.utils.setText
import java.io.Serializable

/**
 * Created by tusi on 5/18/17.
 */
class MangaInfoFragment : BaseStackFragment(), MangaInfoMvpView {
    companion object {
        const val KEY_MANGA = "KEY_MANGA"
        fun newInstance(manga: Any?): MangaInfoFragment {
            val fragment = MangaInfoFragment()
            val bundle = Bundle()
            bundle.putSerializable(KEY_MANGA, manga as? Serializable)
            fragment.arguments = bundle
            return fragment
        }
    }

    private var mChaptersMvpView: ChaptersMvpView? = null
    private var mChaptersFragment: ChaptersFragment? = null
    private lateinit var manga: Manga
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
        manga = arguments.getSerializable(KEY_MANGA) as Manga
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(net.mfilm.R.layout.fragment_manga_info, container, false)
    }

    override fun initFields() {
        back = true
        title = manga.name
    }

    override fun initViews() {
        initMangaInfoHeader()
        attachChaptersFragment()
    }

    fun initMangaInfoHeader() {
        manga.apply {
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
//            layout_manga_info_text.setOnClickListener { viewFullRead() }
            layout_manga_info.setOnClickListener { viewFullRead() }
            btn_read.setOnClickListener { onReadBtnClicked() }
        }
    }

    fun viewFullRead() {
        handler({
            screenManager?.onNewScreenRequested(IndexTags.FRAGMENT_FULL_READ, typeContent = null, obj = manga)
        }, 0)
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
        mChaptersFragment = ChaptersFragment.newInstance(manga)
        mChaptersMvpView = mChaptersFragment
        return mChaptersFragment
    }

    override fun obtainThumbsFragment(): Fragment? {
        return null
    }

    override fun obtainRelatedMangasFragment(): Fragment? {
        return null
    }

    //it's the bridge between chaptersFragment and chapterImageViewer
//    override fun onChapterClicked(chapter: Chapter) {
//        chapter.apply {
//            screenManager?.onNewScreenRequested(IndexTags.FRAGMENT_CHAPTER_IMAGES, typeContent = null, obj = this)
////            showFresco(context, )
//        }
//
//    }

    override fun onReadBtnClicked() {
        screenManager?.onNewScreenRequested(IndexTags.FRAGMENT_CHAPTER_IMAGES, typeContent = null,
                obj = mChaptersMvpView?.currentReadingChapter)
    }
}