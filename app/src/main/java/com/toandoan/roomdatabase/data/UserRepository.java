package com.toandoan.roomdatabase.data;

import com.toandoan.roomdatabase.data.model.User;
import io.reactivex.Flowable;
import java.util.List;

/**
 * Created by toand on 10/16/2017.
 */

public class UserRepository implements UserDataSource {
    private static UserRepository sInstance;

    public static UserRepository getInstance(UserDataSource localDataSource) {
        if (sInstance == null) {
            sInstance = new UserRepository(localDataSource);
        }
        return sInstance;
    }

    private UserDataSource mLocalDataSource;

    public UserRepository(UserDataSource localDataSource) {
        mLocalDataSource = localDataSource;
    }

    @Override
    public Flowable<User> getUserByUserId(int userId) {
        return mLocalDataSource.getUserByUserId(userId);
    }

    @Override
    public Flowable<List<User>> getUserByName(String userName) {
        return mLocalDataSource.getUserByName(userName);
    }

    @Override
    public Flowable<List<User>> getALlUser() {
        return mLocalDataSource.getALlUser();
    }

    @Override
    public void insertUser(User... users) {
        mLocalDataSource.insertUser(users);
    }

    @Override
    public void deleteUser(User user) {
        mLocalDataSource.deleteUser(user);
    }

    @Override
    public void deleteAllUser() {
        mLocalDataSource.deleteAllUser();
    }

    @Override
    public void updateUser(User... users) {
        mLocalDataSource.updateUser(users);
    }
}
