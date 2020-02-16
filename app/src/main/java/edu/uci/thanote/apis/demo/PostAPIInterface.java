package edu.uci.thanote.apis.demo;

import retrofit2.Call;
import retrofit2.http.GET;

import java.util.List;

public interface PostAPIInterface {

    @GET("posts")
    Call<List<Post>> getPosts();
}
