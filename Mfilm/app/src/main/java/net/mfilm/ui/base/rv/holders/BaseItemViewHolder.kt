package net.mfilm.ui.base.rv.holders

//import net.mfilm.ui.manga.SelectableItem
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import net.mfilm.utils.ICallbackOnClick
import net.mfilm.utils.ICallbackOnLongClick

/**
 * Created by tusi on 4/5/17.
 */
@Suppress("DEPRECATION")
abstract class BaseItemViewHolder(protected val mContext: Context, val type: Int = -1,
                                  itemView: View) : RecyclerView.ViewHolder(itemView) {
    var mCallbackOnClick: ICallbackOnClick? = null
    var mCallbackOnLongClick: ICallbackOnLongClick? = null
    constructor(mContext: Context, type: Int, itemView: View, mCallbackOnClick: ICallbackOnClick?)
            : this(mContext, type, itemView) {
        this.mCallbackOnClick = mCallbackOnClick
    }

    constructor(mContext: Context, type: Int, itemView: View,
                mCallbackOnClick: ICallbackOnClick?, mCallbackOnLongClick: ICallbackOnLongClick? = null)
            : this(mContext, type, itemView, mCallbackOnClick) {
        this.mCallbackOnLongClick = mCallbackOnLongClick
    }

    fun initOnClicked(itemView: View) {
        itemView.run {
            setOnClickListener { mCallbackOnClick?.onClick(position, type) }
            setOnLongClickListener {
                mCallbackOnLongClick?.onLongClick(position, type)
                true
            }
        }
    }

    //    val gestureDetector = GestureDetectorCompat(mContext, object : GestureDetector.SimpleOnGestureListener() {
//        override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
//            tryIt{
//                val detector = SwipeDetector(e1, e2, velocityX, velocityY)
//                Timber.e("------detector-----------${detector.isRightSwipe}----------------------------------")
//            }
//            return true
//        }
//    })
    abstract fun bindView(obj: Any?, position: Int)
}

const val TYPE_ITEM = 0
const val TYPE_ITEM_HEADER = 2
const val TYPE_ITEM_SEARCH_HISTORY = 1
const val TYPE_ITEM_LOADING = -1
const val TYPE_ITEM_FILTER = -2
