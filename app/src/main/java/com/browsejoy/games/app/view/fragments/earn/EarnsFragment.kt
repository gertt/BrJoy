package com.browsejoy.games.app.view.fragments.earn

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.browsejoy.games.R
import com.browsejoy.games.app.data.model.response.EarnDataModel
import com.browsejoy.games.app.data.prefs.SaveData
import com.browsejoy.games.app.firebase.FirebaseHelper
import com.browsejoy.games.app.networks.AdGemProvider
import com.browsejoy.games.app.networks.AdscendProvider
import com.browsejoy.games.app.view.activities.tv.TvBetaActivity
import java.lang.RuntimeException


class EarnsFragment : Fragment()  {

    val TAG = "EarnsFragment"

    private lateinit var recyclerView: RecyclerView

    lateinit var earnsFragmentPresenter: EarnsFragmentPresenter

    companion object {
        fun newInstance(): EarnsFragment {
            return EarnsFragment()
        }
    }

    class Offerwalls {
        interface OfferwallType {
            val name : String
            val earnDataModel : EarnDataModel
        }
        class VideoOfferwall(context : Context, index : Int)  : OfferwallType {
            override val name: String = "VIDEOS"
            override val earnDataModel: EarnDataModel = EarnDataModel(
                    index,"EARN NOW","30+ pts","Video","",
                    {
                        EarnsFragmentAdapter.Utils.startNewActivity(context, TvBetaActivity::class.java)
                    }
            )
        }
        class AdscendOfferwall(context : Context, index : Int) : OfferwallType {
            override val name: String = "ADSCEND"
            override val earnDataModel: EarnDataModel = EarnDataModel(index,"EARN NOW","1000+ pts","More Offers","",
                    {
                        val provider = AdscendProvider()
                        val saveData = SaveData(context)
                        val userId = saveData.getStoredValue("authId")
                        provider.startAdscendOfferWall(context, userId)
                    }
            )
        }
        class AdGemOfferwall(context : Context, index : Int)  : OfferwallType {
            override val name: String = "ADGEM"
            override val earnDataModel: EarnDataModel = EarnDataModel(index,"EARN NOW","1000+ pts","More Offers Continued","",
                    {
                        EarnsFragmentAdapter.Utils.startNewActivity(context, AdGemProvider::class.java)
                    }
            )
        }
        companion object {
            fun getByTypeName(context : Context, offerwallName : String, index : Int) : OfferwallType {
                if ("VIDEOS" == offerwallName) {
                    return VideoOfferwall(context, index)
                }
                if ("ADSCEND" == offerwallName) {
                    return AdscendOfferwall(context, index)
                }
                if ("ADGEM" == offerwallName) {
                    return AdGemOfferwall(context, index)
                } else {
                    throw RuntimeException("Trying to load offerwall that doesn't exist")
                }
            }
        }
    }
    private val DEFAULT_OFFERLIST = listOf("VIDEOS", "ADSCEND", "ADGEM")

    private fun setupFirebaseCallbackForOfferwall(context :Context ) {
        try {
            FirebaseHelper.fetchOfferwallConfig(context) { (offerWallTypes) ->
                generateOfferwallModels(offerWallTypes)
                loadAdapter(generateOfferwallModels(offerWallTypes).toList())
            }
        } catch (exception: Exception) {
            Log.e(TAG, exception.toString())
            loadAdapter(generateOfferwallModels(DEFAULT_OFFERLIST).toList());
        }

    }

    private fun loadAdapter(offerwalls : List<EarnDataModel>) {
        recyclerView.adapter = EarnsFragmentAdapter(activity!!.applicationContext, listViewType = offerwalls.toTypedArray())
    }

    private fun generateOfferwallModels(offerwalls: List<String>) : Array<EarnDataModel> {
        var offerwallIndex = 0
        val emptyEarnModel = EarnDataModel(offerwallIndex++,"firstItem","firstItem","firstItem","", {})

        return (listOf(emptyEarnModel) + offerwalls.map { offerwallName ->
           val offerwall = Offerwalls.Companion.getByTypeName(context!!, offerwallName, offerwallIndex++)
            offerwall.earnDataModel
        }).toTypedArray()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setupFirebaseCallbackForOfferwall(context!!);
        val rootView =  inflater.inflate(R.layout.earns_fragment,  null, false)

        recyclerView = rootView.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)

        earnsFragmentPresenter = EarnsFragmentPresenter()

        return rootView
    }
}

