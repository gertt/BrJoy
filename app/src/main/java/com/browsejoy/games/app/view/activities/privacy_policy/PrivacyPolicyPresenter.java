package com.browsejoy.games.app.view.activities.privacy_policy;

import android.content.Context;
import com.browsejoy.games.app.data.prefs.SaveData;

/**
 * Created by Gerti on 9/10/2018.
 */

public class PrivacyPolicyPresenter {

    Context context;
    SaveData saveData ;

    public PrivacyPolicyPresenter(Context context) {

        this.context = context;
        saveData = new SaveData(context);
        saveData.saveScreen("1");
    }
}

