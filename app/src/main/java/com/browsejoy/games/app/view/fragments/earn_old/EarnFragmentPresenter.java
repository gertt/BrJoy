package com.browsejoy.games.app.view.fragments.earn_old;

import android.content.Context;
import com.browsejoy.games.app.data.model.CategorySubCategoryModel;
import com.browsejoy.games.app.data.network.APIClient;
import com.browsejoy.games.app.data.prefs.SaveData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Gerti on 7/23/2018.
 */

public class EarnFragmentPresenter implements EarnFragmentInterface {

    Context context;

    EarnFragment fragmentTab1;

    APIClient apiClient;

    SaveData saveData;

    CategorySubCategoryModel categorySubCategoryModel;

    List<String> listDataHeader = new ArrayList<String>();

    List<String> listChild = new ArrayList<String>();

    List<String> listChild2 = new ArrayList<String>();

    HashMap<String, List<String>> listDataChild = new HashMap<String, List<String>>();

    //Initializing the contex, EarnFragment,and the apiHelper
    public EarnFragmentPresenter(EarnFragment fragmentTab1, Context context) {
        this.fragmentTab1 = fragmentTab1;
        this.context = context;
        saveData = new SaveData(context);
        apiClient = new APIClient();
        categorySubCategoryModel = new CategorySubCategoryModel();
        categorySubCategoryModel.listCategory();
        categorySubCategoryModel.listSubCategoryVideo();
        categorySubCategoryModel.listSubCategoryGame();
    }

    @Override
    public void getCategoryAndSubCategory() {

        for (int i = 0; i < categorySubCategoryModel.listCategory().size(); i++) {
            listDataHeader.add(categorySubCategoryModel.listCategory().get(i));
        }
        for (int i = 0; i < categorySubCategoryModel.listSubCategoryVideo().size(); i++) {
            listChild.add(categorySubCategoryModel.listSubCategoryVideo().get(i));
        }
        for (int i = 0; i < categorySubCategoryModel.listSubCategoryGame().size(); i++) {
            listChild2.add(categorySubCategoryModel.listSubCategoryGame().get(i));
        }
        listDataChild.put(listDataHeader.get(0), listChild);
        listDataChild.put(listDataHeader.get(1), listChild2);
        fragmentTab1.getCategoryAndSubCategory(listDataHeader, listDataChild);

    }
}
