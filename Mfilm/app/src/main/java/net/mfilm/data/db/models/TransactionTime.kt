package net.mfilm.data.db.models

/**
 * Created by chronvas on 30/9/2016.
 */

class TransactionTime {
    var start: Long = 0
    var end: Long = 0
    val duration: Long
        get() = end - start
}
