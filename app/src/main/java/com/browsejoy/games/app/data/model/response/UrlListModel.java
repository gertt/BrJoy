package com.browsejoy.games.app.data.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Gerti on 1/21/2019.
 */

public class UrlListModel {

    @SerializedName("urls")
    @Expose
    private ArrayList<UrlModelModel> urls = null;

    public ArrayList<UrlModelModel> getUrls() {
        return urls;
    }

    public void setUrls(ArrayList<UrlModelModel> urls) {
        this.urls = urls;
    }
}
