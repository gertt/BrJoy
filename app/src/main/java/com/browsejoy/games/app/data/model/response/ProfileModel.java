package com.browsejoy.games.app.data.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Gerti on 7/23/2018.
 */

public class ProfileModel {

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("balance")
    @Expose
    private Double balance;

    @SerializedName("earning_history")
    @Expose
    private String earningHistory;

    @SerializedName("referrer_id")
    @Expose
    private String referrerId;

    @SerializedName("auth_id")
    @Expose
    private String authId;

    public String getAuthId() { return authId; }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getEarningHistory() {
        return earningHistory;
    }

    public void setEarningHistory(String earningHistory) {
        this.earningHistory = earningHistory;
    }

    public String getReferrerId() {
        return referrerId;
    }

    public void setReferrerId(String referrerId) {
        this.referrerId = referrerId;
    }

}