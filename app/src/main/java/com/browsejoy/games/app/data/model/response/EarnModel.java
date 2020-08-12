package com.browsejoy.games.app.data.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Gerti on 2/6/2019.
 */

public class EarnModel {

    @SerializedName("earnings")
    @Expose
    private List<EarningList> earnings = null;

    public List<EarningList> getEarnings() {
        return earnings;
    }

    public void setEarnings(List<EarningList> earnings) {
        this.earnings = earnings;
    }
}
