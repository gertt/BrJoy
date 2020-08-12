package com.browsejoy.games.app.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Gerti on 12/27/2018.
 */

public class TimeByZone {

    public  String timeZone(String time){

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
        df.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
        Date date = null;
        try {
            date = df.parse(time);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return  date.toString();
    }
}
