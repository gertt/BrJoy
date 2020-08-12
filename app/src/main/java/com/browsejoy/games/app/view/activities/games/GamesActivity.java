package com.browsejoy.games.app.view.activities.games;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import com.browsejoy.games.R;
import com.browsejoy.games.app.utils.MyExceptionHandler;
import com.facebook.ads.Ad;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;

public class GamesActivity extends AppCompatActivity {

    private InterstitialAd interstitialAd;

    public static final String TAG ="hhhhh" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Force restart the app if it crashes
        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this));

        interstitialAd = new InterstitialAd(this, "1874659749297686_1874660245964303");

        interstitialAd.setAdListener(new InterstitialAdListener() {

            @Override
            public void onInterstitialDisplayed(Ad ad) {
                // Interstitial ad displayed callback
                Log.e(TAG, "Interstitial ad displayed.");
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                // Interstitial dismissed callback
                Log.e(TAG, "Interstitial ad dismissed.");
            }

            @Override
            public void onError(Ad ad, com.facebook.ads.AdError adError) {

                String sj =  ad.toString();
                String sjs  = adError.toString();

            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Interstitial ad is loaded and ready to be displayed
                Log.d(TAG, "Interstitial ad is loaded and ready to be displayed!");
                // Show the ad
                interstitialAd.show();
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked callback
                Log.d(TAG, "Interstitial ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Ad impression logged callback
                Log.d(TAG, "Interstitial ad impression logged!");
            }
        });

        // For auto play video ads, it's recommended to load the ad
        // at least 30 seconds before it is shown
        interstitialAd.loadAd();
    }
}