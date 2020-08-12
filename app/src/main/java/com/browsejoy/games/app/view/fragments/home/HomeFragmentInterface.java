package com.browsejoy.games.app.view.fragments.home;

import com.browsejoy.games.app.data.model.response.EarningList;
import java.util.List;

/**
 * Created by Gerti on 9/28/2018.
 */

public interface HomeFragmentInterface {

    void redirectUsertoHome();

    void viewStateLoading();

    void viewStateError();

    void viewState();

    void logout();

    void setBalanceAndList(String balance, List<EarningList> array);
}
