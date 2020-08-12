package com.browsejoy.games.app.utils;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import com.browsejoy.games.BrowsejoyApplication;
import com.browsejoy.games.app.view.activities.main.MainActivity;
import com.crashlytics.android.Crashlytics;


public class MyExceptionHandler implements Thread.UncaughtExceptionHandler {

    private Activity activity;

    public MyExceptionHandler(Activity a) {
        activity = a;
    }
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {

        Crashlytics.logException(ex.getCause());

        Intent intent = new Intent(activity, MainActivity.class);
        intent.putExtra("crash", true);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(BrowsejoyApplication.getInstance().getBaseContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager mgr = (AlarmManager) BrowsejoyApplication.getInstance().getBaseContext().getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, pendingIntent);
        activity.finish();
        System.exit(2);


    }
}
