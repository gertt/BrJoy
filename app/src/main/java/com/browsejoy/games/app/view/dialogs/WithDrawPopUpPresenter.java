package com.browsejoy.games.app.view.dialogs;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import com.browsejoy.games.app.data.model.request.WithdrawModel;
import com.browsejoy.games.app.data.network.API;
import com.browsejoy.games.app.data.network.APIClient;
import com.browsejoy.games.app.data.prefs.SaveData;
import java.math.BigDecimal;
import java.math.MathContext;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import static com.browsejoy.games.app.utils.Constants.DOLLAR_SIGN;
import static com.browsejoy.games.app.utils.Constants.EMAIL_PATERN;
import static com.browsejoy.games.app.utils.Constants.WITHDRAW;


public class WithDrawPopUpPresenter  implements WithDrawPopUpInterface {

    Context context;

    SaveData saveData;

    WithDrawPopUp withDrawPopUp;

    public final double MINIMUM_DOLLARS = 2.5;

    public WithDrawPopUpPresenter(Context context, WithDrawPopUp withDrawPopUp) {
        this.context = context;
        this.withDrawPopUp = withDrawPopUp ;
        saveData = new SaveData(context);
    }


    @Override
    public void sendData(String email, final android.support.v7.app.AlertDialog alertDialog) {

            final API apiService = APIClient.getWithdraw(saveData.getIdToken());
            WithdrawModel withdrawModel = new WithdrawModel();

            withdrawModel.setAmount(saveData.getBalance());

            withdrawModel.setPaypalEmail(email);

            Call<ResponseBody> call = apiService.sendWithdraw(withdrawModel);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, final retrofit2.Response<ResponseBody> response) {
                    Log.d("response: ",""+response.code());

                    if (response.code() == 201){
                        Toast.makeText(context,"Withdrawal request submitted!",Toast.LENGTH_LONG).show();
                        alertDialog.dismiss();

                    }else {
                        Toast.makeText(context,"Error submitting withdrawal, please contact support on the website.",Toast.LENGTH_LONG).show();
                        alertDialog.dismiss();
                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e("activity request error", t.toString());
                }
            });
        }

    @Override
    public void setAmount(TextView txt_amount) {
        BigDecimal roundedBalance = new BigDecimal(saveData.getBalance());
        roundedBalance = roundedBalance.setScale(2, BigDecimal.ROUND_FLOOR);


        txt_amount.setText(DOLLAR_SIGN + roundedBalance);
        BigDecimal min = new BigDecimal(MINIMUM_DOLLARS, MathContext.DECIMAL64);
        if (roundedBalance.compareTo(min) > 0) {
            String valueString = WITHDRAW+" "+ DOLLAR_SIGN +String.format("%.2f", roundedBalance);
            withDrawPopUp.enableButton(valueString);
        }
    }

    @Override
    public void checkEmail(String email) {
        if (!email.matches(EMAIL_PATERN)){
            Toast.makeText(context,"Email not correct!",Toast.LENGTH_LONG).show();
        } else if (email.matches(EMAIL_PATERN)){
            if (Double.parseDouble(saveData.getBalance()) < MINIMUM_DOLLARS) {
                withDrawPopUp.balanceToLow();
            } else{
                withDrawPopUp.emailCompleted();
            }
        }
    }
}

