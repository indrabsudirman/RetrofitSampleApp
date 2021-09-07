package com.example.retrofitsampleapp;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface JsonPlaceHolderApi {

    @GET("posts")
    Call<List<Post>> getPost(
            @Query("userId") Integer[] userId,
            @Query("_sort") String sort,
            @Query("_order") String order);

    @GET("post/{id}/comments")
    Call<List<Comment>> getComment(@Path("id") int postId);
}
