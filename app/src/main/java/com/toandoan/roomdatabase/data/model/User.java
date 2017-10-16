package com.toandoan.roomdatabase.data.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import io.reactivex.annotations.NonNull;

/**
 * Created by toand on 10/16/2017.
 */
@Entity(tableName = "users")
public class User {
    private static final String DEFAULT_PW = "12345678";
    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int mId;
    @ColumnInfo(name = "first_name")
    private String mFirstName;
    @ColumnInfo(name = "last_name")
    private String mLastName;
    @ColumnInfo(name = "password")
    private String mPassword;
    @Embedded
    private Place mPlace;

    public User() {
    }

    @Ignore
    public User(String firstName, String lastName) {
        mFirstName = firstName;
        mLastName = lastName;
        mPassword = DEFAULT_PW;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String firstName) {
        mFirstName = firstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String lastName) {
        mLastName = lastName;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public static String getDefaultPw() {
        return DEFAULT_PW;
    }

    public Place getPlace() {
        return mPlace;
    }

    public void setPlace(Place place) {
        mPlace = place;
    }

    @Override
    public String toString() {
        if (mPlace != null) {
            return mFirstName + " " + mLastName + "\n" + mPlace.getName();
        }
        return mFirstName + " " + mLastName;
    }
}
