package com.browsejoy.games.app.oauth;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import com.auth0.android.Auth0;
import com.auth0.android.authentication.AuthenticationAPIClient;
import com.auth0.android.authentication.storage.CredentialsManager;
import com.auth0.android.authentication.storage.CredentialsManagerException;
import com.auth0.android.authentication.storage.SharedPreferencesStorage;
import com.auth0.android.callback.BaseCallback;
import com.auth0.android.lock.AuthenticationCallback;
import com.auth0.android.lock.Lock;
import com.auth0.android.lock.LockCallback;
import com.auth0.android.lock.utils.LockException;
import com.auth0.android.result.Credentials;
import com.browsejoy.games.R;
import com.browsejoy.games.app.view.activities.main.MainActivity;
import com.browsejoy.games.app.data.prefs.SaveData;
import com.browsejoy.games.app.utils.Constants;
import com.browsejoy.games.app.view.activities.privacy_policy.PrivacyPolicyActivity;


public class Oauth extends AppCompatActivity implements OauthInterface {

    private Lock lock;

    SaveData saveData;

    OauthPresenter oauthPresenter;

    private AuthenticationAPIClient apiClient;
    private CredentialsManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Auth0 auth0 = new Auth0(Constants.CLIENT_ID,Constants.DOMAIN);
        auth0.setOIDCConformant(true);
        auth0.setLoggingEnabled(true);
        apiClient = new AuthenticationAPIClient(auth0);

        oauthPresenter = new  OauthPresenter(getApplicationContext(),this);

        saveData = new SaveData(getApplicationContext());

        lock = Lock.newBuilder(auth0, callback)
                .withScheme("brow")
                .withScope("openid email profile")
                .build(this);

        startActivity(lock.newIntent(this));

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Your own Activity code
        lock.onDestroy(this);
        lock = null;
    }

    private LockCallback callback = new AuthenticationCallback() {
        @Override
        public void onAuthentication(Credentials credentials) {

            manager = new CredentialsManager(apiClient, new SharedPreferencesStorage(getApplicationContext()));
            manager.saveCredentials(credentials);
            manager.getCredentials(new BaseCallback<Credentials, CredentialsManagerException>() {
                @Override
                public void onSuccess(Credentials credentials) {
                    Log.d("",""+credentials.getIdToken());

                    oauthPresenter.chekWorkflow();
                }

                @Override
                public void onFailure(CredentialsManagerException error) {
                    //No credentials were previously saved or they couldn't be refreshed
                }
            });


            saveData = new SaveData(getApplicationContext(),apiClient,credentials);
            Log.d("elio : ",""+saveData.getIdToken());

            finish();
        }

        @Override
        public void onCanceled() {
            //User pressed back
        }

        @Override
        public void onError(LockException error) {
            //Exception occurred
            Log.d("LockException : ",""+error);
        }
    };

    @Override
    public void goToPrivacy() {
        Intent intent =  new Intent(getApplicationContext(),PrivacyPolicyActivity.class);
        startActivity(intent);

    }

    @Override
    public void goToMain() {

        Intent intent =  new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);

    }
}