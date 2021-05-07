package com.example.codeforces.interfaces;

import com.example.codeforces.Models.ProblemModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface iProblemAPI {

    @GET("problemset.problems/")
    Call<ProblemModel> getAllProblems();
}
