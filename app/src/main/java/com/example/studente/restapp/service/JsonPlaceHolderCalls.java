package com.example.studente.restapp.service;

import com.example.studente.restapp.Post;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface JsonPlaceHolderCalls {
    @GET("/posts")
    Call<List<Post>> getPosts();

    @GET("/posts/{postId}")
    Call<Post> getComments(@Path("postId") int postId);
}
