package com.toandoan.roomdatabase.data.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import com.toandoan.roomdatabase.data.model.User;
import io.reactivex.Flowable;
import java.util.List;

/**
 * Created by toand on 10/16/2017.
 */
@Dao
public interface UserDAO {
    @Query("SELECT * FROM users WHERE id = :userId")
    Flowable<User> getUserByUserId(int userId);

    @Query("SELECT * FROM users WHERE first_name LIKE :userName OR last_name LIKE :userName")
    Flowable<List<User>> getUserByName(String userName);

    @Query("SELECT * FROM users")
    Flowable<List<User>> getALlUser();

    @Insert
    void insertUser(User... users);

    @Delete
    void deleteUser(User user);

    @Query("DELETE FROM users")
    void deleteAllUser();

    @Update
    void updateUser(User... users);
}
