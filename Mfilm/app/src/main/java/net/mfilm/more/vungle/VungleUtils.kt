package net.mfilm.more.vungle

import android.content.Context
import com.vungle.publisher.VungleAdEventListener
import com.vungle.publisher.VungleInitListener
import com.vungle.publisher.VunglePub
import timber.log.Timber

/**
 * Created by tusi on 7/21/17.
 */
val vunglePub = VunglePub.getInstance()
internal val app_id = "5970d5f593062a7656004221"
internal val DEFAULT_PLACEMENT_ID = "DEFAULT93295"
private val placement_list = arrayOf(DEFAULT_PLACEMENT_ID, "PLACEME03059")
fun loadAds() {
    vunglePub.run {
        if (!isInitialized) return
        loadAd(placement_list[1])
    }
}

fun playAds() {
    vunglePub.run {
        if (!isInitialized) return
        placement_list.run {
            for (i in indices) {
                if (isAdPlayable(this[i])) {
                    playAd(this[i], null)
                    break
                } else {
                    Timber.i("----- !isAdPlayable------ ${this[i]}---------------------")
                }
            }
        }
    }
}

fun initVungle(mContext: Context) {
    vunglePub.init(mContext, app_id, placement_list, object : VungleInitListener {
        override fun onSuccess() {
            Timber.i("---------------init success-----------------")
            vunglePub.clearAndSetEventListeners(vungleDefaultListener)

//            mContext.runOnUiThread(Runnable {
//                setButtonState(initButton, false)
//                for (i in 0..2) {
//                    setButtonState(play_buttons[i], vunglePub.isAdPlayable(placement_list[i]))
//                    setButtonState(load_buttons[i], !vunglePub.isAdPlayable(placement_list[i]))
//                }
//            })
        }

        override fun onFailure(error: Throwable) {
            Timber.i("---------------init failure-----------------")
        }
    })


}

private val vungleDefaultListener = object : VungleAdEventListener {

    override fun onAdEnd(placementReferenceId: String, wasSuccessFulView: Boolean, wasCallToActionClicked: Boolean) {
        // Called when user exits the ad and control is returned to your application
        // if wasSuccessfulView is true, the user watched the ad and could be rewarded
        // if wasCallToActionClicked is true, the user clicked the call to action button in the ad.
        Timber.i("onAdEnd: $placementReferenceId ,wasSuccessfulView: $wasSuccessFulView ,wasCallToActionClicked: $wasCallToActionClicked")

    }

    override fun onAdStart(placementReferenceId: String) {
        // Called before playing an ad
        Timber.i("onAdStart: " + placementReferenceId)
    }

    override fun onUnableToPlayAd(placementReferenceId: String, reason: String) {
        // Called after playAd(placementId, adConfig) is unable to play the ad
        Timber.i("onUnableToPlayAd: $placementReferenceId ,reason: $reason")
    }

    override fun onAdAvailabilityUpdate(placementReferenceId: String, isAdAvailable: Boolean) {

        // Notifies ad availability for the indicated placement
        // There can be duplicate notifications
        Timber.i("onAdAvailabilityUpdate: $placementReferenceId isAdAvailable: $isAdAvailable")

//        val enabled = isAdAvailable
//        val placementIdUpdated = placementReferenceId
//            runOnUiThread(Runnable {
//                if (placementIdUpdated == placement_list[0]) {
//                    setButtonState(play_buttons[0], enabled)
//                } else if (placementIdUpdated == placement_list[1]) {
//                    setButtonState(play_buttons[1], enabled)
//                    setButtonState(load_buttons[1], !enabled)
//                } else if (placementIdUpdated == placement_list[2]) {
//                    setButtonState(play_buttons[2], enabled)
//                    setButtonState(load_buttons[2], !enabled)
//                }
//            })
    }
}