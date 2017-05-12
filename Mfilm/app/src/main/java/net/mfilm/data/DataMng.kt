package net.mfilm.data

import io.reactivex.Observable

/**
 * Created by tusi on 3/29/17.
 */
interface DataMng {
    fun seedDatabaseQuestions(): Observable<Boolean>
    fun seedDatabaseOptions(): Observable<Boolean>
}