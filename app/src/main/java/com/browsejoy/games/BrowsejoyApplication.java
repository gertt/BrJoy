package com.browsejoy.games;

import android.content.Context;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;
import com.browsejoy.games.app.data.prefs.SaveData;
import com.browsejoy.games.app.firebase.force_push.ForceUpdateChecker;
import com.aerserv.sdk.AerServSdk;
import com.facebook.stetho.Stetho;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import static android.content.ContentValues.TAG;

public class BrowsejoyApplication extends MultiDexApplication {

  SaveData saveData;
  public static BrowsejoyApplication instance;

  @Override
  protected void attachBaseContext(Context context) {
    super.attachBaseContext(context);
    MultiDex.install(this);
  }

  private FirebaseRemoteConfig mFirebaseRemoteConfig;

  // Called when the application is starting, before any other application objects have been created.
  // Overriding this method is totally optional!
  @Override
  public void onCreate() {
    super.onCreate();
    Stetho.initializeWithDefaults(this);
    // Required initialization logic here!
    fetchAdInfo();
    initFirebaseRemoteConfig();
    firebaseForceUpdate();
    AerServSdk.init(this, this.getString(R.string.aerserv_app_id));
    instance = this;
  }

  private void initFirebaseRemoteConfig() {
    // Get Remote Config instance.
    // [START get_remote_config_instance]
    mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
    // [END get_remote_config_instance]
    // Create a Remote Config Setting to enable developer mode, which you can use to increase
    // the number of fetches available per hour during development. See Best Practices in the
    // README for more information.
    // [START enable_dev_mode]
    FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
        .setDeveloperModeEnabled(BuildConfig.DEBUG)
        .build();
    mFirebaseRemoteConfig.setConfigSettings(configSettings);
    // [END enable_dev_mode]
    // Set default Remote Config parameter values. An app uses the in-app default values, and
    // when you need to adjust those defaults, you set an updated value for only the values you
    // want to change in the Firebase console. See Best Practices in the README for more
    // information.
    // [START set_default_values]
    mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults);
    // [END set_default_values]
    long cacheExpiration = 3600; // 1 hour in seconds.
    // If your app is using developer mode, cacheExpiration is set to 0, so each fetch will
    // retrieve values from the service.
    if (mFirebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
      cacheExpiration = 0;
    }
    // [START fetch_config_with_callback]
    // cacheExpirationSeconds is set to cacheExpiration here, indicating the next fetch request
    // will use fetch data from the Remote Config service, rather than cached parameter values,
    // if cached parameter values are more than cacheExpiration seconds old.
    // See Best Practices in the README for more information.
    mFirebaseRemoteConfig.fetch(cacheExpiration)
        .addOnCompleteListener(new OnCompleteListener<Void>() {
          @Override
          public void onComplete(@NonNull Task<Void> task) {
            if (task.isSuccessful()) {
              // CommonUtils.showToast(getApplicationContext(), "Fetch Succeeded");
              // After config data is successfully fetched, it must be activated before newly fetched
              // values are returned.
              mFirebaseRemoteConfig.activateFetched();
            } else {
              //   CommonUtils.showToast(getApplicationContext(), "Fetch Failed");
            }
            //displayWelcomeMessage();
          }
        });
    // [END fetch_config_with_callback]
  }

  // Called by the system when the device configuration changes while your component is running.
  // Overriding this method is totally optional!
  @Override
  public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
  }

  // This is called when the overall system is running low on memory,
  // and would like actively running processes to tighten their belts.
  // Overriding this method is totally optional!
  @Override
  public void onLowMemory() {
    super.onLowMemory();
  }

  protected void fetchAdInfo() {
    saveData = new SaveData(getApplicationContext());
    AsyncTask.execute(new Runnable() {
      @Override
      public void run() {
        try {
          AdvertisingIdClient.Info adInfo = AdvertisingIdClient.getAdvertisingIdInfo(getApplicationContext());
          String adId = adInfo != null ? adInfo.getId() : null;
          // Use the advertising id
          saveData.putStoredValue("adId", adId);
        } catch (IOException | GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException exception) {
          // Error handling if needed
          Log.e("fetchAdInfo", exception.toString());
        }
      }
    });
  }

  public void firebaseForceUpdate() {
    final FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
    // set in-app defaults
    Map<String, Object> remoteConfigDefaults = new HashMap();
    remoteConfigDefaults.put(ForceUpdateChecker.KEY_UPDATE_REQUIRED, true);
    remoteConfigDefaults.put(ForceUpdateChecker.KEY_CURRENT_VERSION, "1.0");
    remoteConfigDefaults.put(ForceUpdateChecker.KEY_UPDATE_URL, "https://www.google.com/search?source=hp&ei=F-iQW4HTK5LekgWOiqKgAw&q=test&oq=test&gs_l=psy-ab.3..35i39k1l2j0i131k1l2j0l6.552.866.0.1573.5.4.0.0.0.0.168.538.0j4.4.0....0...1c.1.64.psy-ab..1.4.538.0...0.Z-nsOTS9rxU");
    firebaseRemoteConfig.setDefaults(remoteConfigDefaults);
    firebaseRemoteConfig.fetch(60) // fetch every minutes
        .addOnCompleteListener(new OnCompleteListener<Void>() {
          @Override
          public void onComplete(@NonNull Task<Void> task) {
            if (task.isSuccessful()) {
              Log.d(TAG, "remote config is fetched.");
              firebaseRemoteConfig.activateFetched();
            }
          }
        });
  }

  @Override
  public Context getApplicationContext() {
    return super.getApplicationContext();
  }
  public static BrowsejoyApplication getInstance() {
    return instance;
  }
}