package com.browsejoy.games.app.data.network;

import com.browsejoy.games.app.data.model.request.HmacModel;
import com.browsejoy.games.app.data.model.request.WithdrawModel;

import com.browsejoy.games.app.data.model.response.EarnModel;
import com.browsejoy.games.app.data.model.response.ProfileModel;
import com.browsejoy.games.app.data.model.response.RedeemListModel;
import com.browsejoy.games.app.data.model.response.UrlListModel;

import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Gerti on 1/20/18.
 */

public interface API {

    //Get user profile data
    @GET("profile")
    Call<ProfileModel> getUserProfile();

    @GET("top_videos")
    Call<UrlListModel> getVideoList();

    @GET("withdrawal_requests")
    Call<List<RedeemListModel>> getWithDrawList();

    @GET("earning_history")
    Call<EarnModel> getEarnHistory();

    @POST("activity")
    Call<ResponseBody> sendHmac(@Body HmacModel hmacModel);

    @POST("withdrawal_request")
    Call<ResponseBody> sendWithdraw(@Body WithdrawModel withdrawModel);
}

