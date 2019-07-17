package com.example.test.retrofit_study.dao;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiServer {
    @GET("getWangYiNews/")
    Call<ResponseBody> getNews(@Query("page")String page,@Query("count")String count);


    @GET("getImages/")
    Call<ResponseBody> getImgs(@Query("page")String page,@Query("count")String count);

    @GET("weatherApi")
    Call<ResponseBody> getWeather(@Query("city")String city);

}
