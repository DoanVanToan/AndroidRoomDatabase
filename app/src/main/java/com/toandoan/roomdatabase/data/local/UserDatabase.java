package com.toandoan.roomdatabase.data.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import com.toandoan.roomdatabase.data.UserDataSource;
import com.toandoan.roomdatabase.data.model.User;

import static com.toandoan.roomdatabase.data.local.UserDatabase.DATABASE_VERSION;

/**
 * Created by toand on 10/16/2017.
 */
@Database(entities = User.class, version = DATABASE_VERSION)
public abstract class UserDatabase extends RoomDatabase{
    private static UserDatabase sUserDatabase;

    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "Room-database";

    public abstract UserDAO userDAO();

    public static UserDatabase getInstance(Context context) {
        if (sUserDatabase == null) {
            sUserDatabase = Room.databaseBuilder(context, UserDatabase.class, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build();
        }
        return sUserDatabase;
    }
}
