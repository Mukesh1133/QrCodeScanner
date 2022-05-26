package com.example.ushopcodescanner;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RetrofitAPI {

    @POST("/fcm")
    Call<String> createPosts(@Query("u") String uid,
                               @Query("f") String token);
//    @POST("/fcm")
//    Call<UserData> createPosts(UserData post);

//    Call<Post> createPosts(Post post);
}