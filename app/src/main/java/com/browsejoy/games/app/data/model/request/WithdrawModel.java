package com.browsejoy.games.app.data.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Gerti on 12/22/2018.
 */

public class WithdrawModel {

    @SerializedName("amount")
    @Expose
    private String amount;

    @SerializedName("paypal_email")
    @Expose
    private String paypalEmail;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPaypalEmail() {
        return paypalEmail;
    }

    public void setPaypalEmail(String paypalEmail) {
        this.paypalEmail = paypalEmail;
    }
}
