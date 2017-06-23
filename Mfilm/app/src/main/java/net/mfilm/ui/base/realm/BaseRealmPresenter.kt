package net.mfilm.ui.base.realm

import io.reactivex.disposables.CompositeDisposable
import net.mfilm.data.DataManager
import net.mfilm.ui.base.BasePresenter
import net.mfilm.ui.base.MvpView
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by MRVU on 6/23/2017.
 */
open class BaseRealmPresenter<V : MvpView> @Inject
constructor(dataManager: DataManager, compositeDisposable: CompositeDisposable)
    : BasePresenter<V>(dataManager, compositeDisposable) {
    override fun onDetach() {
        Timber.e("--------------onDetach------------------")
        dataManager.realmClose()
        super.onDetach()
    }
}