package com.example.codeforces.pojo;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;


import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import io.reactivex.Completable;
import io.reactivex.Single;

public class ProblemRepository {

    private static final String TAG = "ProblemRepository";
    private ProblemDao problemDao;

    public ProblemRepository(Application application) {
        ProblemDataBase dataBase = ProblemDataBase.getInstance(application);
        problemDao = dataBase.problemDao();
    }
    public Single<List<Problem>> getProblemsByQuery(int start_rating, int end_rating, String query){
        return problemDao.getProblemsByQuery(start_rating,end_rating,query);
//        try {
//            return new GetProblemsByQueryAsyncTask(problemDao,start_rating,end_rating,query).execute().get();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        return Collections.emptyList();
    }
    public Single<List<Problem> > getProblemsByRange(int start_rating, int end_rating){
        return problemDao.getProblemsByRange(start_rating,end_rating);
//        try {
//            return new GetProblemsByRangeAsyncTask(problemDao,start_rating,end_rating).execute().get();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        return Collections.emptyList();
    }
    public void insert(Problem problem) {
        new InsertProblemAsyncTask(problemDao).execute(problem);
    }
    public Completable insertAll(List<Problem> problems){
        return problemDao.insertAll(problems);
       // new InsertAllProblemsAsyncTask(problemDao).execute(problems);
    }

    public int getSize(){

        try {
            return new GetSizeAsyncTask(problemDao).execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public Single<List<Problem>  > getAllProblems() {
        return problemDao.getAllProblems();
//        try {
//            return new GetAllProblemsAsyncTask(problemDao).execute().get();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        return Collections.emptyList();
    }


    public int hasProblem(String index, int contestId) {
        return problemDao.hasProblem(index, contestId);
    }

//    private class GetAllProblemsAsyncTask extends AsyncTask<Void, Void, List<Problem>>{
//        ProblemDao problemDao;
//        GetAllProblemsAsyncTask(ProblemDao problemDao){
//            this.problemDao = problemDao;
//        }
//        @Override
//        protected List<Problem> doInBackground(Void... voids) {
//
//            return problemDao.getAllProblems();
//        }
//    }
//    private class GetProblemsByQueryAsyncTask extends AsyncTask<Void, Void, List<Problem>>{
//        int start_rating;
//        int end_rating;
//        String name;
//        ProblemDao problemDao;
//        GetProblemsByQueryAsyncTask(ProblemDao problemDao, int start_rating,int end_rating, String name){
//            this.problemDao = problemDao;
//            this.start_rating = start_rating;
//            this.end_rating = end_rating;
//            this.name = name;
//        }
//        @Override
//        protected List<Problem> doInBackground(Void... voids) {
//            return problemDao.getProblemsByQuery(start_rating,end_rating,name);
//        }
//    }
    private class InsertProblemAsyncTask extends AsyncTask<Problem, Void, Void> {
        private ProblemDao problemDao;

        InsertProblemAsyncTask(ProblemDao problemDao) {
            this.problemDao = problemDao;
        }

        @Override
        protected Void doInBackground(Problem... problems) {
            problemDao.insert(problems[0]);
            return null;
        }
    }
    private class InsertAllProblemsAsyncTask extends AsyncTask<List<Problem>, Void, Void> {
        private ProblemDao problemDao;

        InsertAllProblemsAsyncTask(ProblemDao problemDao) {
            this.problemDao = problemDao;
        }


        @Override
        protected Void doInBackground(List<Problem>... problems) {
            problemDao.insertAll(problems[0]);

            return null;
        }
    }

    private class HasProblemAsyncTask extends AsyncTask<Void, Void, Integer> {

        String index;
        int contestId;
        private ProblemDao problemDao;

        HasProblemAsyncTask(ProblemDao problemDao, String index, int contestId) {
            this.problemDao = problemDao;
            this.index = index;
            this.contestId = contestId;
        }

        @Override
        protected Integer doInBackground(Void... Voids) {
            return problemDao.hasProblem(index, contestId);
        }
    }
    private class GetSizeAsyncTask extends AsyncTask<Void,Void,Integer>{
        private ProblemDao problemDao;
        GetSizeAsyncTask(ProblemDao problemDao){
            this.problemDao = problemDao;
        }
        @Override
        protected Integer doInBackground(Void... voids) {

            return problemDao.getSize();
        }
    }
//    private class GetProblemsByRangeAsyncTask extends AsyncTask<Void, Void, List<Problem>>{
//        int start_rating;
//        int end_rating;
//        ProblemDao problemDao;
//        GetProblemsByRangeAsyncTask(ProblemDao problemDao, int start_rating,int end_rating){
//            this.problemDao = problemDao;
//            this.start_rating = start_rating;
//            this.end_rating = end_rating;
//        }
//        @Override
//        protected List<Problem> doInBackground(Void... voids) {
//            return problemDao.getProblemsByRange(start_rating,end_rating);
//        }
//    }
}
