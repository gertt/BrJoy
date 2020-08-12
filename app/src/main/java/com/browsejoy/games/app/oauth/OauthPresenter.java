package com.browsejoy.games.app.oauth;

import android.content.Context;
import com.browsejoy.games.app.data.prefs.SaveData;

/**
 * Created by Gerti on 8/23/2018.
 */

public class OauthPresenter {

    Context ctx;
    SaveData saveData;
    Oauth oauth;

    public OauthPresenter(Context ctx,Oauth oauth) {
        this.ctx = ctx;
        this.oauth = oauth;
        saveData = new SaveData(ctx);
    }

    public void chekWorkflow() {
        if (saveData.getScreen().equalsIgnoreCase("")){

            oauth.goToPrivacy();

        }else if (saveData.getScreen().equalsIgnoreCase("1")){

            oauth.goToMain();
        }
    }
}
