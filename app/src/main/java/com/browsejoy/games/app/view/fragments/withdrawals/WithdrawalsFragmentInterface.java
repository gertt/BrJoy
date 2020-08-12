package com.browsejoy.games.app.view.fragments.withdrawals;

import com.browsejoy.games.app.data.model.response.RedeemListModel;
import java.util.List;

/**
 * Created by Gerti on 12/22/2018.
 */

public interface WithdrawalsFragmentInterface {

    void viewStateLoading();

    void viewState();

    void viewStateError();

    void showList(List<RedeemListModel> array);
}
