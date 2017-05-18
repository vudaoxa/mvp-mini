package net.mfilm.ui.manga_info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.layout_manga_info_header.*
import kotlinx.android.synthetic.main.layout_manga_thumb_small.*
import net.mfilm.R
import net.mfilm.data.network_retrofit.Manga
import net.mfilm.ui.base.stack.BaseStackFragment
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

    private lateinit var mManga: Manga
    override var manga: Manga
        get() = mManga
        set(value) {
            mManga = value
        }

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
    }

    fun initMangaInfoHeader() {
        manga.apply {
            img_thumb.setImageURI(coverUrl)
            tv_name.text = name
            setText(context, tv_other_name, R.string.title_other_name, otherName)
            setText(context, tv_author, R.string.title_author, author)
            setText(context, tv_view_counts, R.string.title_views_count, views?.toString())
            setText(context, tv_des, R.string.title_des, summary)
        }
    }
}