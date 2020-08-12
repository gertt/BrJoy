package com.browsejoy.games.app.view.activities.splash_screen;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import com.browsejoy.games.R;
import com.browsejoy.games.app.data.prefs.SaveData;
import com.browsejoy.games.app.oauth.Oauth;
import com.browsejoy.games.app.view.activities.main.MainActivity;


public class SplashScreen extends AppCompatActivity implements  SplashScreenInterface {

    SaveData saveData;

    SplashScreenPresenter splashScreenPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        splashScreenPresenter  = new SplashScreenPresenter(this);

        splashScreenPresenter.checkPlayStore();

    }

    @Override
    public void goMenu() {

        saveData = new SaveData(getApplicationContext());
        if(!saveData.getIdToken().equalsIgnoreCase("")){
            Intent intent  = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();

        }else{
            new Handler().postDelayed(new Runnable(){
                @Override
                public void run() {

                    Intent intent = new Intent(getApplicationContext(), Oauth.class);
                    startActivity(intent);
                    finish();


                }
            }, 2000);
        }

    }
}
