package com.example.codeforces.pojo;

import android.content.Intent;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDao {

    @Insert
    void insert(User user);

    @Delete
    void delete(User user);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(User user);

    @Query("SELECT * FROM user_table ORDER BY rating DESC")
    LiveData<List<User>> getAllUsers();

    @Query("SELECT * FROM user_table WHERE handle = :handle")
    User getUserByHandle(String handle);

    @Query("SELECT COUNT(handle) FROM user_table WHERE handle = :handle")
    Integer hasUser(String handle);

    @Query("SELECT * FROM user_table ORDER BY rating DESC")
    List<User> getAllUsersSync();




}
