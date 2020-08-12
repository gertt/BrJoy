package com.browsejoy.games.app.view.activities.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import com.auth0.android.Auth0;
import com.auth0.android.authentication.AuthenticationAPIClient;
import com.auth0.android.authentication.storage.CredentialsManager;
import com.auth0.android.authentication.storage.SharedPreferencesStorage;
import com.browsejoy.games.app.data.prefs.SaveData;
import com.browsejoy.games.app.firebase.FirebaseHelper;
import com.browsejoy.games.app.utils.Constants;
import com.browsejoy.games.app.view.activities.privacy_policy.PrivacyPolicyActivity;
import com.browsejoy.games.app.view.fragments.earn.EarnsFragment;
import com.browsejoy.games.app.view.fragments.home.HomeFragment;
import com.browsejoy.games.app.view.fragments.redeem.RedeemFragment;
import com.browsejoy.games.app.utils.OnDataPass;
import com.browsejoy.games.R;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import static com.browsejoy.games.app.utils.Balance.convertToPoints;


public class MainActivity extends AppCompatActivity implements OnDataPass,MainActivityInterface {

    BottomNavigationView bottomNavigationView;
    TextView txt_balance;
    Toolbar mActionBarToolbar;
    CredentialsManager manager;

    MainActivityPresenter mainActivityPresenter;

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    String value ;

    SaveData saveData;

    Menu menu;
    TextView text;

    public final static String SELECTED_TAB_EXTRA_KEY = "selectedTabIndex";
    public final static int HOME_TAB = 0;
    public final static int EARNINGS_TAB = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupToolbar();
        setupCredentialsManager();

        saveData = new SaveData(getApplicationContext());
        // SendHmc();
        mainActivityPresenter = new MainActivityPresenter(this, getApplicationContext());

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        txt_balance = findViewById(R.id.value);

        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

    }


    // Reads selected tab from launching intent and
// sets page accordingly
    public void setSelectedTab() {
        // Fetch the selected tab index with default
        int selectedTabIndex = getIntent().getIntExtra(SELECTED_TAB_EXTRA_KEY, HOME_TAB);
        // Switch to page based on index
        viewPager.setCurrentItem(selectedTabIndex);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new HomeFragment(), "Home");
        adapter.addFragment(new EarnsFragment(), "Earn");
        adapter.addFragment(new RedeemFragment(), "Redeem");
        viewPager.setAdapter(adapter);
        setSelectedTab();
    }

    @Override
    public void setBalance(Double balance) {
        value = convertToPoints(balance);
        menu.findItem(R.id.value).setTitle(value);
      //  text = findViewById(R.id.total_balance_main);
       // text.setText(value);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private void setupToolbar() {
        mActionBarToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);
        getSupportActionBar().setTitle(R.string.app_name);

    }

    private void setupCredentialsManager() {
        Auth0 auth0 = new Auth0(Constants.CLIENT_ID,Constants.DOMAIN);
        auth0.setOIDCConformant(true);
        auth0.setLoggingEnabled(true);
        AuthenticationAPIClient apiClient = new AuthenticationAPIClient(auth0);
        manager = new CredentialsManager(apiClient, new SharedPreferencesStorage(getApplicationContext()));
    }

    @Override
    public void onDataPass(String data) {
        if(data != null){
         value = data ;
            setBalance(Double.parseDouble(data));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.privacy_policy) {
            Intent browserIntent = new Intent(MainActivity.this, PrivacyPolicyActivity.class);
            startActivity(browserIntent);
        }

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_menu, menu);
        this.menu = menu;
        return true;
    }

    private void redirectStore(String updateUrl) {
        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(updateUrl));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}