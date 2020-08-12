package com.browsejoy.games.app.data.model.response;

import android.support.annotation.Nullable;
import android.view.View;

public class EarnDataModel {

    private int position;

    private String title;
    private String point;
    private String earnTittle;
    private String activityName;

    @Nullable
    public View.OnClickListener onClickListener;

    public EarnDataModel(int position, String title, String point, String earnTittle, String activityName, @Nullable View.OnClickListener listener) {
        this.position = position;
        this.title = title;
        this.point = point;
        this.earnTittle = earnTittle;
        this.activityName = activityName;
        this.onClickListener = listener;

    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getEarnTittle() {
        return earnTittle;
    }

    public void setEarnTittle(String earnTittle) {
        this.earnTittle = earnTittle;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }
}