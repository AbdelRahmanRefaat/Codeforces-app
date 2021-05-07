package com.example.codeforces.ViewModels;

import android.app.Application;
import android.util.Log;
import android.view.animation.Transformation;
import android.widget.SpinnerAdapter;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.example.codeforces.pojo.Problem;
import com.example.codeforces.pojo.ProblemRepository;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

public class ProblemViewModel extends AndroidViewModel {

    private static final String TAG = "ProblemViewModel";
    private ProblemRepository repository;

    public ProblemViewModel(@NonNull Application application) {
        super(application);
        repository = new ProblemRepository(application);
    }
    public void insert(Problem problem){
        repository.insert(problem);
    }

    public Completable insertAll(List<Problem> problems){

        return repository.insertAll(problems);
    }

    public int getSize(){
        return repository.getSize();
    }


    public int hasProblem(String index, int contestId){
        return repository.hasProblem(index, contestId);
    }

    public Single<List<Problem> > getAllProblems(){
        return repository.getAllProblems();
    }
    public Single<List<Problem> > getProblemsByRange(int start_rating, int end_rating){
        return repository.getProblemsByRange(start_rating,end_rating);
    }
    public Single<List<Problem> > getProblemsByQuery(int start_rating, int end_rating, String name){
       return repository.getProblemsByQuery(start_rating,end_rating,name);
    }

}
