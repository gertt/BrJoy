package com.browsejoy.games.app.utils;

import android.app.Activity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

/**
 * Created by Gerti on 1/12/2019.
 */

public class CheckPlayStore {

    public boolean isGooglePlayServicesAvailable(Activity activity) {

        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(activity);
        if(status != ConnectionResult.SUCCESS) {
            if(googleApiAvailability.isUserResolvableError(status)) {
                googleApiAvailability.getErrorDialog( activity, status, 2404).show();
            }
            return false;
        }
        return true;
    }
}
