package net.mfilm.data.prefs

/**
 * Created by MRVU on 7/7/2017.
 */
open class SettingModel(val titleResId: Int)

class MangaSource(val index: Int, val title: String, val code: String) {
    override fun toString(): String {
        return "--MangaSource-------------$index --------- $title ------------- $code"
    }
}

class MangaSources(titleResId: Int, var sources: List<MangaSource>,
                   var selectedIndex: Int? = 0) : SettingModel(titleResId) {
    fun arrange(pivotIndex: Int) {
        val res = mutableListOf<MangaSource>()
        selectedIndex = pivotIndex
        sources.filterTo(res, { it.index == selectedIndex })
        sources.filterNotTo(res, { it.index == selectedIndex })
        sources = res
    }
}
xx
class SwitchItem(titleResId: Int, var enabled: Boolean)