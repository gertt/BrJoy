package com.browsejoy.games.app.view.fragments.video;

import com.browsejoy.games.app.data.model.response.UrlModelModel;
import java.util.ArrayList;

/**
 * Created by Gerti on 8/5/2018.
 */

public interface VideoFragmentView {

    void addGifListSuccesfully(ArrayList<UrlModelModel> feedItemList);

    void showAdGifListError();

    void showCountDownSeconds(String str);

    void showCountDownView();

    void finishCountDownView();

    void showAdTag();

    void showGif();

}
