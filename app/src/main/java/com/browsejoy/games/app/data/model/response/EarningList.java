package com.browsejoy.games.app.data.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Gerti on 2/6/2019.
 */

public class EarningList {

    @SerializedName("table")
    @Expose
    private EarningTableModel earningTableModel;

    public EarningList(EarningTableModel earningTableModel) {
        this.earningTableModel = earningTableModel;
    }

    public EarningTableModel getTable() {
        return earningTableModel;
    }

    public void setTable(EarningTableModel earningTableModel) {
        this.earningTableModel = earningTableModel;
    }
}
