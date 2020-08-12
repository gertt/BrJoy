package com.browsejoy.games.app.view.activities.privacy_policy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import com.browsejoy.games.R;
import com.browsejoy.games.app.utils.MyExceptionHandler;
import com.browsejoy.games.app.view.activities.main.MainActivity;

public class PrivacyPolicyActivity extends AppCompatActivity {

    WebView webView;

    PrivacyPolicyPresenter privacyPolicyPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this));
        privacyPolicyPresenter  = new PrivacyPolicyPresenter(getApplicationContext());

        webView = findViewById(R.id.webView);
        webView.loadUrl("file:///android_asset/browsejoy.html");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_go_main) {

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_privacy, menu);

        return super.onCreateOptionsMenu(menu);
    }
}
