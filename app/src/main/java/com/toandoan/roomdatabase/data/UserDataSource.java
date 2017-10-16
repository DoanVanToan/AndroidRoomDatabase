package com.toandoan.roomdatabase.data;

import com.toandoan.roomdatabase.data.model.User;
import io.reactivex.Flowable;
import java.util.List;

/**
 * Created by toand on 10/16/2017.
 */

public interface UserDataSource {
    Flowable<User> getUserByUserId(int userId);

    Flowable<List<User>> getUserByName(String userName);

    Flowable<List<User>> getALlUser();

    void insertUser(User... users);

    void deleteUser(User user);

    void deleteAllUser();

    void updateUser(User... users);
}
