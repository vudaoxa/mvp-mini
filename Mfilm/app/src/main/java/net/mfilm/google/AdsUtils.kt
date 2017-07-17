package net.mfilm.google

import android.content.Context
import android.support.v4.util.SparseArrayCompat
import com.google.android.gms.ads.*
import net.mfilm.R
import net.mfilm.di.AppContext
import net.mfilm.utils.rand
import timber.log.Timber

/**
 * Created by tusi on 2/17/17.
 */

fun ads(mInterstitialAd: InterstitialAd?, f: (() -> Unit)? = null) {
    val rand = rand(MAX_ADS)
    Timber.e("---ads--------x--- $rand ------------------")
    if (rand < MIN_ADS) {
        f?.invoke()
    } else {
        val show = showInterAds(mInterstitialAd)
        Timber.e("--ads----show --------- $show-----------------------")
        if (!show) {
            f?.invoke()
        }
    }
}

fun initAds(@AppContext context: Context) {
    context.apply {
        MobileAds.initialize(this, getString(R.string.banner_ad_unit_id))
        MobileAds.initialize(this, getString(R.string.inter_ad_unit_id))
    }
    initMapAdsErrors()
}

private const val MAX_ADS = 7
private const val MIN_ADS = 4
const val PAGES_PER_AD = 4
//var time = -1L
//val DURATION = 60000
//val NATIVE_EXPRESS_AD_LARGE_HEIGHT = 300
//val NATIVE_EXPRESS_AD_HEIGHT = 100
val adRequest = AdRequest.Builder().build()

//val madSize = AdSize(AdSize.FULL_WIDTH, NATIVE_EXPRESS_AD_HEIGHT)
fun loadBannerAds(mAdView: AdView?, adBannerListener: AdListener?) {
    Timber.e("loadBannerAds-----$mAdView-----------")
    mAdView?.run {
        adListener = adBannerListener
        requestNewBanner(this)
    }
}

//val mapAdsErrors = SparseArray<String>()
private val mapAdsErrors = SparseArrayCompat<String>()

private fun initMapAdsErrors() {
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
    mInterstitialAd?.loadAd(adRequest)
}

fun requestNewBanner(mAdView: AdView?) {
    mAdView?.loadAd(adRequest)
}

private fun showInterAds(mInterstitialAd: InterstitialAd?): Boolean {
    return mInterstitialAd?.run {
        val loaded = isLoaded
        Timber.e("---showInterAds-----loaded-------$loaded---------------------")
//        if (loaded)
//            show()
//        else
//            requestNewInterstitial(this)
        loaded
    } ?: false
}

val adBannerListener = object : AdListener() {
    override fun onAdClosed() {
        sendHit(CATEGORY_ACTION, ACTION_onAdClosed)
    }

    override fun onAdFailedToLoad(p0: Int) {
        Timber.e("--onAdFailedToLoad--------------------------------------------")
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
    abstract fun fClosed()
    abstract fun fFailedToLoaded()
    abstract fun fLoaded()
    override fun onAdClosed() {
        sendHit(CATEGORY_ACTION, ACTION_onAdClosed)
        fClosed()
    }

    override fun onAdFailedToLoad(p0: Int) {
        sendHit(CATEGORY_ACTION, mapAdsErrors.get(p0) as String)
        fFailedToLoaded()
    }

    override fun onAdOpened() {
        sendHit(CATEGORY_ACTION, ACTION_onAdOpened)
    }

    override fun onAdLoaded() {
        sendHit(CATEGORY_ACTION, ACTION_onAdLoaded)
        fLoaded()
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