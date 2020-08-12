package com.browsejoy.games.app.view.dialogs;

import android.support.v7.app.AlertDialog;
import android.widget.TextView;

public interface WithDrawPopUpInterface {

    void sendData(String email, AlertDialog alertDialog);

    void setAmount(TextView txt_amount);

    void checkEmail(String s);
}
