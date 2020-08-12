package com.browsejoy.games.app.data.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gerti on 8/2/2018.
 */

public class CategorySubCategoryModel {

    public CategorySubCategoryModel() {

    }

    public List<String> listCategory() {

        List<String>    listDataHeader = new ArrayList<String>();
        listDataHeader.add("Watch Video");
        listDataHeader.add("Play Game And More ");

        return  listDataHeader;

    }

    public List<String> listSubCategoryVideo() {

        List<String> listVideo = new ArrayList<String>();
        listVideo.add("Funny Video");
        listVideo.add("Sport Video");
        listVideo.add("Cooking");

        return  listVideo;

    }

    public List<String> listSubCategoryGame() {

        List<String> listGame = new ArrayList<String>();
        listGame.add("Funny Video");
        listGame.add("Sport Video");
        listGame.add("Cooking");

        return  listGame;
    }

}


