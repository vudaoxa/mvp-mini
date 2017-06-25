package net.mfilm.ui.history

import net.mfilm.ui.base.MvpPresenter

/**
 * Created by tusi on 6/25/17.
 */
interface HistoryMvpPresenter<V : HistoryMvpView> : MvpPresenter<V> {
    fun requestHistory()
}