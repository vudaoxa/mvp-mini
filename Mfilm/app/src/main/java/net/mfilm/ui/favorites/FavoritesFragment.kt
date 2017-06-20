package net.mfilm.ui.favorites

import net.mfilm.ui.base.stack.BaseStackFragment
import javax.inject.Inject

/**
 * Created by MRVU on 6/20/2017.
 */
class FavoritesFragment : BaseStackFragment(), FavoritesMvpView {
    companion object {
        fun newInstance(): FavoritesFragment {
            val fragment = FavoritesFragment()
            return fragment
        }
    }

    @Inject
    lateinit var mFavoritesPresenter: FavoritesMvpPresenter<FavoritesMvpView>

    override fun initFields() {

    }

    override fun initViews() {

    }
}