package net.mfilm.ui.chapters.rv

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import net.lc.holders.LoadingViewHolder
import net.mfilm.R
import net.mfilm.data.db.models.MangaHistoryRealm
import net.mfilm.data.network_retrofit.Chapter
import net.mfilm.ui.base.rv.adapters.BaseRvLoadMoreAdapter
import net.mfilm.ui.base.rv.holders.TYPE_ITEM
import net.mfilm.utils.ICallbackChaptersHistory
import net.mfilm.utils.ICallbackOnClick
import timber.log.Timber

/**
 * Created by tusi on 5/27/17.
 */
class ChaptersRvAdapter<V : Chapter>(mContext: Context, mData: MutableList<V>?, mCallbackOnClick: ICallbackOnClick)
    : BaseRvLoadMoreAdapter<V>(mContext, mData, mCallbackOnClick), ICallbackChaptersHistory {
    override fun isMainItem(item: V): Boolean {
        return item.id != null
    }

    override val loadingItem: V
        get() = Chapter() as V

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            TYPE_ITEM -> {
                val view = LayoutInflater.from(mContext).inflate(R.layout.item_chapter, parent, false)
                return ChaptersItemViewHolder(mContext, viewType, view, mCallbackOnClick, this)
            }
            else -> {
                val view = LayoutInflater.from(mContext).inflate(R.layout.progress_view_small, parent, false)
                return LoadingViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        when (holder) {
            is ChaptersItemViewHolder -> {
                mData?.get(position)?.run {
                    holder.bindView(this, position)
                }
            }
            is LoadingViewHolder -> {
                holder.isIndeterminate = true
            }
        }
    }

    private var historyIds: List<Int>? = null
    private var historyReadingChapterId: Int? = null
    fun updateChaptersHistory(ids: List<Int>) {
        historyIds = ids
        mData?.run {
            val historyIndices = indices.filter { get(it).id in ids }
            Timber.e("----updateChaptersHistory-----${historyIndices}---------------------")
            historyIndices.forEach {
                Timber.e("---historyChapters.withIndex-------------${it}------------------")
                val item = get(it)
                item.run {
                    if (chapterHistory) return
                    chapterHistory = true
                    notifyItemChanged(it)
                }
            }
        }
    }

    private var mMangaHistoryRealm: MangaHistoryRealm? = null
    fun updateReadingChapter(mangaHistoryRealm: MangaHistoryRealm) {
        mMangaHistoryRealm = mangaHistoryRealm
        mData?.run {
            val currentReadingIndex = indices.filter { get(it).reading }.firstOrNull()
            currentReadingIndex?.run {
                val item = get(this)
                if (item.id == mangaHistoryRealm.id) return
                item.reading = false
                notifyItemChanged(this)
            }
            val newReadingIndex = indices.filter { get(it).id == mangaHistoryRealm.readingChapterId }.firstOrNull()
            newReadingIndex?.run {
                val item = get(this)
                item.run {
                    if (reading) return
                    reading = true
                    notifyItemChanged(newReadingIndex)
                }
            }
        }
    }

    override fun initReading(chapter: Chapter) {
        mMangaHistoryRealm?.run {
            if (chapter.id == id) {
                if (chapter.reading) return
                chapter.reading = true
            }
        }
    }

    override fun initChapterHistory(chapter: Chapter) {
        historyIds?.run {
            if (contains(chapter.id))
                chapter.chapterHistory = true
        }
    }

    fun contains(id: Int): Int {
        return mData?.map { it.id }?.indexOf(id) ?: -1
    }

}