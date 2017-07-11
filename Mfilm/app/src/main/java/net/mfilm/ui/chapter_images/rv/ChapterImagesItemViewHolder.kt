package net.mfilm.ui.chapter_images.rv

import android.content.Context
import android.net.Uri
import android.view.View
import com.github.piasy.biv.indicator.progresspie.ProgressPieIndicator
import com.github.piasy.biv.loader.ImageLoader
import kotlinx.android.synthetic.main.item_big_image.view.*
import net.mfilm.data.network_retrofit.ChapterImage
import net.mfilm.ui.base.rv.holders.BaseItemViewHolder
import net.mfilm.utils.ICallbackOnClick
import timber.log.Timber
import java.io.File
import java.lang.Exception

/**
 * Created by MRVU on 7/11/2017.
 */
class ChapterImagesItemViewHolder(mContext: Context, type: Int, itemView: View, mCallbackOnclick: ICallbackOnClick?)
    : BaseItemViewHolder(mContext, type, itemView, mCallbackOnclick) {
    override fun bindView(obj: Any?, position: Int) {
        if (obj is ChapterImage) {
            itemView.run {
                Timber.e("----onBindViewHolder---------$position-----${obj.url}-------------------------------")
//                big_image.setImageURI(obj.url)
                big_image.showImage(Uri.parse(obj.url))
                big_image.setProgressIndicator(ProgressPieIndicator())
//                tv_des.text=position.toString()
                setOnClickListener { mCallbackOnClick?.onClick(position, type) }
//                big_image.setImageLoaderCallback(mImageLoaderCallback)
            }
        }
    }

    val mImageLoaderCallback = object : ImageLoader.Callback {
        override fun onFinish() {
            Timber.e("--------onFinish----------------------------------")
        }

        override fun onSuccess(image: File?) {
            Timber.e("--------onSuccess----------------------------------")
        }

        override fun onFail(error: Exception?) {
            Timber.e("---------onFail---------------------------------")
        }

        override fun onCacheHit(image: File?) {
            Timber.e("---------onCacheHit---------------------------------")
        }

        override fun onCacheMiss(image: File?) {
            Timber.e("---------onCacheMiss---------------------------------")
        }

        override fun onProgress(progress: Int) {
            Timber.e("---------onProgress---------------------------------")
        }

        override fun onStart() {
            Timber.e("---------onStart---------------------------------")
        }

    }
}