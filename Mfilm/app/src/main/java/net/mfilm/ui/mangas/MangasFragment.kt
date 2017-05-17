package net.mfilm.ui.mangas

import kotlinx.android.synthetic.main.fragment_home.*
import net.mfilm.R
import net.mfilm.utils.filters

/**
 * Created by tusi on 4/2/17.
 */
class MangasFragment : net.mfilm.ui.base.rv.BaseLoadMoreFragment(), MangasMVPView {

    companion object {
        fun newInstance(): net.mfilm.ui.mangas.MangasFragment {
            val args = android.os.Bundle()
            val fragment = net.mfilm.ui.mangas.MangasFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override var isDataEnd: Boolean
        get() = false
        set(value) {}

    @javax.inject.Inject
    lateinit var mPresenter: MangasMvpPresenter<MangasMVPView>
    var mMangasRvAdapter: net.mfilm.ui.mangas.rv.MangaRvAdapter? = null


    override fun onCreateView(inflater: android.view.LayoutInflater?, container: android.view.ViewGroup?, savedInstanceState: android.os.Bundle?): android.view.View? {
        return inflater!!.inflate(net.mfilm.R.layout.fragment_home, container, false)
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

    inner class AdapterTracker : android.widget.AdapterView.OnItemSelectedListener {
        var mPosition = 0
        override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {

        }

        override fun onItemSelected(parent: android.widget.AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
            mPosition = position
        }

    }

    val spnFilterTracker = AdapterTracker()
    fun buildSpnBanks() {
        val banksAdapter = android.widget.ArrayAdapter(activity, R.layout.item_spn_filter, filters.map { getString(it.resId) })
        spn_filter.setAdapter(banksAdapter)
        spn_filter.setOnItemSelectedListener(spnFilterTracker)
    }

    override fun requestMangas() {
        mPresenter.requestMangas(null, 10, page++, net.mfilm.utils.filters[spnFilterTracker.mPosition].content, null)
    }

    override fun onMangasResponse(mangasResponse: net.mfilm.data.network_retrofit.MangasResponse?) {
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
        net.mfilm.utils.DebugLog.e("----------------onMangasNull-----------------")
    }

    override fun initMangas(mangas: List<net.mfilm.data.network_retrofit.Manga>) {
        net.mfilm.utils.DebugLog.e("---------------initMangas---------------${mangas[0].coverUrl}")
        mMangasRvAdapter?.apply {
            onAdapterLoadMoreFinished {
                mMangas?.addAll(mangas)
                notifyDataSetChanged()
            }
        } ?: let {
            mMangasRvAdapter = net.mfilm.ui.mangas.rv.MangaRvAdapter(context, mangas.toMutableList(), this)
            rv.adapter = mMangasRvAdapter
        }
    }

    override fun onLoadMore() {
        net.mfilm.utils.DebugLog.e("--------------------onLoadMore----------------------")
        mMangasRvAdapter?.onAdapterLoadMore { requestMangas() }
    }

    override fun onClick(position: Int, event: Int) {
        net.mfilm.utils.DebugLog.e("---------------------onClick--------------------$position")
    }
}