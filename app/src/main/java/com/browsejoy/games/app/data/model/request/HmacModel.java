package com.browsejoy.games.app.data.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Gerti on 11/12/2018.
 */

public class HmacModel {

    @SerializedName("activity_metadata")
    @Expose
    private String activityMetadata;

    @SerializedName("nonce")
    @Expose
    private String nonce;

    public String getActivityMetadata() {
        return activityMetadata;
    }

    public void setActivityMetadata(String activityMetadata) {
        this.activityMetadata = activityMetadata;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

}