package com.example.codeforces.pojo;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Contest.class}, version = 1)
public abstract class ContestDataBase extends RoomDatabase {

    private static ContestDataBase instance;
    public abstract ContestDao contestDao();

    public static synchronized ContestDataBase getInstance(Context context){

        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext()
            ,ContestDataBase.class , "contest_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

}
