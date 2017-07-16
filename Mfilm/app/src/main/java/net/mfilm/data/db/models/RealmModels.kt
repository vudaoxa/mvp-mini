package net.mfilm.data.db.models

import io.realm.RealmList
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
        var favorite: Boolean = false
) : RealmObject()

open class MangaHistoryRealm(
        @PrimaryKey
        var id: Int? = null,
        var name: String? = null,
        var coverUrl: String? = null,
        var time: Long? = 0,
        var history: Boolean = false,
        var readChaptersIds: RealmList<ChapterRealm> = RealmList<ChapterRealm>(),
        var readingChapterId: Int = 0,
        var readingChapterPosition: Int = 0,
        var readingPage: Int = 0
) : RealmObject()

open class SearchQueryRealm(@PrimaryKey var query: String? = null,
                            var time: Long? = 0, var status: Boolean = false) : RealmObject() {
    override fun toString(): String {
        return query!!
    }
}

open class ChapterRealm(@PrimaryKey var id: Int = 0) : RealmObject()