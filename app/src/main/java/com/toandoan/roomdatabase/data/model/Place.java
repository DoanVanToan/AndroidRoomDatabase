package com.toandoan.roomdatabase.data.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by toand on 10/16/2017.
 */
@Entity(tableName = "place")
public class Place {
    @PrimaryKey(autoGenerate = true)
    private int mId;
    @ColumnInfo(name = "lat")
    private double mLat;
    @ColumnInfo(name = "lng")
    private double mLng;
    @ColumnInfo(name = "name")
    private String mName;

    public Place() {
    }

    public Place(String name) {
        mName = name;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public double getLat() {
        return mLat;
    }

    public void setLat(double lat) {
        mLat = lat;
    }

    public double getLng() {
        return mLng;
    }

    public void setLng(double lng) {
        mLng = lng;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }
}
