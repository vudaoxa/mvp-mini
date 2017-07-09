package net.mfilm.google

import android.content.Context
import android.util.SparseArray
import com.google.android.gms.ads.*
import net.mfilm.R
import timber.log.Timber

/**
 * Created by tusi on 2/17/17.
 */
fun initAds(context: Context) {
    context.apply {
        MobileAds.initialize(this, getString(R.string.banner_ad_unit_id))
        MobileAds.initialize(this, getString(R.string.inter_ad_unit_id))
    }
    initMapAdsErrors()
}

val MAX_ADS = 7
val MIN_ADS = 4
val MIN_ITEMS_2NATIVE_LARGE_ADS = 1
val ITEMS_PER_AD = 5
var time = -1L
val DURATION = 60000
val NATIVE_EXPRESS_AD_LARGE_HEIGHT = 300
val NATIVE_EXPRESS_AD_HEIGHT = 100
val adRequest = AdRequest.Builder().build()
val madSize = AdSize(AdSize.FULL_WIDTH, NATIVE_EXPRESS_AD_HEIGHT)
fun loadBannerAds(mAdView: AdView?) {
    Timber.e("loadBannerAds-----$mAdView-----------")
    mAdView?.run {
        loadAd(adRequest)
        adListener = adBannerListener
    }
}

val mapAdsErrors = SparseArray<String>()
fun initMapAdsErrors() {
    mapAdsErrors.put(AdRequest.ERROR_CODE_INTERNAL_ERROR, "ERROR_CODE_INTERNAL_ERROR")
    mapAdsErrors.put(AdRequest.ERROR_CODE_INVALID_REQUEST, "ERROR_CODE_INVALID_REQUEST")
    mapAdsErrors.put(AdRequest.ERROR_CODE_NETWORK_ERROR, "ERROR_CODE_NETWORK_ERROR")
    mapAdsErrors.put(AdRequest.ERROR_CODE_NO_FILL, "ERROR_CODE_NO_FILL")
}

fun initInterAds(context: Context, adListener: AdListener): InterstitialAd? {
    val mInterstitialAd = InterstitialAd(context)
    mInterstitialAd.apply {
        adUnitId = context.getString(R.string.inter_ad_unit_id)
        this.adListener = adListener
    }
    requestNewInterstitial(mInterstitialAd)
    return mInterstitialAd
}

fun initNativeAds(mAdView: NativeExpressAdView) {
    mAdView.apply {
        videoOptions = VideoOptions.Builder()
                .setStartMuted(true)
                .build()
        videoController?.apply {
            videoLifecycleCallbacks = object : VideoController.VideoLifecycleCallbacks() {
                override fun onVideoEnd() {
                    super.onVideoEnd()
                    sendHit(CATEGORY_ACTION, ACTION_onNativeAdsVideoPlayFinished)
                }
            }

            adListener = object : AdListener() {
                override fun onAdLoaded() {
                    if (hasVideoContent()) {
                        sendHit(CATEGORY_ACTION, ACTION_onNativeAdsHasVideo)
                    } else {
                        sendHit(CATEGORY_ACTION, ACTION_onAdLoaded)
                    }
                }

                override fun onAdClosed() {
                    Timber.e("onAdClosed--------------")
                    sendHit(CATEGORY_ACTION, ACTION_onNativeAdLargeClosed)
                }

                override fun onAdFailedToLoad(p0: Int) {
                    sendHit(CATEGORY_ACTION, mapAdsErrors.get(p0) as String)
                }

                override fun onAdOpened() {
                    sendHit(CATEGORY_ACTION, ACTION_onAdOpened)
                }

                override fun onAdLeftApplication() {
                    sendHit(CATEGORY_ACTION, ACTION_onAdLeftApplication)
                }
            }
        }
    }
}

fun requestNewInterstitial(mInterstitialAd: InterstitialAd?) {
    val adRequest = AdRequest.Builder().build()
    mInterstitialAd?.loadAd(adRequest)
}

fun showInterAds(mInterstitialAd: InterstitialAd?): Boolean {
    mInterstitialAd?.apply {
        if (isLoaded) {
            show()
            return true
        } else return false
    }
    return false
}

val adBannerListener = object : AdListener() {
    override fun onAdClosed() {
        sendHit(CATEGORY_ACTION, ACTION_onAdClosed)
    }

    override fun onAdFailedToLoad(p0: Int) {
        sendHit(CATEGORY_ACTION, mapAdsErrors.get(p0) as String)
    }

    override fun onAdOpened() {
        sendHit(CATEGORY_ACTION, ACTION_onAdOpened)
    }

    override fun onAdLoaded() {
        sendHit(CATEGORY_ACTION, ACTION_onAdLoaded)
    }

    override fun onAdLeftApplication() {
        sendHit(CATEGORY_ACTION, ACTION_onAdLeftApplication)
    }
}

val nativeAdListener = object : AdListener() {
    override fun onAdLoaded() {
        sendHit(CATEGORY_ACTION, ACTION_onNativeAdLoaded)
    }

    override fun onAdClosed() {
        Timber.e("onAdClosed--------------")
        sendHit(CATEGORY_ACTION, ACTION_onNativeAdLargeClosed)
    }

    override fun onAdFailedToLoad(p0: Int) {
        sendHit(CATEGORY_ACTION, "native_" + (mapAdsErrors.get(p0) as String))
    }

    override fun onAdOpened() {
        sendHit(CATEGORY_ACTION, ACTION_onNativeAdOpened)
    }

    override fun onAdLeftApplication() {
        sendHit(CATEGORY_ACTION, ACTION_onAdLeftApplication)
    }
}

abstract class IAdListener() : AdListener() {
    abstract fun iAction()
    override fun onAdClosed() {
        sendHit(CATEGORY_ACTION, ACTION_onAdClosed)
        iAction()
    }

    override fun onAdFailedToLoad(p0: Int) {
        sendHit(CATEGORY_ACTION, mapAdsErrors.get(p0) as String)
    }

    override fun onAdOpened() {
        sendHit(CATEGORY_ACTION, ACTION_onAdOpened)
    }

    override fun onAdLoaded() {
        sendHit(CATEGORY_ACTION, ACTION_onAdLoaded)
    }

    override fun onAdLeftApplication() {
        sendHit(CATEGORY_ACTION, ACTION_onAdLeftApplication)
    }
}

val nativeAdsListener = object : AdListener() {
    override fun onAdClosed() {
        Timber.e("onAdClosed--------------")
        sendHit(CATEGORY_ACTION, ACTION_onNativeAdLargeClosed)
    }

    override fun onAdFailedToLoad(p0: Int) {
        sendHit(CATEGORY_ACTION, mapAdsErrors.get(p0) as String)
    }

    override fun onAdOpened() {
        sendHit(CATEGORY_ACTION, ACTION_onAdOpened)
    }

    override fun onAdLoaded() {
        sendHit(CATEGORY_ACTION, ACTION_onAdLoaded)
    }

    override fun onAdLeftApplication() {
        sendHit(CATEGORY_ACTION, ACTION_onAdLeftApplication)
    }
}