package net.mfilm.data.db.daos

/**
 * Created by tusi on 6/16/17.
 */
abstract class BaseDao {
    abstract fun clearDb()
}

class MangasDaoFav : BaseDao(), IDaoFav {
    override fun storeOrUpdateFav() {

    }

    override fun clearDb() {

    }
}