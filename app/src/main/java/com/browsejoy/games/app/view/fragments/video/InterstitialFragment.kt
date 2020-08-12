package com.browsejoy.browsejoy.browsejoy.app.view.fragments.video

import android.support.v4.app.Fragment;
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import com.aerserv.sdk.AerServBanner
import com.aerserv.sdk.AerServConfig
import com.browsejoy.games.R
import com.facebook.ads.*
import kotlinx.android.synthetic.main.fragment_interstitial.view.*

class InterstitialFragment : Fragment(), AdListener {

    val TAG = javaClass.canonicalName

    private var rectangleAdContainer: RelativeLayout? = null
    private var rectangleAdView: AdView? = null
    private var aerServBanner: AerServBanner? = null

    companion object {
        fun newInstance(): InterstitialFragment {
            return InterstitialFragment()
        }
    }

    var doneCallback : (() -> Unit)? = null
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        val rootView = inflater.inflate(R.layout.fragment_interstitial, container, false)
        rootView.continue_button.setOnClickListener {
            doneCallback?.invoke()
        }

        rectangleAdContainer = rootView.findViewById(R.id.rectangleAdContainer) as RelativeLayout
        loadAdView()
        setupAerservAdSDK()
        return rootView;
    }

    private fun loadAdView() {
        if (rectangleAdView != null) {
            rectangleAdView!!.destroy()
            rectangleAdView = null
        }
        // Create a banner's ad view with a unique placement ID (generate your own on the Facebook
        // app settings). Use different ID for each ad placement in your app.
        rectangleAdView = AdView(activity!!, getString(R.string.facebook_rectangle_ad_id),
                AdSize.RECTANGLE_HEIGHT_250)

        // Reposition the ad and add it to the view hierarchy.
        rectangleAdContainer!!.addView(rectangleAdView)

        // Set a listener to get notified on changes or when the user interact with the ad.
        rectangleAdView!!.setAdListener(this@InterstitialFragment)

        // Initiate a request to load an ad.
        rectangleAdView!!.loadAd()
    }

    override fun onError(ad: Ad, error: AdError) {
        if (ad === rectangleAdView) {
            Log.e(TAG,"Ad failed to load: " + error.errorMessage)
        }
    }

    override fun onAdLoaded(ad: Ad) {
        if (ad === rectangleAdView) {
            Log.d(TAG, "Loading rectangle ad")
        }
    }

    override fun onAdClicked(ad: Ad) {
        Toast.makeText(this.getActivity(), "Ad Clicked", Toast.LENGTH_SHORT).show()
    }

    override fun onLoggingImpression(ad: Ad) {
        Log.d(TAG, "onLoggingImpression")
    }

    private fun setupAerservAdSDK() {
        loadBannerAd();
    }
    override fun onResume() {
        aerServBanner?.play();
        super.onResume()
    }
    override fun onPause() {
        aerServBanner?.pause();
        super.onPause();
    }
    override fun onDestroy() {
        aerServBanner?.kill();
        super.onDestroy()
    }
    private fun loadBannerAd() {
        try {
            val config = AerServConfig(getActivity()!!, getActivity()!!.getString(R.string.aerserv_app_placement_id))
            aerServBanner = getActivity()!!.findViewById<AerServBanner>(R.id.aerserv_banner)
            aerServBanner!!.configure(config)?.show()
        } catch(e : Exception) {
            Log.e(TAG, e.toString());
        }
    }
}



