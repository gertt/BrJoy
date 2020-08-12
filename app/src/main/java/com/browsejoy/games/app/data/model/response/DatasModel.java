package com.browsejoy.games.app.data.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Gerti on 8/5/2018.
 */

public class DatasModel {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("url")

    @Expose
    private String url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
