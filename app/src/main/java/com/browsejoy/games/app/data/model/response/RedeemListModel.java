package com.browsejoy.games.app.data.model.response;

import com.browsejoy.games.app.utils.RecyclerViewItem;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Gerti on 12/22/2018.
 */

public class RedeemListModel extends RecyclerViewItem {


    @SerializedName("amount")
    @Expose
    private Integer amount;

    @SerializedName("requested_at")
    @Expose
    private String requestedAt;

    @SerializedName("paid_at")
    @Expose
    private String paidAt;

    @SerializedName("email")
    @Expose
    private String email;

    public RedeemListModel(Integer amount, String requestedAt, String paidAt, String email) {
        this.amount = amount;
        this.requestedAt = requestedAt;
        this.paidAt = paidAt;
        this.email = email;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getRequestedAt() {
        return requestedAt;
    }

    public void setRequestedAt(String requestedAt) {
        this.requestedAt = requestedAt;
    }

    public String getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(String paidAt) {
        this.paidAt = paidAt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }




}



