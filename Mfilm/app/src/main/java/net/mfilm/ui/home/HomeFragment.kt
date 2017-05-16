package net.mfilm.ui.home

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
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
import net.mfilm.ui.home.rv.MangaRvAdapter
import net.mfilm.utils.DebugLog
import net.mfilm.utils.filters
import javax.inject.Inject

/**
 * Created by tusi on 4/2/17.
 */
class HomeFragment : BaseLoadMoreFragment(), HomeMVPView {

    override var isDataEnd: Boolean
        get() = false
        set(value) {}

    companion object {
        fun newInstance(): HomeFragment {
            val args = Bundle()
            val fragment = HomeFragment()
            fragment.arguments = args
            return fragment
        }
    }

    @Inject
    lateinit var mPresenter: HomeMvpPresenter<HomeMVPView>
    lateinit var mMangasRvAdapter: MangaRvAdapter



    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_home, container, false)
    }

    override fun initViews() {
        buildSpnBanks()
        rv.apply {
            layoutManager = LinearLayoutManager(context)
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

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
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
        mPresenter.requestMangas(null, 10, 1, filters[spnFilterTracker.mPosition].content, null)
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

    }

    override fun onLoadMore() {
        mMangasRvAdapter.onLoadMore()
    }
}