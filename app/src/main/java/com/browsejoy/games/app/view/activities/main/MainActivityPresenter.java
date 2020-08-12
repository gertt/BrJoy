package com.browsejoy.games.app.view.activities.main;

import android.content.Context;
import com.browsejoy.games.app.data.prefs.SaveData;
/**
 * Created by Gerti on 9/28/2018.
 */

public class MainActivityPresenter implements MainActivityPresenterInterface {

    MainActivity mainActivity;

    Context context;

    SaveData saveData;

    public MainActivityPresenter(MainActivity mainActivity, Context context) {
        this.mainActivity = mainActivity;
        saveData = new SaveData(context);
    }
}
