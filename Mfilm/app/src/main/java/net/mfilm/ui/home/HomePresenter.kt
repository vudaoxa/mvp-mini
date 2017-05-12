package net.mfilm.ui.home

import io.reactivex.disposables.CompositeDisposable
import net.mfilm.data.DataMng
import net.mfilm.ui.base.BasePresenter
import net.mfilm.utils.DebugLog
import javax.inject.Inject

/**
 * Created by Dieu on 09/03/2017.
 */

class HomePresenter<V : HomeMVPView> @Inject
constructor(dataManager: DataMng, compositeDisposable: CompositeDisposable) : BasePresenter<V>(dataManager, compositeDisposable), HomeMvpPresenter<V> {
    override fun getApiHome() {
        DebugLog.d("xyz--getApiHome--")
    }
}
