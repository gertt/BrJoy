package com.browsejoy.games.app.view.fragments.home;

import android.content.Context;
import android.util.Log;
import com.browsejoy.games.app.data.model.response.EarnModel;
import com.browsejoy.games.app.data.model.response.EarningList;
import com.browsejoy.games.app.data.model.response.EarningTableModel;
import com.browsejoy.games.app.data.model.response.ProfileModel;
import com.browsejoy.games.app.data.network.API;
import com.browsejoy.games.app.data.network.APIClient;
import com.browsejoy.games.app.data.prefs.SaveData;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by Gerti on 9/28/2018.
 */

public class HomeFragmentPresenter implements HomeFragmentPresenterInterface {

    Context context;

    HomeFragment homeFragment;

    List<EarningList> array = new ArrayList<EarningList>();

    SaveData saveData;

    EarningTableModel earningTableModel;

    EarningList earningList;


    public HomeFragmentPresenter(Context context, HomeFragment homeFragment) {
        this.context = context;
        this.homeFragment = homeFragment;
        saveData = new SaveData(context);

    }

    @Override
    public void getUserProfile() {
        homeFragment.viewStateLoading();

        API api = APIClient.getApi(saveData.getIdToken());

        Call<ProfileModel> totclient = api.getUserProfile();
        totclient.enqueue(new Callback<ProfileModel>() {
            @Override
            public void onResponse(Call<ProfileModel> call, retrofit2.Response<ProfileModel> response) {
                if (response.body() != null) {
                    Log.d("getUserProfile.response", response.body().toString());
                    homeFragment.setBalance(response.body().getBalance());
                    homeFragment.viewState();
                    saveData.saveBalance(String.valueOf(response.body().getBalance()));
                    saveData.putStoredValue("authId", response.body().getAuthId());

                    getListRedeem(response.body().getBalance().toString());


                }else if(response.code() == 401){
                    saveData.deleteIdToken();
                    homeFragment.redirectUsertoHome();
                } else{
                    homeFragment.viewStateError();
                }
            }

            @Override
            public void onFailure(Call<ProfileModel> call, Throwable t) {
                Log.d("getUserProfile.response", t.toString());
                t.printStackTrace();
                homeFragment.viewStateError();
            }
        });
    }

    public void getListRedeem(final String balance) {


        array.clear();

        earningTableModel =  new EarningTableModel(1,"","");

        earningList =  new EarningList(earningTableModel);

        array.add(earningList);

        API api = APIClient.getApi(saveData.getIdToken());

        Call<EarnModel> totclient = api.getEarnHistory();
        totclient.enqueue(new Callback<EarnModel>() {
            @Override
            public void onResponse(Call<EarnModel> call, retrofit2.Response<EarnModel>response) {
                if (response.body() != null) {
                    Log.d("getUserProfile.response", response.body().toString());

                    for (int i =0 ;i <response.body().getEarnings().size();i++){

                        array.add(response.body().getEarnings().get(i));
                    }

                    homeFragment.setBalanceAndList(balance,array);

                    homeFragment.viewState();

                }else{

                    homeFragment.viewStateError();
                }
            }

            @Override
            public void onFailure(Call<EarnModel> call, Throwable t) {
                Log.d("getUserProfile.response", t.toString());
                t.printStackTrace();
                homeFragment.viewStateError();
            }
        });

    }

    @Override
    public void setBalance() {
     //   if (saveData.getBalance() != null && saveData.getBalance() != "") {
       //     homeFragment.setBalanceAndList(saveData.getBalance(), array);
        //}
    }

    @Override
    public void logout() {
        saveData.deleteIdToken();
        homeFragment.redirectUsertoHome();
    }
}
