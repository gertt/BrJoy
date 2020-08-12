package com.browsejoy.games.app.firebase

import android.content.Context
import android.util.Log
import com.browsejoy.games.BuildConfig
import com.browsejoy.games.R
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import org.json.JSONObject


class FirebaseHelper {

    init {
        setup()
    }
    companion object {
        val TAG = javaClass.canonicalName
        private var firebaseRemoteConfig : FirebaseRemoteConfig? = FirebaseRemoteConfig.getInstance()
        private var firebaseRemoteConfigSettings : FirebaseRemoteConfigSettings? = null
        internal var cacheExpiration: Long = 43200

        data class VideoConfig(val videosPerAd: Int = 3, val maxVideos: Int = 20)
        data class OfferwallConfig(val offerWallTypes: ArrayList<String>)
        private var videoConfig = VideoConfig()
        private var setup : Boolean = false;
        private fun setup() {
            if (!setup) {
                firebaseRemoteConfigSettings = FirebaseRemoteConfigSettings.Builder()
                        .setDeveloperModeEnabled(BuildConfig.DEBUG)
                        .build()
                firebaseRemoteConfig?.setConfigSettings(firebaseRemoteConfigSettings)
                firebaseRemoteConfig?.setDefaults(R.xml.remote_config_defaults)
                setup = true;
            }
        }


        fun fetchVideoConfig(context : Context, finishedCallback: (VideoConfig) -> Unit) {
            setup();
            firebaseRemoteConfig?.fetch(getCacheExpiration())?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    firebaseRemoteConfig?.activateFetched()
                    val maxVideos : Int = firebaseRemoteConfig?.getString("max_videos")!!.toInt()
                    val videosPerAd : Int = firebaseRemoteConfig?.getString("videos_per_ad")!!.toInt();
                    finishedCallback.invoke(
                            VideoConfig(videosPerAd,maxVideos)
                    )
                } else {
                    Log.e(TAG, "Firebase fetch failed with ", task.exception)
                }
            }
        }

        fun fetchOfferwallConfig(context : Context, finishedCallback: (OfferwallConfig) -> Unit) {
            setup();
            firebaseRemoteConfig?.fetch(getCacheExpiration())?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    firebaseRemoteConfig?.activateFetched()
                    val offerwallConfig= JSONObject(firebaseRemoteConfig?.getString("offerwalls"))
                    val offerwallJSONArray = offerwallConfig.getJSONArray("offerwalls")
                    val offerwallTypes = (0..(offerwallJSONArray.length()-1)).map { i ->
                        offerwallJSONArray.getString(i)
                    }

                    finishedCallback.invoke(
                            OfferwallConfig(
                                    offerWallTypes = ArrayList(offerwallTypes)
                            )
                    )
                } else {
                    Log.e(TAG, "Firebase fetch failed with ", task.exception)
                }
            }
        }

        fun getCacheExpiration(): Long {
            // If is developer mode, cache expiration set to 0, in order to test
            if (firebaseRemoteConfig?.getInfo()?.getConfigSettings()!!.isDeveloperModeEnabled()) {
                cacheExpiration = 0
            }
            return cacheExpiration
        }
    }
}
