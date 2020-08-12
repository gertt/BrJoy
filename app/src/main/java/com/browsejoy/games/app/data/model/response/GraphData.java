package com.browsejoy.games.app.data.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Gerti on 9/28/2018.
 */

public class GraphData {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("colum")
    @Expose
    private Integer colum;

    @SerializedName("row")
    @Expose
    private String row;

    @SerializedName("name")
    @Expose
    private Integer name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getColum() {
        return colum;
    }

    public void setColum(Integer colum) {
        this.colum = colum;
    }

    public String getRow() {
        return row;
    }

    public void setRow(String row) {
        this.row = row;
    }

    public Integer getName() {
        return name;
    }

    public void setName(Integer name) {
        this.name = name;
    }
}
