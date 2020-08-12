package com.browsejoy.games.app.data.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Gerti on 2/6/2019.
 */

public class EarningTableModel {

    public EarningTableModel(Integer rate, String name, String createdAt) {
        this.rate = rate;
        this.name = name;
        this.createdAt = createdAt;
    }

    @SerializedName("rate")

    @Expose
    private Integer rate;
    @SerializedName("name")

    @Expose
    private String name;
    @SerializedName("created_at")

    @Expose
    private String createdAt;

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
