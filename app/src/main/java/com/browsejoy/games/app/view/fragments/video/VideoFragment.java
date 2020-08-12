package com.browsejoy.games.app.view.fragments.video;

/**
 * Created by Gerti on 8/28/2018.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.aerserv.sdk.AerServSdk;
import com.amazon.device.ads.DTBAdRequest;
import com.amazon.device.ads.DTBAdResponse;
import com.amazon.device.ads.DTBAdSize;
import com.browsejoy.games.R;
import com.browsejoy.games.app.data.model.response.UrlModelModel;
import com.browsejoy.games.app.data.prefs.SaveData;
import com.browsejoy.games.app.firebase.FirebaseConfig;
import com.browsejoy.games.app.utils.MyExceptionHandler;
import com.browsejoy.games.app.utils.OnListPass;
import com.browsejoy.games.app.utils.SampleVideoPlayer;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.ads.interactivemedia.v3.api.Ad;
import com.google.ads.interactivemedia.v3.api.AdDisplayContainer;
import com.google.ads.interactivemedia.v3.api.AdErrorEvent;
import com.google.ads.interactivemedia.v3.api.AdEvent;
import com.google.ads.interactivemedia.v3.api.AdsLoader;
import com.google.ads.interactivemedia.v3.api.AdsManager;
import com.google.ads.interactivemedia.v3.api.AdsManagerLoadedEvent;
import com.google.ads.interactivemedia.v3.api.AdsRequest;
import com.google.ads.interactivemedia.v3.api.ImaSdkFactory;
import com.google.ads.interactivemedia.v3.api.player.ContentProgressProvider;
import com.google.ads.interactivemedia.v3.api.player.VideoProgressUpdate;
import com.kennyc.view.MultiStateView;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import com.aerserv.sdk.AerServBanner;
import com.aerserv.sdk.AerServConfig;
import com.amazon.device.ads.AdError;
import com.amazon.device.ads.AdRegistration;
import com.amazon.device.ads.DTBAdCallback;

/**
 * The main fragment for displaying video content.
 */
public class VideoFragment extends Fragment implements AdEvent.AdEventListener, AdErrorEvent.AdErrorListener, MultiStateView.StateListener, VideoFragmentView {

    public static VideoFragment newInstance() {
        VideoFragment fragment = new VideoFragment();
        return fragment;
    }

    ImageView img_gif;
    View rootView;
    MultiStateView multiStateView;

    VideoFragmentPresenter videoFragmentPresenter;
    FirebaseConfig firebaseConfig;

    private static String LOGTAG = "ImaVideoPlayerFragment";

    // The video player.
    private SampleVideoPlayer mVideoPlayer;

    // The container for the ad's UI.
    private ViewGroup mAdUiContainer;

    // Factory class for creating SDK objects.
    private ImaSdkFactory mSdkFactory;

    // The AdsLoader instance exposes the requestAds method.
    private AdsLoader mAdsLoader;

    // AdsManager exposes methods to control ad playback and listen to ad events.
    private AdsManager mAdsManager;

    // Whether an ad is displayed.
    private boolean mIsAdDisplayed;

    TextView txt_count_down;

    String currentUrl = "";

   // private AdView mAdView;

    static ArrayList<UrlModelModel> arrayListGif;
    static ArrayList<String> arrayListAdTagUrl = new ArrayList<>();
    static int currectPosition = 0;
    static SaveData saveData;

    private AerServBanner banner1;

    private static final String LOG_TAG = "AerServSampleApp";

 //   private AerServBanner banner1;
  //  private AerServInterstitial interstitial;

    private static final String APP_KEY = "a9_onboarding_app_id";
    private static final String SLOT_320x50 = "54fb2d08-c222-40b1-8bbe-4879322dc04b";
    private static final String SLOT_INTERSTITIAL = "4e918ac0-5c68-4fe1-8d26-4e76e8f74831";

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);

        // Create an AdsLoader.
        mSdkFactory = ImaSdkFactory.getInstance();
        mAdsLoader = mSdkFactory.createAdsLoader(this.getContext());

        saveData = new SaveData(getActivity());
        saveData.saveId(0);

        // Add listeners for when ads are loaded and for errors.
        mAdsLoader.addAdErrorListener(this);
        mAdsLoader.addAdsLoadedListener(new AdsLoader.AdsLoadedListener() {
            @Override
            public void onAdsManagerLoaded(AdsManagerLoadedEvent adsManagerLoadedEvent) {
                // Ads were successfully loaded, so get the AdsManager instance. AdsManager has
                // events for ad playback and errors.
                mAdsManager = adsManagerLoadedEvent.getAdsManager();

                // Attach event and error event listeners.
                mAdsManager.addAdErrorListener(VideoFragment.this);
                mAdsManager.addAdEventListener(VideoFragment.this);
                mAdsManager.init();
            }
        });


        // Add listener for when the content video finishes.
        mVideoPlayer.addVideoCompletedListener(new SampleVideoPlayer.OnVideoCompletedListener() {
            @Override
            public void onVideoCompleted() {
                // Handle completed event for playing post-rolls.
                if (mAdsLoader != null) {
                    mAdsLoader.contentComplete();
                }
            }
        });

        videoFragmentPresenter = new VideoFragmentPresenter(this, getActivity());
        videoFragmentPresenter.getGifList();
        firebaseConfig = new FirebaseConfig();

     AerServSdk.init(getActivity(), "1013710");

        loadBanner();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_video, container, false);

        //Force restart the app if it crashes
        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(getActivity()));
     //   MobileAds.initialize(getActivity(), String.valueOf(R.string.banner_ad_unit_id));


      //  mAdView = (AdView) rootView.findViewById(R.id.admob);

        mVideoPlayer = (SampleVideoPlayer) rootView.findViewById(R.id.sampleVideoPlayer);
        mAdUiContainer = (ViewGroup) rootView.findViewById(R.id.videoPlayerWithAdPlayback);

        txt_count_down = (TextView) rootView.findViewById(R.id.txt_count_down);
        img_gif = rootView.findViewById(R.id.img_gif);

        multiStateView = rootView.findViewById(R.id.multiStateView);
        multiStateView.setStateListener(this);

        return rootView;
    }

    @Override
    public void onAdEvent(AdEvent adEvent) {
        Log.i(LOGTAG, "Event: " + adEvent.getType());
        multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
        // These are the suggested event types to handle. For full list of all ad event
        // types, see the documentation for AdEvent.AdEventType.
        Log.d("event : ", "" + adEvent.getType());
        switch (adEvent.getType()) {
            case LOADED:
                // AdEventType.LOADED will be fired when ads are ready to be played.
                // AdsManager.start() begins ad playback. This method is ignored for VMAP or
                // ad rules playlists, as the SDK will automatically start executing the
                // playlist.
                mAdsManager.start();
                Log.d("LOADED : ", "");
                break;
            case CONTENT_PAUSE_REQUESTED:
                // AdEventType.CONTENT_PAUSE_REQUESTED is fired immediately before a videocharles
                // ad is played.
                multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
                mIsAdDisplayed = true;
                mVideoPlayer.pause();
                break;
            case CONTENT_RESUME_REQUESTED:
                // AdEventType.CONTENT_RESUME_REQUESTED is fired when the ad is completed
                // and you should start playing your content.
                Log.d("Finished watching ad ", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
                mIsAdDisplayed = false;
                mVideoPlayer.play();
                break;
            case COMPLETED:
                Ad ad = adEvent.getAd();
                Log.d("COMPLETED AD :", ad.toString());
                try {
                    videoFragmentPresenter.sendVideoData(ad.toString());
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (SignatureException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                }
                break;
            case ALL_ADS_COMPLETED:
                if (mAdsManager != null) {
                    mAdsManager.destroy();
                    mAdsManager = null;
                    mVideoPlayer.setVisibility(View.GONE);
                    img_gif.setVisibility(View.VISIBLE);

                    multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
                    if (mAdsLoader != null) {
                        mAdsLoader.contentComplete();
                    }
                    currectPosition++;
                    //requestGif();
                    videoFragmentPresenter.startCountDown();
                }

                break;
            default:
                break;
        }
    }

    @Override
    public void onAdError(AdErrorEvent adErrorEvent) {
        Log.e(LOGTAG, "Ad Error: " + adErrorEvent.getError().getMessage());
        mIsAdDisplayed = false;
        mVideoPlayer.play();
        if (mAdsManager != null) {
            mAdsManager.destroy();
            mAdsManager = null;
        }
        if (mAdsLoader != null) {
            mAdsLoader.contentComplete();
        }

        currectPosition++;
        multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
        videoFragmentPresenter.startCountDown();
    }

    @Override
    public void onResume() {
        if (mAdsManager != null && mIsAdDisplayed) {
            mAdsManager.resume();
        } else {
            mVideoPlayer.play();
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        if (mAdsManager != null && mIsAdDisplayed) {
            mAdsManager.pause();
        } else {
            mVideoPlayer.pause();
        }
        super.onPause();
    }

    @Override
    public void onStateChanged(int i) {}

    @Override
    public void addGifListSuccesfully(ArrayList<UrlModelModel> feedItemList) {

        multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);

        arrayListGif = new ArrayList<>();
        arrayListGif = feedItemList;

        firebaseConfig.fetchCallback(getActivity());
        firebaseConfig.setOnListener(new OnListPass() {
            @Override
            public void onListPass(ArrayList<String> list) {
                arrayListAdTagUrl = list;
                videoFragmentPresenter.requestAd();
            }
        });
    }

    @Override
    public void showAdGifListError() {
        multiStateView.setViewState(MultiStateView.VIEW_STATE_ERROR);
        multiStateView.getView(MultiStateView.VIEW_STATE_ERROR).findViewById(R.id.retry)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
                        videoFragmentPresenter.getGifList();
                    }
                });
    }

    @Override
    public void showCountDownSeconds(String str) {
        txt_count_down.setText(str);
    }

    @Override
    public void showCountDownView() {

        multiStateView.setViewState(MultiStateView.VIEW_STATE_EMPTY);

    }

    @Override
    public void finishCountDownView() {
        multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
        multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
        videoFragmentPresenter.requestGif(arrayListGif);
    }

    @Override
    public void showAdTag() {

        getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
                }
            });

        AdDisplayContainer adDisplayContainer = mSdkFactory.createAdDisplayContainer();
        adDisplayContainer.setAdContainer(mAdUiContainer);

        AdsRequest request = mSdkFactory.createAdsRequest();


        currentUrl = videoFragmentPresenter.playAdTagURl(currectPosition,arrayListAdTagUrl);

        request.setAdTagUrl(currentUrl);
        request.setAdDisplayContainer(adDisplayContainer);
        request.setContentProgressProvider(new ContentProgressProvider() {
            @Override
            public VideoProgressUpdate getContentProgress() {
                if (mIsAdDisplayed || mVideoPlayer == null || mVideoPlayer.getDuration() <= 0) {
                    return VideoProgressUpdate.VIDEO_TIME_NOT_READY;
                }
                return new VideoProgressUpdate(mVideoPlayer.getCurrentPosition(),
                        mVideoPlayer.getDuration());
            }
        });

        // Request the ad. After the ad is loaded, onAdsManagerLoaded() will be called.
        mAdsLoader.requestAds(request);
    }

    @Override
    public void showGif() {
        img_gif.setVisibility(View.VISIBLE);

        Glide.with(getActivity())
                .asGif()
                .load(arrayListGif.get(saveData.getId()).getUrl())
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .listener(new RequestListener<GifDrawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
                        Log.e("requestGif Glide", "onLoadFailed" + e.toString());

                        multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
                        videoFragmentPresenter.requestAd();

                        return false;
                    }

                    @Override
                    public boolean onResourceReady(final GifDrawable resource, Object model,
                                                   Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {

                        resource.setLoopCount(1);
                        multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
                        final Timer t = new Timer();
                        t.schedule(new TimerTask() {
                            @Override
                            public void run() {

                                if (!resource.isRunning()) {
                                    t.cancel();
                                    videoFragmentPresenter.requestAd();
                                }
                            }
                        }, 2000, 3000);
                        return false;
                    }

                }).into(img_gif);
    }


    public void loadBanner() {
        AdRegistration.getInstance(APP_KEY, getActivity());
        AdRegistration.enableLogging(true);
        AdRegistration.enableTesting(true);
        AdRegistration.useGeoLocation(true);

        final DTBAdRequest loader = new DTBAdRequest();
        loader.setSizes(new DTBAdSize(300, 250, SLOT_320x50));

        //Send ad request to Amazon
        loader.loadAd(new DTBAdCallback() {
            @Override
            public void onFailure(AdError adError) {
                Log.e("A9", "Failed to get banner ad from Amazon: " + adError.getMessage());
                final AerServConfig config = new AerServConfig(getActivity(), getPlc())
                        .setA9AdResponses(null)
                        //  .setEventListener(bannerListener)
                        //  .setPreload(preloadSwitch.isChecked())
                        .setRefreshInterval(60);
                banner1 = (AerServBanner) getActivity().findViewById(R.id.banner);
                banner1.configure(config);

                banner1.show();

            }

            @Override
            public void onSuccess(DTBAdResponse dtbAdResponse) {
                List<DTBAdResponse> responses = new ArrayList<DTBAdResponse>();
                responses.add(dtbAdResponse);
                Log.i("A9", "Successfully get " + dtbAdResponse.getDTBAds().size()
                        + " banner ad from Amazon");

                final AerServConfig config = new AerServConfig(getActivity(), getPlc())
                        .setA9AdResponses(responses)
                        //   .setEventListener(bannerListener)
                        //  .setPreload(preloadSwitch.isChecked())
                        .setRefreshInterval(60);
                banner1 = (AerServBanner) getActivity().findViewById(R.id.banner);
                banner1.configure(config);

                banner1.show();

            }
        });

    }
    private String getPlc() {
        return "1053138";
    }
}