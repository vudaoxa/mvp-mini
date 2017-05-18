package net.mfilm.ui.mangas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.fragment_home.*
import net.mfilm.R
import net.mfilm.data.network_retrofit.Manga
import net.mfilm.data.network_retrofit.MangasResponse
import net.mfilm.ui.base.rv.BaseLoadMoreFragment
import net.mfilm.ui.base.rv.holders.TYPE_ITEM_MANGA
import net.mfilm.ui.mangas.rv.MangasRvAdapter
import net.mfilm.utils.DebugLog
import net.mfilm.utils.IndexTags
import net.mfilm.utils.filters

/**
 * Created by tusi on 4/2/17.
 */
class MangasFragment : BaseLoadMoreFragment(), MangasMVPView {

    companion object {
        fun newInstance(): MangasFragment {
            val args = Bundle()
            val fragment = MangasFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override var isDataEnd: Boolean
        get() = false
        set(value) {}

    @javax.inject.Inject
    lateinit var mPresenter: MangasMvpPresenter<MangasMVPView>
    var mMangasRvAdapter: MangasRvAdapter? = null


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_home, container, false)
    }

    override fun initViews() {
        buildSpnBanks()
        rv.apply {
            layoutManager = android.support.v7.widget.LinearLayoutManager(context)
            setupOnLoadMore(this, mCallBackLoadMore)
        }
        requestMangas()
    }

    override fun initFields() {
        activityComponent.inject(this)
        mPresenter.onAttach(this)
    }

    inner class AdapterTracker : AdapterView.OnItemSelectedListener {
        var mPosition = 0
        override fun onNothingSelected(parent: AdapterView<*>?) {

        }

        override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
            mPosition = position
        }

    }

    val spnFilterTracker = AdapterTracker()
    fun buildSpnBanks() {
        val banksAdapter = ArrayAdapter(activity, R.layout.item_spn_filter, filters.map { getString(it.resId) })
        spn_filter.setAdapter(banksAdapter)
        spn_filter.setOnItemSelectedListener(spnFilterTracker)
    }

    override fun requestMangas() {
        mPresenter.requestMangas(null, 10, page++, filters[spnFilterTracker.mPosition].content, null)
    }

    override fun onMangasResponse(mangasResponse: MangasResponse?) {
        hideLoading()
        mangasResponse.let { mr ->
            mr?.apply {
                mr.mangasPaging.let { mp ->
                    mp?.apply {
                        mp.mangas.let { mgs ->
                            mgs?.apply {
                                if (mgs.isNotEmpty()) {
                                    initMangas(mgs)
                                } else onMangasNull()
                            } ?: let { onMangasNull() }
                        }
                    } ?: let { onMangasNull() }
                }
            } ?: let { onMangasNull() }
        }
    }

    override fun onMangasNull() {
        DebugLog.e("----------------onMangasNull-----------------")
    }

    override fun initMangas(mangas: List<Manga>) {
        DebugLog.e("---------------initMangas---------------${mangas[0].coverUrl}")
        mMangasRvAdapter?.apply {
            onAdapterLoadMoreFinished {
                mMangas?.addAll(mangas)
                notifyDataSetChanged()
            }
        } ?: let {
            mMangasRvAdapter = MangasRvAdapter(context, mangas.toMutableList(), this)
            rv.adapter = mMangasRvAdapter
        }
    }

    override fun onLoadMore() {
        DebugLog.e("--------------------onLoadMore----------------------")
        mMangasRvAdapter?.onAdapterLoadMore { requestMangas() }
    }

    override fun onClick(position: Int, event: Int) {
        DebugLog.e("---------------------onClick--------------------$position")
        if (event != TYPE_ITEM_MANGA) return
        screenManager?.onNewScreenRequested(IndexTags.FRAGMENT_MANGA_INFO, typeContent = null, obj = mMangasRvAdapter?.mMangas!![position])
    }
}