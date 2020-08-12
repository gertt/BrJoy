package com.browsejoy.games.app.view.fragments.earn

import android.content.Context
import com.browsejoy.games.app.data.model.response.EarnDataModel

class EarnsFragmentPresenter  :EarnsFragmentPresenterInterface{

    val list: MutableList<EarnDataModel> = ArrayList()

    override fun createList(context: Context): Array<EarnDataModel> {

        val  earnDataModel = EarnDataModel(0,"firstItem","firstItem","firstItem","", {})
        list.add(earnDataModel)

        val  earnDataModel1 = EarnDataModel(
                1,"EARN NOW","30+ pts","Video","",
                {
                   EarnsFragmentAdapter.Utils.startNewActivity(context, TvBetaActivity::class.java)
                }
                )
        list.add(earnDataModel1)

        val earnDataModel2 = EarnDataModel(2,"EARN NOW","1000+ pts","More Offers","",
                {
                    val provider = AdscendProvider()
                    val saveData = SaveData(context)
                    val userId = saveData.getStoredValue("authId")
                    provider.startAdscendOfferWall(context, userId)
                }
        )
        list.add(earnDataModel2)
        val earnDataModel3 = E
        list.add(earnDataModel3)
        return list.toTypedArray()
    }

}