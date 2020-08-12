package com.browsejoy.games.app.networks

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
import com.adgem.android.AdGem
import com.adgem.android.AdGemCallback
import com.adgem.android.PlayerMetadata
import com.browsejoy.games.R
import com.browsejoy.games.app.data.prefs.SaveData
import com.browsejoy.games.app.view.activities.main.MainActivity
import com.crashlytics.android.Crashlytics


class AdGemProvider : AppCompatActivity()  {



    private val adGem by lazy { AdGem.get() }

    val context = this

    class AdExceptionHandler : Thread.UncaughtExceptionHandler {
        private var activity : Activity? = null
        constructor(bactivity: Activity){
            activity = bactivity
        }
        override fun uncaughtException(thread: Thread, ex: Throwable) {
            Crashlytics.logException(ex.cause)

            var intent : Intent = Intent(activity, MainActivity::class.java)
            activity!!.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        window.setFlags(FLAG_LAYOUT_NO_LIMITS, FLAG_LAYOUT_NO_LIMITS)
        super.onCreate(savedInstanceState)
        saveData = SaveData(this)
        val player = PlayerMetadata.Builder()
                .id(saveData!!.getStoredValue("authId"))
                .build()

        adGem!!.setPlayerMetaData(player)

        setContentView(R.layout.activity_adgem)

        adGem.registerCallback(callback)
        adGem.showOfferWall(this)

        Thread.setDefaultUncaughtExceptionHandler(AdExceptionHandler(this));
    }

    private var saveData : SaveData? = null

    override fun onDestroy() {
        adGem!!.unregisterCallback(callback)

        super.onDestroy()
    }

    var callback: AdGemCallback = object : AdGemCallback {
        override fun onStandardVideoAdStateChanged(newState: Int) {
            // newState will notify a state of a standard video
            // Full list of possible state codes is defined in AdGem class.
        }

        override fun onRewardedVideoAdStateChanged(newState: Int) {
            // newState will notify a state of a rewarded video
            // Full list of possible state codes is defined in AdGem class.
        }

        override fun onStandardVideoComplete() {
            // Notifies that the user has finished watching standard video ad.
        }

        override fun onRewardedVideoComplete() {
            // Reward user for watching a rewarded video ad.
        }

        override fun onOfferWallStateChanged(newState: Int) {
            //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onRewardUser(amount: Int) {
            //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }
}

