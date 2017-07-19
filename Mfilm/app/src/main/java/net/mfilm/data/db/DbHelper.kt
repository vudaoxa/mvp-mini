package net.mfilm.data.db

import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.realm.RealmList
import io.realm.RealmModel
import io.realm.RealmObject
import io.realm.RealmResults
import net.mfilm.data.db.models.ChapterRealm
import net.mfilm.data.db.models.MangaFavoriteRealm
import net.mfilm.data.db.models.MangaHistoryRealm
import net.mfilm.data.db.models.SearchQueryRealm
import net.mfilm.data.network_retrofit.Chapter
import net.mfilm.data.network_retrofit.Manga

/**
 * Created by tusi on 3/29/17.
 */
interface DbHelper {
    fun loadSearchHistory(observer: DisposableObserver<RealmResults<SearchQueryRealm>>? = null): Disposable
    fun loadFavorites(observer: DisposableObserver<RealmResults<MangaFavoriteRealm>>? = null): Disposable
    fun loadHistory(observer: DisposableObserver<RealmResults<MangaHistoryRealm>>? = null): Disposable
    fun isFavorite(id: Int): MangaFavoriteRealm?
    fun requestMangaHistory(id: Int): MangaHistoryRealm?
    fun requestChaptersHistory(id: Int, observer: DisposableObserver<RealmList<ChapterRealm>>? = null): Disposable?
    //    fun saveChapterHistory(chapter: Chapter, position: Int)
    fun saveChapterHistory(chapter: Chapter)

    //    fun saveReadingChapter(chapter: Chapter, position: Int)
    fun saveReadingChapter(chapter: Chapter)
    fun saveReadingPage(chapter: Chapter, page: Int)
    fun delete(clazz: Class<out RealmModel>)
    fun saveMangaHistory(manga: Manga)
    fun saveObject(obj: RealmObject)
    fun saveObjects(objs: List<RealmObject>)
    fun realmClose()
}