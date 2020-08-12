package com.browsejoy.games.app.data.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.auth0.android.authentication.AuthenticationAPIClient;
import com.auth0.android.authentication.storage.CredentialsManager;
import com.auth0.android.authentication.storage.CredentialsManagerException;
import com.auth0.android.authentication.storage.SharedPreferencesStorage;
import com.auth0.android.callback.BaseCallback;
import com.auth0.android.result.Credentials;

/**
 * Created by Gerti on 1/22/18.
 */

public class SaveData {

    Context ctx;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    private CredentialsManager manager;
    private Context context;
    private AuthenticationAPIClient apiClient;
    String idToken = "";

    public SaveData(Context ctx1){
        this.ctx = ctx1;
        preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        editor = preferences.edit();
        manager = new CredentialsManager(apiClient, new SharedPreferencesStorage(ctx));
    }

    public SaveData(Context context, AuthenticationAPIClient apiClient,Credentials credentials){
        manager = new CredentialsManager(apiClient, new SharedPreferencesStorage(context));
        this.context = context;
        this.apiClient = apiClient;
        manager.saveCredentials(credentials);
    }

    public String getIdToken(){

       if(manager.hasValidCredentials() == true){
           manager.getCredentials(new BaseCallback<Credentials, CredentialsManagerException>() {
               @Override
               public void onSuccess(final Credentials credentials) {

                   idToken = credentials.getType()+" "+credentials.getIdToken();
                   manager.saveCredentials(credentials);
               }

               @Override
               public void onFailure(CredentialsManagerException error) {
                   //No credentials were previously saved or they couldn't be refreshed
               }
           });
       }else{
           manager.clearCredentials();
       }
       return idToken;
    }

    public void deleteIdToken(){
        manager.clearCredentials();
    }

    public String getStoredValue(String key) {
        return preferences.getString(key, "");
    }

    public void putStoredValue(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    // What is this for?
    public void saveId(int id){
        editor.putInt("id", id);
        editor.commit();
    }

    public int getId(){
        return preferences.getInt("id", 0);
    }


    // What is this for?
    public void saveBalance(String balance){
        editor.putString("balance", balance);
        editor.commit();
    }

    public String getBalance(){
        return preferences.getString("balance", "");
    }


    public void saveScreen(String screen){
        editor.putString("screen",screen );
        editor.commit();
    }

    public String getScreen(){

        return preferences.getString("screen", "");

    }
}


