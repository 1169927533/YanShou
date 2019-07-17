package com.example.test.controller;

import com.example.test.object.NewsObj;

import java.util.List;

public interface NewsInterface {
    void getNewsList(String start_num,String end_num);//获取指定条数的新闻

    void getPictureList(String start_num,String end_num);//获取指定图片

    void getWeather(String city);//获取天气
}
