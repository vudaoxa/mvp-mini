package net.mfilm.ui.base.realm

import io.realm.RealmObject
import net.mfilm.ui.base.MvpView
import net.mfilm.utils.*

/**
 * Created by MRVU on 7/3/2017.
 */
interface RealmMvpView<V : RealmObject> : MvpView, ICallbackOnClick,
        ICallbackSpanCount, ICallbackEmptyDataViewHolder, ICallbackReceiveOptionsMenu,
        ICallbackLocalSearch, ICallbackEdit, ICallbackSort,
        ICallbackOnLongClick, ICallbackBottomFun, ICallbackRealm<V> {
    val actionSearch: Int
    val actionSort: Int
    val actionEdit: Int
//    fun delete(clazz: Class<out RealmModel>)
}