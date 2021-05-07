package com.example.codeforces.pojo;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.paging.PagedList;
import androidx.paging.PagingData;
import androidx.paging.PagingSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.security.Policy;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import kotlinx.coroutines.flow.Flow;

@Dao
public interface ProblemDao {

    @Insert
    void insert(Problem problem);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertAll(List<Problem> problems);

    @Query("SELECT COUNT(*) FROM problem_table WHERE `index` = :index AND contestId = :contestId")
    int hasProblem(String index, int contestId);


    @Query("SELECT * FROM problem_table")
    Single<List<Problem>> getAllProblems();

    @Query("SELECT * FROM problem_table")
    List<Problem> getAllProblemsSync();

    @Query("SELECT COUNT(*) from problem_table")
    int getSize();

    @Query("SELECT * FROM problem_table WHERE rating >= :start_rating AND rating <= :end_rating AND name LIKE :query")
    Single<List<Problem>>  getProblemsByQuery(int start_rating, int end_rating, String query);

    @Query("SELECT * FROM problem_table WHERE rating >= :start_rating AND rating <= :end_rating")
    Single<List<Problem> > getProblemsByRange(int start_rating, int end_rating);
}
