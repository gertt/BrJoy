package com.browsejoy.games.app.utils;

/**
 * Created by Gerti on 12/25/2018.
 */

public class Balance {

    public static String convertToPoints(Double balance){
        final int points_per_dollar = 100*100;
        int pointsBalance = (int) Math.round(new Double(balance*points_per_dollar));
       return String.format("%d", pointsBalance);
    }

}
