package com.browsejoy.games.app.view.fragments.earn

import android.content.Context
import com.browsejoy.games.app.data.model.response.EarnDataModel

interface EarnsFragmentPresenterInterface {

    fun createList(context: Context): Array<EarnDataModel>
}