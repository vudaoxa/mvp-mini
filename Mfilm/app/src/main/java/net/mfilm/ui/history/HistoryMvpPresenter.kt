package net.mfilm.ui.history

import net.mfilm.ui.base.MvpPresenter
import net.mfilm.utils.ICallbackBus

/**
 * Created by tusi on 6/25/17.
 */
interface HistoryMvpPresenter<V : HistoryMvpView> : MvpPresenter<V>, ICallbackBus {
    fun requestHistory()
}