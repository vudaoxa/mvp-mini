package net.mfilm.data.prefs

/**
 * Created by MRVU on 7/7/2017.
 */
class MangaSource(val index: Int, val titleResId: Int)

class MangaSources(val mangaSources: List<MangaSource>, var selectedIndex: Int)