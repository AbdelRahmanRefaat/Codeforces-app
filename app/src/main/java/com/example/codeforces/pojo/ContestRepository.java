package com.example.codeforces.pojo;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ContestRepository {


    private ContestDao contestDao;
    private LiveData<PagedList<Contest>> allContests;

    private PagedList.Config configs = new PagedList.Config.Builder()
            .setPageSize(15)
            .setEnablePlaceholders(true)
            .setPrefetchDistance(45)
            .setInitialLoadSizeHint(15)
            .build();
    public ContestRepository(Application application, long time) {
        ContestDataBase contestDataBase = ContestDataBase.getInstance(application);
        contestDao = contestDataBase.contestDao();



        allContests = new LivePagedListBuilder<>( contestDao.getAllUpComingContests(time),configs)
                                    .build();
    }

    public void insert(Contest contest) {
        new InsertContestsAsyncTask(contestDao).execute(contest);
    }

    public void insertAll(List<Contest> allContests){
        new InsertAllAsyncTask(contestDao).execute(allContests);
    }
    public LiveData<PagedList<Contest>> getAllUpComingContests(long time){
        allContests = new LivePagedListBuilder<>(contestDao.getAllUpComingContests(time),
                configs).build();
        return allContests;
    }

    public LiveData<PagedList<Contest>> getAllPastContests(long time){
        allContests = new LivePagedListBuilder<>(contestDao.getAllPastContests(time),
                configs).build();
        return allContests;
    }

    public LiveData<PagedList<Contest>> getAllContests() {
        return this.allContests;
    }



    public int getContest(int id) {
        try {
            return new GetOneContestAsyncTask(contestDao).execute(id).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;

    }

    private class InsertContestsAsyncTask extends AsyncTask<Contest, Void, Void> {

        ContestDao contestDao;

        public InsertContestsAsyncTask(ContestDao contestDao) {
            this.contestDao = contestDao;
        }

        @Override
        protected Void doInBackground(Contest... contests) {
            contestDao.insert(contests[0]);
            return null;
        }
    }
    private class InsertAllAsyncTask extends  AsyncTask<List<Contest>,Void,Void>{
        private ContestDao contestDao;
        InsertAllAsyncTask(ContestDao contestDao){
            this.contestDao = contestDao;
        }
        @Override
        protected Void doInBackground(List<Contest>... contests) {
            contestDao.insertAll(contests[0]);
            return null;
        }
    }



    private class GetOneContestAsyncTask extends AsyncTask<Integer, Void, Integer> {

        ContestDao contestDao;

        public GetOneContestAsyncTask(ContestDao contestDao) {
            this.contestDao = contestDao;
        }

        @Override
        protected Integer doInBackground(Integer... ids) {
            return contestDao.getContest(ids[0]);
        }
    }

}
