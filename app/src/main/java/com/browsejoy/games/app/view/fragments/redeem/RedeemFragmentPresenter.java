package com.browsejoy.games.app.view.fragments.redeem;

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
 * Created by Gerti on 12/27/2018.
 */

public class RedeemFragmentPresenter  implements  RedeemFragmentPresenterInterface {

    FragmentActivity activity;

    RedeemFragment redeemFragment;

    SaveData saveData;

    List<RedeemListModel> array = new ArrayList<RedeemListModel>();

    RedeemListModel redeemListModel;

    public RedeemFragmentPresenter(FragmentActivity activity, RedeemFragment redeemFragment) {

        this.activity = activity;
        this.redeemFragment = redeemFragment;
        saveData  = new SaveData(activity);

    }

    @Override
    public void getListRedeem() {

        array.clear();

        redeemListModel =  new RedeemListModel(1,"","","");

        array.add(redeemListModel);

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



                    redeemFragment.showList(array);

                    redeemFragment.viewState();

                }else{

                    redeemFragment.viewStateError();
                }
            }

            @Override
            public void onFailure(Call<List<RedeemListModel>> call, Throwable t) {
                Log.d("getUserProfile.response", t.toString());
                t.printStackTrace();
                redeemFragment.viewStateError();
            }
        });
    }
}
