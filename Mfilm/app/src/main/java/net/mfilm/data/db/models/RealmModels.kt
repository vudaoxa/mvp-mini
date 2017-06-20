package net.mfilm.data.db.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by tusi on 6/16/17.
 */
open class MangaRealm(
        @PrimaryKey
        var id: Int? = null,
        var name: String? = null,
        var coverUrl: String? = null,
        var time: Long? = 0,
        var fav: Int? = 0,
        var history: Int? = 0
) : RealmObject()
