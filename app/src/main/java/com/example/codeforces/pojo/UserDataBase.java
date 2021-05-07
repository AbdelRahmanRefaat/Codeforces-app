package com.example.codeforces.pojo;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {User.class} , version = 1)
public abstract class UserDataBase extends RoomDatabase {

    private static final String TAG = "UserDataBase";
    private static UserDataBase instance;

    public abstract UserDao userDao();

    public static synchronized UserDataBase getInstance(Context context){
        Log.d(TAG, "getInstance: getting an instance and creating database");
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext()
            ,UserDataBase.class , "user_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
