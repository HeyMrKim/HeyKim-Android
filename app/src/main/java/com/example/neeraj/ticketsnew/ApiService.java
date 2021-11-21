package com.example.neeraj.ticketsnew;

import com.google.gson.JsonArray;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
    @GET("http://3.38.115.233:8080/all/")
    Call<List<Post>> getData(@Query("userid") String id);

    @GET("http://3.38.115.233:8080/all/")
    Call<List<Post>> gettestdata(@Query("test") String token);



    @POST("http://3.38.115.233:8080/all/")
    Call<Post> postData(@Body HashMap<String,Object> param);




}
