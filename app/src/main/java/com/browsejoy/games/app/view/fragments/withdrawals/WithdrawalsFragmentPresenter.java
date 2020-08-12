package com.browsejoy.games.app.view.fragments.withdrawals;

import android.support.v4.app.FragmentActivity;
import android.util.Log;
import com.browsejoy.games.app.data.model.response.RedeemListModel;
import com.browsejoy.games.app.data.network.API;
import com.browsejoy.games.app.data.network.APIClient;
import com.browsejoy.games.app.data.prefs.SaveData;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by Gerti on 12/22/2018.
 */

public class WithdrawalsFragmentPresenter implements WithdrawalsFragmentPresenterInterface {

    FragmentActivity fragmentActivity;

    WithdrawalsFragment withDrawFragment;

    SaveData saveData;

    List<RedeemListModel> array = new ArrayList<RedeemListModel>();

    public WithdrawalsFragmentPresenter(FragmentActivity fragmentActivity, WithdrawalsFragment withDrawFragment) {
        this.fragmentActivity = fragmentActivity;
        this.withDrawFragment = withDrawFragment;
        saveData  = new SaveData(fragmentActivity);

    }

    @Override
    public void getListWithDraw() {

        withDrawFragment.viewStateLoading();

        API api = APIClient.getApi(saveData.getIdToken());

        Call<List<RedeemListModel>> totclient = api.getWithDrawList();
        totclient.enqueue(new Callback<List<RedeemListModel>>() {
            @Override
            public void onResponse(Call<List<RedeemListModel>> call, retrofit2.Response<List<RedeemListModel>>response) {
                if (response.body() != null) {
                    Log.d("getUserProfile.response", response.body().toString());

                    for (int i =0 ;i <response.body().size();i++){

                        array.add(response.body().get(i));
                    }

                    withDrawFragment.showList(array);

                    withDrawFragment.viewState();
                }else{
                    withDrawFragment.viewStateError();
                }
            }

            @Override
            public void onFailure(Call<List<RedeemListModel>> call, Throwable t) {
                Log.d("getUserProfile.response", t.toString());
                t.printStackTrace();
                withDrawFragment.viewStateError();
            }
        });
    }
}
