package com.example.codeforces.pojo;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Problem.class}, version = 1)
public abstract class ProblemDataBase extends RoomDatabase {

    private static ProblemDataBase instance;
    public abstract ProblemDao problemDao();

    public static synchronized ProblemDataBase getInstance(Context context) {

        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    ProblemDataBase.class, "problem_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

}
