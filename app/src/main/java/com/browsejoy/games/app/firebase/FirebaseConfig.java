package com.browsejoy.games.app.firebase;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import com.browsejoy.games.BuildConfig;
import com.browsejoy.games.R;
import com.browsejoy.games.app.utils.OnListPass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Created by Gerti on 8/9/2018.
 */

public class FirebaseConfig {

  FirebaseRemoteConfig mFirebaseRemoteConfig;
  FirebaseRemoteConfigSettings configSettings;
  long cacheExpiration = 43200;
  OnListPass onListPass;

  public FirebaseConfig() {
    mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
    configSettings = new FirebaseRemoteConfigSettings.Builder()
        .setDeveloperModeEnabled(BuildConfig.DEBUG)
        .build();
    mFirebaseRemoteConfig.setConfigSettings(configSettings);
    mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults);
  }

  public ArrayList<String> fetchTvContent(Context context) {
    final ArrayList<String> list = new ArrayList<>();
    mFirebaseRemoteConfig.fetch(getCacheExpiration())
        .addOnCompleteListener((Activity) context, new OnCompleteListener<Void>() {
          @Override
          public void onComplete(@NonNull Task<Void> task) {
            // If is successful, activated fetched
            if (task.isSuccessful()) {
              mFirebaseRemoteConfig.activateFetched();
            } else {
              Log.d("", "");
            }
            JSONObject jsonObj = new JSONObject();
            String s = mFirebaseRemoteConfig.getString("video_content_list");
            try {
              jsonObj = new JSONObject(s);

            } catch (JSONException e) {
              e.printStackTrace();
            }
            try {
              JSONArray data = jsonObj.getJSONArray("data");
              for (int i = 0; i < data.length(); i++) {
                String value = data.getJSONObject(i).get("url").toString();
                list.add(value);
              }
              onListPass.onListPass(list);
            } catch (JSONException e) {
              e.printStackTrace();
            }

          }
        });
    return list;
  }

  public ArrayList<String> fetchCallback(Context context) {
    final ArrayList<String> list = new ArrayList<>();
    mFirebaseRemoteConfig.fetch(getCacheExpiration())
        .addOnCompleteListener((Activity) context, new OnCompleteListener<Void>() {
          @Override
          public void onComplete(@NonNull Task<Void> task) {
            // If is successful, activated fetched
            if (task.isSuccessful()) {
              mFirebaseRemoteConfig.activateFetched();
            } else {
              Log.d("", "");
            }
            JSONObject jsonObj = new JSONObject();
            String s = mFirebaseRemoteConfig.getString("video_add_tags");
            try {
              jsonObj = new JSONObject(s);

            } catch (JSONException e) {
              e.printStackTrace();
            }
            try {
              JSONArray data = jsonObj.getJSONArray("video_ad_tags");
              for (int i = 0; i < data.length(); i++) {
                String value = data.get(i).toString();
                list.add(value);

              }
              onListPass.onListPass(list);
            } catch (JSONException e) {
              e.printStackTrace();
            }

          }
        });
    return list;
  }

  public long getCacheExpiration() {
    // If is developer mode, cache expiration set to 0, in order to test
    if (mFirebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
      cacheExpiration = 0;
    }
    return cacheExpiration;
  }

  public void setOnListener(OnListPass oNListPass) {
    onListPass = oNListPass;
  }

}
