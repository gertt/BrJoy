package com.browsejoy.games.app.view.fragments.earn_old;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Gerti on 7/23/2018.
 */

public interface EarnFragmentView {

    void getCategoryAndSubCategory(List<String> listDataHeader, HashMap<String, List<String>> listDataChild);

    void viewStateLoading();

    void viewStateError();

    void viewState();
}
