package com.elkhamitech.projectkeeper.data.roomdatabase.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.elkhamitech.projectkeeper.data.roomdatabase.model.UserModel;

@Dao
public interface UserDao {

    @Insert
    long createUser (UserModel user);

    @Query("SELECT * FROM users WHERE pin = :pinCode")
    UserModel getRegisteredUser(String pinCode);
}