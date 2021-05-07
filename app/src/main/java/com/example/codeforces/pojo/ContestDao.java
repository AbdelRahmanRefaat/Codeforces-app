package com.example.codeforces.pojo;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ContestDao {

    @Insert
    void insert(Contest contest);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Contest> contests);

    @Delete
    void delete(Contest contest);

    @Update
    void update(Contest contest);

    @Query("SELECT * FROM contest_table ORDER BY startTimeSeconds DESC")
    DataSource.Factory<Integer,Contest> getAllContests();

    @Query("SELECT COUNT(*) FROM contest_table WHERE id = :id")
    int getContest(int id);

    @Query("SELECT * FROM contest_table WHERE (startTimeSeconds + durationSeconds) >= :time ORDER BY startTimeSeconds ASC")
    DataSource.Factory<Integer,Contest> getAllUpComingContests(long time);

    @Query("SELECT * FROM contest_table WHERE (startTimeSeconds + durationSeconds) < :time ORDER BY startTimeSeconds DESC")
    DataSource.Factory<Integer,Contest> getAllPastContests(long time);

}
