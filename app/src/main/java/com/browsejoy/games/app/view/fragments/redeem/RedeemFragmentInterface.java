package com.browsejoy.games.app.view.fragments.redeem;

import com.browsejoy.games.app.data.model.response.RedeemListModel;
import java.util.List;

/**
 * Created by Gerti on 12/27/2018.
 */

public interface RedeemFragmentInterface {

    void showList(List<RedeemListModel> array);

    void viewState();

    void viewStateError();

    void viewStateLoading();

}
