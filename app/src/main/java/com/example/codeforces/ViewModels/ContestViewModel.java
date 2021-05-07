package com.example.codeforces.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.PagedList;

import com.example.codeforces.pojo.Contest;
import com.example.codeforces.pojo.ContestRepository;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ContestViewModel extends AndroidViewModel {

    private ContestRepository repository;
    private LiveData<PagedList<Contest>> allContests;
    private LiveData<PagedList<Contest>> allUpComingContests;
    private LiveData<PagedList<Contest>> allPastContests;

    public ContestViewModel(@NonNull Application application) {
        super(application);
        repository = new ContestRepository(application,System.currentTimeMillis()/1000);
        allUpComingContests = repository.getAllUpComingContests(System.currentTimeMillis()/1000);
        allPastContests = repository.getAllPastContests(System.currentTimeMillis()/1000);
    }

    public void insertAll(List<Contest> allContests){
        repository.insertAll(allContests);
    }

    public void insert(Contest contest) {
        repository.insert(contest);
    }

    public LiveData<PagedList<Contest>> getAllContests() {
        return this.allContests;
    }

    public int getContest(int id)  {
        return repository.getContest(id);
    }

    public LiveData<PagedList<Contest>> getAllUpComingContests(long time){
        return repository.getAllUpComingContests(time);
    }
    public LiveData<PagedList<Contest>> getAllPastContests(long time){
        return repository.getAllPastContests(time);
    }


}
