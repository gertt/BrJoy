package com.browsejoy.games.app.networks

import android.content.Context
import android.content.Intent
import com.adgem.android.AdGem
import com.adgem.android.AdGemCallback
import com.adgem.android.PlayerMetadata
import com.adscendmedia.sdk.ui.OffersActivity
import com.browsejoy.games.R
import java.util.*


public class AdscendProvider {

    fun startAdscendOfferWall(context : Context, userId : String) {
        val options = Hashtable<String, String>()
        val publisherId = context.getString(R.string.adscend_pub_id)
        val adwallId = context.getString(R.string.adscend_adwall_id)
        val intent = OffersActivity.getIntentForOfferWall(context, publisherId, adwallId, userId, options)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
}


