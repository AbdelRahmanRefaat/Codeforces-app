package com.example.codeforces.interfaces;

import com.example.codeforces.Models.ContestModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface iContestAPI {
    @GET("contest.list")
    public Call<ContestModel> getContestsList(@Query("gym") boolean isGym);
}
