package com.browsejoy.games.app.view.activities.tv

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlin.properties.Delegates

import android.util.Log
import android.widget.FrameLayout
import android.widget.RelativeLayout
import com.browsejoy.browsejoy.browsejoy.app.view.fragments.video.InterstitialFragment
import com.browsejoy.games.R
import com.browsejoy.games.app.data.model.response.UrlListModel
import com.browsejoy.games.app.data.network.API
import com.browsejoy.games.app.data.network.APIClient
import com.browsejoy.games.app.data.prefs.SaveData
import com.browsejoy.games.app.tv.VastTagPopulator
import com.google.android.exoplayer2.ui.PlayerView

import com.browsejoy.games.app.firebase.FirebaseConfig
import com.browsejoy.games.app.firebase.FirebaseHelper

import com.browsejoy.games.app.view.fragments.video.RewardedVideoFragment

import java.util.ArrayList

import org.apache.commons.collections.buffer.CircularFifoBuffer
import retrofit2.Call
import retrofit2.Callback

interface TvState {
    fun consumeAction(action: Action): TvState
}

sealed class Action {
    class VideoRollInitialized : Action()
    class VideoRollComplete : Action()
    class InterstitialComplete : Action()
    class EngagementComplete : Action()
    class TvStarted : Action()
}

class InitializeVideoRoll : TvState {
    override fun consumeAction(action: Action): TvState {
        return when(action) {
            is Action.VideoRollInitialized -> ContinueVideoRoll()
            else -> throw IllegalStateException("Invalid action $action passed to state $this")
        }
    }
}

class ContinueVideoRoll : TvState {
    override fun consumeAction(action: Action): TvState {
        return when(action) {
            is Action.VideoRollComplete -> ShowInterstitial()
            else -> throw IllegalStateException("Invalid action $action passed to state $this")
        }
    }
}
class ShowInterstitial : TvState {
    override fun consumeAction(action: Action): TvState {
        return when(action) {
            is Action.InterstitialComplete -> ShowEngagement()
            else -> throw IllegalStateException("Invalid action $action passed to state $this")
        }
    }
}
class ShowEngagement : TvState {
    override fun consumeAction(action: Action): TvState {
        return when(action) {
            is Action.EngagementComplete -> InitializeVideoRoll()
            else -> throw IllegalStateException("Invalid action $action passed to state $this")
        }
    }
}

class TvStartedUp : TvState {
    override fun consumeAction(action: Action): TvState {
        return InitializeVideoRoll()
//        return when(action) {
//            is Action.TvStarted -> InitializeVideoRoll()
//            else -> throw IllegalStateException("Invalid action $action passed to state $this")
//        }
    }
}

class TvBetaActivity : AppCompatActivity() {
    internal var playerView: PlayerView? = null
    internal var player: AdWithVideoPlayerManager? = null
    internal var interstitialView: FrameLayout? = null
    internal var rewardedVideoFrame: FrameLayout? = null

    internal var transparentOverlay: RelativeLayout? = null


    internal var firebaseConfig: FirebaseConfig? = FirebaseConfig()
    internal var tvContentURIs: ArrayList<String>? = null

    internal var saveData: SaveData? = null
    internal var adTagURIs = CircularFifoBuffer()

    internal var maxVideos : Int = 20
    internal var videosPerAd : Int = 3

    internal val TAG = "TvFSM"

    internal val apiService = APIClient.getClient().create(API::class.java);

    var currentState by Delegates.observable<TvState>(TvStartedUp(), { _, old, new ->
        renderViewState(new, old)
    })


    private var exoPlayerEventListener: ExoPlayerEventListener? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tv_beta_activity)
        playerView = findViewById<PlayerView>(R.id.player_view)
        transparentOverlay = findViewById<RelativeLayout>(R.id.transparentOverlay)
        interstitialView = findViewById<FrameLayout>(R.id.interstitial_frame)
        rewardedVideoFrame = findViewById<FrameLayout>(R.id.rewarded_video_frame)

        this.exoPlayerEventListener = null;
        playerView?.hideController()

        saveData = SaveData(this)
        firebaseConfig = FirebaseConfig()
        setupFirebaseContentCallbacks();

    }



    private fun setupFirebaseContentCallbacks() {
        FirebaseHelper.fetchVideoConfig(this, { videoConfig ->
            maxVideos = videoConfig.maxVideos
            videosPerAd = videoConfig.videosPerAd
        })

        fun setupTVContentProvider() {
            val apiService = APIClient.getClient().create(API::class.java)
            val totclient = apiService.videoList
            totclient.enqueue(object : Callback<UrlListModel> {
                override fun onResponse(call: Call<UrlListModel>, response: retrofit2.Response<UrlListModel>) {
                    if (response.errorBody() == null) {
                        var list = ArrayList<String>()
                        for (i in 0 until response.body()!!.urls.size) {
                            list.add(response.body()!!.urls[i].url)
                        }
                        list.shuffle()
                        tvContentURIs = list
                        currentState = currentState.consumeAction(Action.VideoRollInitialized())
                    }
                }

                override fun onFailure(call: Call<UrlListModel>, t: Throwable) {
                    t.printStackTrace()
                }
            })
        }


        firebaseConfig = FirebaseConfig()
        firebaseConfig?.fetchCallback(this)
        firebaseConfig?.setOnListener { list ->
            val enricher = VastTagPopulator(saveData, applicationContext)
            for (i in list.indices) {
                adTagURIs.add(enricher.enrichWithDeviceData(list[i]))
            }
            setupTVContentProvider()
        }
    }

    private fun renderViewState(newState: TvState, oldState: TvState) {
        Log.i(TAG, "Changing state newState: ${newState} oldState:${oldState}")
        when (newState) {
            is InitializeVideoRoll -> initializeVideoRoll()
            is ContinueVideoRoll -> continueVideoRoll()
            is ShowInterstitial -> showInterstitial()
            is ShowEngagement -> showEngagement()
        }

        when (oldState) {
            is ContinueVideoRoll -> stopAndHideVideoRoll()
            is ShowInterstitial -> hideInterstitial()
            is ShowEngagement -> hideEngagement()
        }
    }

    private fun initializeVideoRoll() {
        player = AdWithVideoPlayerManager(this, tvContentURIs, adTagURIs, videosPerAd, maxVideos)
        this.exoPlayerEventListener = object : ExoPlayerEventListener {
            override fun onPlaylistFinished(data: String) {
                Log.d(TAG, "Finished video roll")
                currentState = currentState.consumeAction(Action.VideoRollComplete())
            }
        }
        player?.setExoPlayerEventListener(this.exoPlayerEventListener)
        // this must be done when coming back to playlist, because exoplayer lifecycle releases on pause
        initPlayer();

    }

    private fun initPlayer() {
        player?.init(this, playerView, saveData)
        currentState = currentState.consumeAction(Action.VideoRollInitialized())

    }

    private fun continueVideoRoll() {
        playerView?.visibility = View.VISIBLE
        transparentOverlay?.visibility = View.INVISIBLE;
        player?.playNext()
    }
    private fun stopAndHideVideoRoll() {
        releaseMediaPlayer()
        transparentOverlay?.visibility = View.VISIBLE;
    }

    private fun showInterstitial() {
        interstitialView?.visibility = View.VISIBLE;
        val interstitialFragment = InterstitialFragment.newInstance()
        interstitialFragment.doneCallback = {
            currentState = currentState.consumeAction(Action.InterstitialComplete())
        }
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.interstitial_frame, interstitialFragment)
        transaction.commit()
    }
    private fun hideInterstitial() {
        transparentOverlay?.visibility = View.INVISIBLE;
        interstitialView?.visibility = View.INVISIBLE;
    }

    private fun showEngagement() {
        Log.d(TAG, "showEngagement")
        playerView?.visibility = View.INVISIBLE
        rewardedVideoFrame?.visibility = View.VISIBLE;
        val rewardedFragment = RewardedVideoFragment.newInstance()
        rewardedFragment.doneCallback = {
            Log.d(TAG, "showEngagement Finished")
            currentState = currentState.consumeAction(Action.EngagementComplete())
//            when (currentState) {
//                is ShowEngagement -> {
//                    currentState = currentState.consumeAction(Action.EngagementComplete())
//                }
//            }
        }
        tvContentURIs?.shuffle()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.rewarded_video_frame, rewardedFragment)
        transaction.commit()
    }
    private fun hideEngagement() {
        Log.d(TAG, "hideEngagement")
        playerView?.visibility = View.VISIBLE
        rewardedVideoFrame?.visibility = View.INVISIBLE;
    }

    private fun releaseMediaPlayer() {
        player?.pausePlayer();
        player?.release();
    }

    override fun onDestroy() {
        releaseMediaPlayer()
        super.onDestroy()
    }


}