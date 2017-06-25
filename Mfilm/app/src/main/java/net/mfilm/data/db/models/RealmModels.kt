package net.mfilm.data.db.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by tusi on 6/16/17.
 */
open class MangaFavoriteRealm(
        @PrimaryKey
        var id: Int? = null,
        var name: String? = null,
        var coverUrl: String? = null,
        var time: Long? = 0,
        var fav: Boolean = false
) : RealmObject()

open class MangaHistoryRealm(
        @PrimaryKey
        var id: Int? = null,
        var name: String? = null,
        var coverUrl: String? = null,
        var time: Long? = 0,
        var history: Boolean = false
) : RealmObject()

open class SearchQueryRealm(@PrimaryKey var query: String? = null,
                            var time: Long? = 0) : RealmObject() {
    override fun toString(): String {
        return query!!
    }
}
