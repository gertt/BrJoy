package com.browsejoy.games.app.view.activities.tv;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.browsejoy.games.R;
import com.browsejoy.games.app.data.prefs.SaveData;
import com.browsejoy.games.app.tv.VastTagPopulator;
import com.browsejoy.games.app.utils.MyExceptionHandler;
import com.browsejoy.games.app.utils.OnListPass;
import com.google.android.exoplayer2.ui.PlayerView;
import com.browsejoy.games.app.firebase.FirebaseConfig;
import java.util.ArrayList;
import org.apache.commons.collections.buffer.CircularFifoBuffer;

public final class TvActivity extends AppCompatActivity {

  private PlayerView playerView;

  private AdWithVideoPlayerManager player;

  FirebaseConfig firebaseConfig;

  ArrayList<String> tvContentURIs;

  static SaveData saveData;

  CircularFifoBuffer adTagURIs = new CircularFifoBuffer();

  @Override
  protected void onCreate(Bundle savedInstanceState) {

    firebaseConfig = new FirebaseConfig();
    super.onCreate(savedInstanceState);
    setContentView(R.layout.tv_activity);
    playerView = findViewById(R.id.player_view);
    playerView.hideController();

    //Force restart the app if it crashes
    Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this));

    saveData = new SaveData(this);
    // after loading ad tags list, the player is started
    loadAdTags();

  }

  private void loadAdTags() {

    firebaseConfig = new FirebaseConfig();
    firebaseConfig.fetchCallback(this);
    firebaseConfig.setOnListener(new OnListPass() {

      @Override
      public void onListPass(ArrayList<String> list) {

        VastTagPopulator enricher = new VastTagPopulator(saveData, getApplicationContext());
        for(int i = 0; i < list.size(); i++) {

          adTagURIs.add(enricher.enrichWithDeviceData(list.get(i)));

        }
        setupPlayerManager();
        setupTVContentProvider();
      }

    });

  }

  protected void setupTVContentProvider() {

    firebaseConfig.fetchTvContent(this);
    firebaseConfig.setOnListener(new OnListPass() {

      @Override
      public void onListPass(ArrayList<String> list) {

        player.contentURIs = list;
        player.playNext();

      }

    });

  }

  @Override
  public void onResume() {

    super.onResume();

  }

  @Override
  public void onPause() {

    super.onPause();
    player.reset();

  }

  @Override
  public void onDestroy() {

    shutDownPlayer();
    super.onDestroy();

  }

  protected void setupPlayerManager() {

    player = new AdWithVideoPlayerManager(this, tvContentURIs, adTagURIs);
    player.init(this, playerView, saveData);
  }

  protected void setupResetTimer() {

  }

  protected void shutDownPlayer() {

    player.release();

  }

}
