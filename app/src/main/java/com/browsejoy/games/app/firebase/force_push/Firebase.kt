package com.browsejoy.games.app.firebase.force_push

import android.app.Activity
import android.content.Context
import android.util.Log
import com.browsejoy.games.BuildConfig
import com.browsejoy.games.R
import com.browsejoy.games.app.utils.OnListPass
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList

object Firebase {

    internal var mFirebaseRemoteConfig: FirebaseRemoteConfig?
    internal var configSettings: FirebaseRemoteConfigSettings
    internal var cacheExpiration: Long = 43200
    internal var onListPass: OnListPass

    fun FirebaseConfig(): ??? {
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
        configSettings = FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build()
        mFirebaseRemoteConfig.setConfigSettings(configSettings)
        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults)
    }

    fun fetchTvContent(context: Context): ArrayList<String> {
        val list = ArrayList<String>()
        mFirebaseRemoteConfig.fetch(getCacheExpiration())
                .addOnCompleteListener(context as Activity) { task ->
                    // If is successful, activated fetched
                    if (task.isSuccessful) {
                        mFirebaseRemoteConfig.activateFetched()
                    } else {
                        Log.d("", "")
                    }
                    var jsonObj = JSONObject()
                    val s = mFirebaseRemoteConfig.getString("video_content_list")
                    try {
                        jsonObj = JSONObject(s)

                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                    try {
                        val data = jsonObj.getJSONArray("data")
                        for (i in 0 until data.length()) {
                            val value = data.getJSONObject(i).get("url").toString()
                            list.add(value)
                        }
                        onListPass.onListPass(list)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
        return list
    }

    fun fetchCallback(context: Context): ArrayList<String> {
        val list = ArrayList<String>()
        mFirebaseRemoteConfig.fetch(getCacheExpiration())
                .addOnCompleteListener(context as Activity) { task ->
                    // If is successful, activated fetched
                    if (task.isSuccessful) {
                        mFirebaseRemoteConfig.activateFetched()
                    } else {
                        Log.d("", "")
                    }
                    var jsonObj = JSONObject()
                    val s = mFirebaseRemoteConfig.getString("video_add_tags")
                    try {
                        jsonObj = JSONObject(s)

                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                    try {
                        val data = jsonObj.getJSONArray("video_ad_tags")
                        for (i in 0 until data.length()) {
                            val value = data.get(i).toString()
                            list.add(value)

                        }
                        onListPass.onListPass(list)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
        return list
    }

    fun getCacheExpiration(): Long {
        // If is developer mode, cache expiration set to 0, in order to test
        if (mFirebaseRemoteConfig.info.configSettings.isDeveloperModeEnabled) {
            cacheExpiration = 0
        }
        return cacheExpiration
    }

    fun setOnListener(oNListPass: OnListPass) {
        onListPass = oNListPass
    }
}