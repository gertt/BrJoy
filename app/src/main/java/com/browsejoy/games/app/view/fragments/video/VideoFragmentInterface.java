package com.browsejoy.games.app.view.fragments.video;

import com.browsejoy.games.app.data.model.response.UrlModelModel;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.ArrayList;

/**
 * Created by Gerti on 8/5/2018.
 */

public interface VideoFragmentInterface {

    void getGifList();

    void startCountDown();

    void requestAd();

    void requestGif(ArrayList<UrlModelModel> feedItemListgGlobal);

    String playAdTagURl(int currectPosition,ArrayList<String> arrayListAdTagUrl);

    void sendVideoData(String adData) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException;
}
