package com.browsejoy.games.app.view.activities.splash_screen;

import com.browsejoy.games.app.utils.CheckPlayStore;

/**
 * Created by Gerti on 1/12/2019.
 */

public class SplashScreenPresenter  implements  SplashScreenPresenterInterface{

    SplashScreen splashScreen;
    CheckPlayStore checkPlayStore;

    public SplashScreenPresenter(SplashScreen splashScreen) {
        this.splashScreen = splashScreen;
        checkPlayStore =  new CheckPlayStore();
    }

    @Override
    public void checkPlayStore() {
        checkPlayStore.isGooglePlayServicesAvailable(splashScreen);

        if (checkPlayStore.isGooglePlayServicesAvailable(splashScreen)==true){

            splashScreen.goMenu();

        }
    }
}
