package net.mfilm.ui.base.realm

import io.realm.RealmModel

/**
 * Created by MRVU on 7/4/2017.
 */
interface RealmMvpPreseneter {
    fun delete(clazz: Class<out RealmModel>)
}