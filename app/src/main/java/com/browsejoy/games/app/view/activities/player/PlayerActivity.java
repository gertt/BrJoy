package com.browsejoy.games.app.view.activities.player;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import com.browsejoy.games.R;
import com.browsejoy.games.app.utils.MyExceptionHandler;
import com.browsejoy.games.app.view.fragments.video.VideoFragment;

/**
 * Created by Gerti on 8/1/2018.
 */

public class PlayerActivity extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        orientVideoDescriptionFragment(getResources().getConfiguration().orientation);

        //Force restart the app if it crashes
        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this));

        Fragment selectedFragment = VideoFragment.newInstance();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame1, selectedFragment);
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        orientVideoDescriptionFragment(configuration.orientation);

    }

    private void orientVideoDescriptionFragment(int orientation) {

    }
}