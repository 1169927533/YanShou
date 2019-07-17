package com.example.test.controller;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.test.fragment.NewsFragment;
import com.example.test.fragment.PictureFragment;
import com.example.test.net.Net;
import com.example.test.object.NewsObj;
import com.example.test.object.PictureObj;
import com.example.test.object.Weather;
import com.example.test.retrofit_study.dao.ApiServer;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewsController implements NewsInterface {

    private static NewsFragment newsFragment;
    private static PictureFragment pictureFragment;

    Gson gson = new Gson();

    //新闻数据
    static List<NewsObj> newsObjslist;
    //图片信息
    static List<PictureObj> pictureObjslist;


    public NewsController(NewsFragment newsFragment) {
        this.newsFragment = newsFragment;
    }
    public NewsController(PictureFragment pictureFragment) {
        this.pictureFragment = pictureFragment;
    }

    MyHandler myHandler = new MyHandler(this);

    @Override
    public void getNewsList(String start_num, String end_num) {
        newsObjslist = new ArrayList<>();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Net.news_Netease)
                .build();
        ApiServer apiServer = retrofit.create(ApiServer.class);
        final retrofit2.Call<ResponseBody> call = apiServer.getNews(start_num, end_num);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {
                    String content = response.body().string();
                    JSONObject jsonObject = new JSONObject(content);
                    if (jsonObject.getString("code").equals("200")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            NewsObj newsObj = gson.fromJson(jsonArray.get(i).toString(), NewsObj.class);
                            newsObjslist.add(newsObj);
                        }

                        myHandler.sendEmptyMessage(1);
                    }
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

       /*以下是okhttp的方法
        newsObjslist = new ArrayList<>();
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(Net.news_Netease + "?page=" + start_num + "&count=" + end_num)
                .get()//默认就是GET请求，可以不写
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String content = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(content);
                    if (jsonObject.getString("code").equals("200")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            NewsObj newsObj = gson.fromJson(jsonArray.get(i).toString(), NewsObj.class);
                            newsObjslist.add(newsObj);
                        }

                        handler.sendEmptyMessage(1);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });*/
    }

    @Override
    public void getPictureList(String start_num, String end_num) {
        pictureObjslist = new ArrayList<>();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Net.picture)
                .build();
        ApiServer apiServer = retrofit.create(ApiServer.class);
        final retrofit2.Call<ResponseBody> call = apiServer.getImgs(start_num, end_num);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {
                    String content = response.body().string();
                    JSONObject jsonObject = new JSONObject(content);
                    if (jsonObject.getString("code").equals("200")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            PictureObj newsObj = gson.fromJson(jsonArray.get(i).toString(), PictureObj.class);
                            pictureObjslist.add(newsObj);
                        }
                        myHandler.sendEmptyMessage(2);
                    }
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });


    }

    @Override
    public void getWeather(String city) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Net.weather_forecast)
                .build();
        ApiServer apiServer = retrofit.create(ApiServer.class);
        final retrofit2.Call<ResponseBody> call = apiServer.getWeather(city);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String content = response.body().string();
                    JSONObject jsonObject = new JSONObject(content);
                    if (jsonObject.getString("code").equals("200")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        String city = jsonObject1.getString("city");
                        String api = jsonObject1.getString("aqi");
                        if(jsonObject1.getString("aqi") == "null" || jsonObject1.getString("aqi").equals("null")) {
                            api = "暂无数据";
                        }
                        JSONObject jsonObject2 = jsonObject1.getJSONArray("forecast").getJSONObject(3);
                        String maxt = jsonObject2.getString("high");
                        String mint = jsonObject2.getString("low");
                        String type = jsonObject2.getString("type");
                        Weather weather = new Weather(city,api,maxt,mint,type);
                        Message msg = Message.obtain();
                        msg.what = 3;
                        Bundle b = new Bundle();
                        b.putParcelable("MyObject", (Parcelable) weather);
                        msg.setData(b);
                        myHandler.sendMessage(msg);
                    }
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }


    static class MyHandler extends Handler{
        WeakReference<Object> mWeakReference;
        public MyHandler(Object activity){
            mWeakReference = new WeakReference<Object>(activity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            final Object activity = mWeakReference.get();
            if(activity != null){
                if (msg.what == 1) {
                    newsFragment.success(newsObjslist);
                }else if (msg.what == 2) {
                    pictureFragment.success(pictureObjslist);
                }else if(msg.what == 3){
                    Weather objectRcvd = (Weather) msg.getData().getParcelable("MyObject");
                    newsFragment.weather_success(objectRcvd);

                }
            }
        }
    }

}
