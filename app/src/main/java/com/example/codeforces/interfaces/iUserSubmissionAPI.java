package com.example.codeforces.interfaces;

import com.example.codeforces.Models.UserSubmissionModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface iUserSubmissionAPI {
    @GET("user.status")
    public Call<UserSubmissionModel> getUserSubmissions(@Query("handle") String handle);
}
