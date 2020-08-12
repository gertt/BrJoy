package com.browsejoy.games.app.view.fragments.video;

import android.content.Context;
import android.os.CountDownTimer;
import com.browsejoy.games.app.data.model.response.UrlModelModel;
import com.browsejoy.games.app.data.prefs.SaveData;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.ArrayList;

/**
 * Created by Gerti on 8/5/2018.
 */

public class VideoFragmentPresenter implements VideoFragmentInterface {

    VideoFragment videoFragment;

    ArrayList<UrlModelModel> feedItemList;

    Context context;

    SaveData saveData;

    public VideoFragmentPresenter(VideoFragment videoFragment, Context context) {
        this.videoFragment = videoFragment;
        this.context = context ;
        saveData = new SaveData(context);
    }

    @Override
    public void getGifList() {

    }

    @Override
    public void requestAd() {

        videoFragment.showAdTag();

    }

    @Override
    public void startCountDown() {

        videoFragment.showCountDownView();
        new CountDownTimer(10000, 10) {

            public void onTick(long millisUntilFinished) {
                String seconds = millisUntilFinished / 1000 + "";
                videoFragment.showCountDownSeconds(seconds);
            }

            public void onFinish() {
                videoFragment.finishCountDownView();
            }

        }.start();
    }

    @Override
    public void requestGif(ArrayList<UrlModelModel> feedItemListgGlobal) {
        if (feedItemListgGlobal.size() == saveData.getId() + 1) {
            saveData.saveId(0);
        }

        saveData.saveId(saveData.getId() + 1);

        if (context != null) {
            videoFragment.showGif();
        }
    }

    @Override
    public String playAdTagURl(int currectPosition,ArrayList<String> arrayListAdTagUrl) {

        if (currectPosition < arrayListAdTagUrl.size()) {
            return arrayListAdTagUrl.get(currectPosition).toString();
        }
        currectPosition = 0;
        return arrayListAdTagUrl.get(currectPosition).toString();
    }

    @Override
    public void sendVideoData(String adData) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {

    }
}