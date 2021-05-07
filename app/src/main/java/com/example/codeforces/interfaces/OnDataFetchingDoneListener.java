package com.example.codeforces.interfaces;

import com.example.codeforces.Models.ProblemModel;

import java.util.List;

public interface OnDataFetchingDoneListener {
    void onDataFetchCompleted(List<ProblemModel.Result.P> allProblems, List<ProblemModel.Result.ProblemStatistics> problemStatistics );

}
