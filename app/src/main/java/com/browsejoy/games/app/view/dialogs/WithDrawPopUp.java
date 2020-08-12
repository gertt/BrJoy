package com.browsejoy.games.app.view.dialogs;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.browsejoy.games.R;

public class WithDrawPopUp  implements WithDrawPopUpView {

    WithDrawPopUpPresenter withDrawPopUpPresenter;

    AlertDialog alertDialog;

    Context context;

    EditText edx_email;

    TextView txtWithdraw;

    TextView txt_amount;

    public WithDrawPopUp(Context context, AlertDialog alertDialog) {
        this.context  = context;
        this.alertDialog = alertDialog;
        withDrawPopUpPresenter = new WithDrawPopUpPresenter(context,this);
    }

    public void showMaterialDialogService(final FragmentActivity activity) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.with_draw_popup, null);
        dialogBuilder.setView(dialogView);

        txtWithdraw = (TextView) dialogView.findViewById(R.id.txtWithdraw);
        edx_email = (EditText) dialogView.findViewById(R.id.edx_email);
        txt_amount = (TextView) dialogView.findViewById(R.id.txt_amount);

        withDrawPopUpPresenter.setAmount(txt_amount);

        txtWithdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                withDrawPopUpPresenter.checkEmail(edx_email.getText().toString());

            }
        });
        alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void emailCompleted() {

        withDrawPopUpPresenter.sendData(edx_email.getText().toString(),alertDialog);

    }

    @Override
    public void enableButton(String value) {

        txtWithdraw.setText(value);
        txtWithdraw.setTextColor(ContextCompat.getColor(context, R.color.blueSky));

    }

    @Override
    public void balanceToLow() {

        Toast.makeText(context,"Your balance is too low to withdraw.",Toast.LENGTH_LONG).show();

    }
}



