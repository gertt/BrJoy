package com.browsejoy.games.app.view.fragments.video

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.browsejoy.games.R
import com.facebook.ads.Ad
import com.facebook.ads.AdError
import com.facebook.ads.RewardData
import com.facebook.ads.RewardedVideoAd
import com.facebook.ads.S2SRewardedVideoAdListener

class RewardedVideoFragment : Fragment(), S2SRewardedVideoAdListener {

    val TAG = javaClass.canonicalName

    companion object {
        fun newInstance(): RewardedVideoFragment {
            return RewardedVideoFragment()
        }
    }

    private var rewardedVideoAdStatusLabel: TextView? = null
    private var loadRewardedVideoButton: Button? = null
    private var showRewardedVideoButton: Button? = null

    private var rewardedVideoAd: RewardedVideoAd? = null
    var doneCallback : (() -> Unit)? = null


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_rewarded_video, container, false)
        if (rewardedVideoAd != null) {
            rewardedVideoAd!!.destroy()
            rewardedVideoAd = null
        }
        rewardedVideoAd = RewardedVideoAd(this@RewardedVideoFragment.activity,
                getString(R.string.facebook_interstitial_ad_id))
        rewardedVideoAd!!.setAdListener(this@RewardedVideoFragment)
        rewardedVideoAd!!.loadAd(true)
        rewardedVideoAd!!.setRewardData(RewardData("YOUR_USER_ID", "YOUR_REWARD"))

        Log.d(TAG,"Loading rewarded video ad...")

        return view
    }

    override fun onError(ad: Ad, error: AdError) {
        if (ad === rewardedVideoAd) {
            Log.d(TAG,"Rewarded video ad failed to load: " + error.errorMessage)
            doneCallback?.invoke()
        }
    }

    override fun onAdLoaded(ad: Ad) {
        if (ad === rewardedVideoAd) {
            rewardedVideoAd!!.show()
            //Log.d(TAG,"Ad loaded. Click show to present!")
        }
    }

    override fun onAdClicked(ad: Ad) {
        Log.d(TAG,"Rewarded Video Clicked")
    }


    override fun onRewardedVideoCompleted() {
        Log.d(TAG,"Rewarded Video View Complete")
    }

    override fun onLoggingImpression(ad: Ad) {
        Log.d(TAG,"Rewarded Video Impression")
    }

    override fun onRewardedVideoClosed() {
        Log.d(TAG,"Rewarded Video Closed")
        doneCallback?.invoke()
    }

    override fun onRewardServerFailed() {
        Log.d(TAG,"Reward Video Server Failed")
    }

    override fun onRewardServerSuccess() {
        Log.d(TAG,"Reward Video Server Succeeded")
    }
}
