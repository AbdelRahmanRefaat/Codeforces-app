package com.example.codeforces.interfaces;

import com.example.codeforces.Models.UserRatingModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface iUserRatingAPI {
    @GET("user.rating")
    public Call<UserRatingModel> getUserRatingChanges(@Query("handle") String handle);
}
