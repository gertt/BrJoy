package com.browsejoy.games.app.view.activities.tv;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import com.browsejoy.games.R;
import com.browsejoy.games.app.data.model.request.HmacModel;
import com.browsejoy.games.app.data.network.API;
import com.browsejoy.games.app.data.network.APIClient;
import com.browsejoy.games.app.data.prefs.SaveData;
import com.browsejoy.games.app.firebase.FirebaseConfig;
import com.browsejoy.games.app.utils.hmac.Hmac;
import com.google.ads.interactivemedia.v3.api.Ad;
import com.google.ads.interactivemedia.v3.api.AdEvent;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.C.ContentType;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.ext.ima.ImaAdsLoader;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.DefaultMediaSourceEventListener;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.ads.AdsMediaSource;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.gson.Gson;
import org.apache.commons.collections.buffer.CircularFifoBuffer;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import kotlin.jvm.Synchronized;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
/**
 * Manages the {@link ExoPlayer}, the IMA plugin and all video playback.
 */
/* package */
public final class AdWithVideoPlayerManager implements AdsMediaSource.MediaSourceFactory {
  private static final String TAG = AdWithVideoPlayerManager.class.getSimpleName();
  private final DataSource.Factory dataSourceFactory;

  private SimpleExoPlayer player;
  private long contentPosition;

    //we are going to use a handler to be able to run in our TimerTask
    final Handler handler = new Handler();
  static SaveData saveData;

  MediaSource mediaSourceWithAds;
  MediaSource mediaSource;

  Context context;

  FirebaseConfig firebaseConfig;
  CircularFifoBuffer adTagURIs = new CircularFifoBuffer();

  private PlayerView playerView;

  ArrayList<String> contentURIs;
  private int playbackIndex;

  ExoPlayerEventListener exoPlayerEventListener;

  static boolean adTimerStarted = false;

  private int videosPerAd = 3;
  private int maxVideos = 20;

  Timer timer;

  TimerTask frozenAdTimeoutWatcher;

  public AdWithVideoPlayerManager(Context context, ArrayList<String> contentURIs, CircularFifoBuffer adTags, Integer videosPerAd, Integer maxVideos) {
    this.context = context;
    this.contentURIs = contentURIs;
    this.videosPerAd = videosPerAd;
    this.maxVideos = maxVideos;
    this.dataSourceFactory =
        new DefaultDataSourceFactory(
            context, Util.getUserAgent(context, context.getString(R.string.app_name)));
    this.adTagURIs = adTags;
  }

    public void startFrozenAdWatcher() {
        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();
        adTimerStarted = true;

        //schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
        timer.schedule(frozenAdTimeoutWatcher, 140*1000); //
    }

    public void stopTimerTask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            adTimerStarted = false;
            timer.cancel();
            frozenAdTimeoutWatcher.cancel();
            frozenAdTimeoutWatcher = null;
            timer = null;
        }
    }
    public void initializeTimerTask() {

        frozenAdTimeoutWatcher = new TimerTask() {
            public void run() {
                //use a handler to run a toast that shows the current timestamp
                handler.post(new Runnable() {
                    public void run() {
                      if (adTimerStarted) {
                        Log.e(TAG, "Restarting player because of frozen ad!");
                        playNext();
                        timer.cancel();
                      }
                    }
                });
            }
        };
    }

  protected void setupPlayerListeners() {
    player.addListener(new Player.EventListener() {
      @Override
      public void onTimelineChanged(Timeline timeline, @Nullable Object manifest, int reason) {
      }

      @Override
      public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
      }

      @Override
      public void onLoadingChanged(boolean isLoading) {
      }

      @Override
      public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        switch (playbackState) {
          case Player.STATE_BUFFERING:
            break;
          case Player.STATE_ENDED:
            Log.d(TAG, "onPlayerStateChanged STATE_ENDED playing next video");
            if (playWhenReady) {
              playNext();
            }
            break;
          case Player.STATE_IDLE:
            break;
          case Player.STATE_READY:
            break;
          default:
            break;
        }
      }

      @Override
      public void onRepeatModeChanged(int repeatMode) {
      }

      @Override
      public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
      }

      @Override
      public void onPlayerError(ExoPlaybackException error) {
        Log.e(TAG, "onPlayerStateChanged STATE_ERROR playing next video" + error.toString());
        Log.e(TAG, "----" + "playbackIndex " + playbackIndex + " " + contentURIs.get(playbackIndex));

        try {
          Thread.sleep(5000);
          playNext();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }

      }

      @Override
      public void onPositionDiscontinuity(int reason) {
      }

      @Override
      public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
      }

      @Override
      public void onSeekProcessed() {
      }


    });
  }

  private void initPlayer(Context context, PlayerView playerView) {
    // Create a default track selector.
    TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory();
    TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
    // Create a player instance.
    player = ExoPlayerFactory.newSimpleInstance(context, trackSelector);
    // Bind the player to the view.
    this.playerView = playerView;
    playerView.setPlayer(player);
  }

  private void playMediaSource(MediaSource mediaSource) {
    // reset state with extra flags
    if(player != null) {
      player.setPlayWhenReady(true);
      player.prepare(mediaSource, true, true);
    } else {
      Log.e(TAG, "Tried to playMediaSource when player was null!");
    }
  }

  private Boolean shouldPlayAd() {
    return playbackIndex % videosPerAd == 0;
  }

  static boolean isAdMediaSource = true;

  // This also increments the playback index
  @Synchronized
  private MediaSource buildNextMediaSource() {
    MediaSource mediaSource;
    boolean jumpToEnd = false; // for testing / fast-forward
    try {
      if (!isAdMediaSource) {
        mediaSource = getAdMediaSource();
        isAdMediaSource = true;
      } else {
        isAdMediaSource = false;
        mediaSource = getConcatenatedMediaSource();
      }
    } catch (Exception e) {
      Log.e(TAG, "Error building media source" + e.toString());
      mediaSource = null;
    }
    if (mediaSource instanceof ConcatenatingMediaSource) {
      playbackIndex += videosPerAd;
      Log.d(TAG, "Building concatenating media source index " + playbackIndex );
    } else {
      Log.d(TAG, "Building ad media source index " + playbackIndex );
      playbackIndex++;
    }


    if (jumpToEnd || playbackIndex >= contentURIs.size() || playbackIndex > maxVideos) {
      Log.i(TAG, "Reached end of TV content list");
      playbackIndex = 0;
      pausePlayer();
      exoPlayerEventListener.onPlaylistFinished("done");
    }
    return mediaSource;
  }

  public Map<String, String> getMapFromJson(String jsonString) {
    Map<String, String> map = new HashMap<>();
    try {
      JSONObject object = new JSONObject(jsonString);
      Iterator<?> iterator = object.keys();
      while (iterator.hasNext()) {
        String key = (String) iterator.next();
        if(!key.isEmpty() && !object.getString(key).isEmpty()){
          map.put(key, object.getString(key));
        }
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return map;
  }

  private Map<String, String> buildDiagnosticInfoWithAdEvent(AdEvent adInfo) {
    Map<String, String> diagInfo = new HashMap<String, String>();
      diagInfo.put("BRAND",  Build.BRAND);
      diagInfo.put("FINGERPRINT",  Build.FINGERPRINT);
      diagInfo.put("MANUFACTURER",  Build.MANUFACTURER);
      diagInfo.put("MODEL",  Build.MODEL);
      diagInfo.put("PRODUCT",  Build.PRODUCT);
    return diagInfo;
  }

  private void sendSuccessfulVideoCreditEvent(AdEvent adInfo) {
    Log.d(TAG, "sendSuccessfulVideoCreditEvent " + adInfo.toString());
    Hmac hmac = new Hmac();

    final API apiService = APIClient.getApiHmc(saveData.getIdToken(), hmac);
    HmacModel hmacModel = new HmacModel();

    Map metaData = buildDiagnosticInfoWithAdEvent(adInfo);
    Gson gson = new Gson();
    String json = gson.toJson(metaData);

    hmacModel.setActivityMetadata(json);
    hmacModel.setNonce(hmac.nonce);

    Call<ResponseBody> call = apiService.sendHmac(hmacModel);
    call.enqueue(new Callback<ResponseBody>() {
      @Override
      public void onResponse(Call<ResponseBody> call, final retrofit2.Response<ResponseBody> response) {
        try {
          if (response.isSuccessful()) {
            String jsonData = response.body().string();
            Log.d(TAG, "Successfully registered video view " + jsonData);
            Map m = getMapFromJson(jsonData);
//            if (m.containsKey("pending_earnings")) {
//              //Toast.makeText(context, "Your points are on the way!", Toast.LENGTH_SHORT).show();
//            }
          } else {
             Log.d(TAG, "Error sending video credit: " + response.errorBody().string());
            }
        }
        catch (Exception e) {
          Log.e(TAG, e.toString());
          e.printStackTrace();
        }
      }

      @Override
      public void onFailure(Call<ResponseBody> call, Throwable t) {
        Log.e("activity request error", t.toString());
      }
    });
  }

  private String getVastTagURL() {
    return (String) adTagURIs.get();
  }

  private MediaSource getAdMediaSource() {
    // This is the MediaSource representing the content media (i.e. not the ad).
    String contentUrl = contentURIs.get(playbackIndex);
    MediaSource contentMediaSource = buildMediaSource(Uri.parse(contentUrl));
    //String adTag = context.getString(R.string.ad_tag_url);
    String adTag = getVastTagURL();
    startFrozenAdWatcher();
    ImaAdsLoader.Builder builder = new ImaAdsLoader.Builder(context);
    final ImaAdsLoader adsLoader  = builder.setAdEventListener(new AdEvent.AdEventListener() {
      @Override
      public void onAdEvent(AdEvent adEvent) {
        Log.i(TAG, "Event: " + adEvent.getType());
        // These are the suggested event types to handle. For full list of all ad event
        // types, see the documentation for AdEvent.AdEventType.
        Log.d("event : ", "" + adEvent.getType());
        switch (adEvent.getType()) {
          case LOADED:
            // AdEventType.LOADED will be fired when ads are ready to be played.
            // AdsManager.start() begins ad playback. This method is ignored for VMAP or
            // ad rules playlists, as the SDK will automatically start executing the
            // playlist.
            Log.d("LOADED : ", "");
            break;
          case CONTENT_PAUSE_REQUESTED:
            // AdEventType.CONTENT_PAUSE_REQUESTED is fired immediately before a videocharles
            // ad is played.
            break;
          case CONTENT_RESUME_REQUESTED:
            // AdEventType.CONTENT_RESUME_REQUESTED is fired when the ad is completed
            // and you should start playing your content.
            Log.d("Finished watching ad ", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
            break;
          case COMPLETED:
            Ad ad = adEvent.getAd();
            Log.d(TAG, "COMPLETED AD :" + ad.toString());
            break;
          case ALL_ADS_COMPLETED:
            Log.d(TAG, "All ads completed:" + adEvent.toString());
            try {
              stopTimerTask();
              sendSuccessfulVideoCreditEvent(adEvent);
              playNext();
            } catch (Exception e) {
              e.printStackTrace();
            }
            break;
          default:
            break;
        }


      }
    }).buildForAdTag(Uri.parse(adTag));

    // Compose the content media source into a new AdsMediaSource with both ads and content.
    MediaSource mediaSourceWithAds =
        new AdsMediaSource(
            contentMediaSource,
            /* adMediaSourceFactory= */ this,
                adsLoader,
            playerView.getOverlayFrameLayout()
        );
    Handler handler = new Handler();
    mediaSourceWithAds.addEventListener(handler, new DefaultMediaSourceEventListener() {
      @Override
      public void onLoadError(int windowIndex, @Nullable MediaSource.MediaPeriodId mediaPeriodId, LoadEventInfo loadEventInfo, MediaLoadData mediaLoadData, IOException error, boolean wasCanceled) {
        Log.e(TAG, "media load error for" +playbackIndex +" " + error);
        adsLoader.release();
      }
    });
    return mediaSourceWithAds;
  }

  private MediaSource getConcatenatedMediaSource() {
    // This is the MediaSource representing the content media (i.e. not the ad).
    ConcatenatingMediaSource concatenatingMediaSource = new ConcatenatingMediaSource();
    int endOfLoop = contentURIs.size();
    int endOfMediaRange = playbackIndex + videosPerAd;
    if (endOfMediaRange > endOfLoop) {
      endOfMediaRange = endOfLoop;
    }
    List<String> selectedURIs = contentURIs.subList(playbackIndex, endOfMediaRange);
    for (int i = 0; i < selectedURIs.size(); ++i) {
      concatenatingMediaSource.addMediaSource(
          buildMediaSource(
              Uri.parse(selectedURIs.get(i))
          )
      );
    }
    return concatenatingMediaSource;
  }

  @Synchronized
  public void playNext() {
    MediaSource nextMediaSource = buildNextMediaSource();
    Log.d(TAG, "Built nextMediaSource (player index " + playbackIndex + " ) ");
    playMediaSource(nextMediaSource);
  }

  public void pausePlayer(){
    if(player !=null) {
      player.setPlayWhenReady(false);
      player.getPlaybackState();
    }
}

  public void setExoPlayerEventListener(ExoPlayerEventListener exoPlayerEventListener)
  {
    this.exoPlayerEventListener = exoPlayerEventListener;
  }

  public void init(Context context, PlayerView playerView, SaveData saveData) {
    initPlayer(context, playerView);
    setupPlayerListeners();
    this.saveData = saveData;
  }


  public void reset() {
    if (player != null) {
      contentPosition = player.getContentPosition();
      player.release();
      player = null;
    }
  }

  public void release() {
    if (player != null) {
      player.release();
      player = null;
    }
    //adsLoader.release();
  }
  // AdsMediaSource.MediaSourceFactory implementation.

  @Override
  public MediaSource createMediaSource(Uri uri) {
    return buildMediaSource(uri);
  }

  @Override
  public int[] getSupportedTypes() {
    // IMA does not support Smooth Streaming ads.
    return new int[]{C.TYPE_DASH, C.TYPE_HLS, C.TYPE_OTHER};
  }
  // Internal methods.

  private MediaSource buildMediaSource(Uri uri) {
    @ContentType int type = Util.inferContentType(uri);
    switch (type) {
      case C.TYPE_DASH:
        return new DashMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
      case C.TYPE_SS:
        return new SsMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
      case C.TYPE_HLS:
        return new HlsMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
      case C.TYPE_OTHER:
        return new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
      default:
        throw new IllegalStateException("Unsupported type: " + type);
    }
  }

}





