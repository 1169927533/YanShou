package com.example.test.fragment;


import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.R;
import com.example.test.activity.NewsInfoActivity;
import com.example.test.adapter.NewsAdapter;
import com.example.test.controller.NewsController;
import com.example.test.controller.NewsInterface;
import com.example.test.object.NewsObj;
import com.example.test.object.Weather;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class NewsFragment extends BaseFragment {

    @BindView(R.id.fra_recycleview)
    RecyclerView fraRecycleview;

    View view;

    NewsAdapter newsAdapter;//适配器

    List<NewsObj> newsObjList;//新闻数据

    NewsInterface newsInterface;
    @BindView(R.id.weather)
    TextView weather;
    @BindView(R.id.temperature)
    TextView temperature;
    @BindView(R.id.aqi)
    TextView aqi;


    public NewsFragment() {

    }

    @Override
    View initview(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        view = inflater.inflate(R.layout.fragment_news, container, false);
        return view;
    }

    @Override
    void initdata() {
        newsObjList = new ArrayList<>();
        newsAdapter = new NewsAdapter(getContext(), newsObjList);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        fraRecycleview.setLayoutManager(layoutManager);
        fraRecycleview.setAdapter(newsAdapter);

        fraRecycleview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == newsAdapter.getItemCount()) {
                    //判断是不是滑到了最后一项
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            news_loadMore();
                            newsAdapter.changeMoreStatus(NewsAdapter.PULLUP_LOAD_MORE);
                        }
                    }, 1000);
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
            }
        });

        newsInterface = new NewsController(this);
        newsInterface.getNewsList("1", "5");
        newsInterface.getWeather("杭州");

        newsAdapter.setItemClick(new NewsAdapter.ItemClick() {
            @Override
            public void itemclick(int position) {
                Intent intent = new Intent(getContext(), NewsInfoActivity.class);
                intent.putExtra("info", newsObjList.get(position).getPath());
                startActivity(intent);
            }
        });
    }

    int lastVisibleItem;


    public void success(List<NewsObj> success_newslist) {
        int size = newsObjList.size();
        newsObjList.addAll(success_newslist);
        newsAdapter.notifyItemRangeChanged(size, newsObjList.size());
        newsAdapter.changeMoreStatus(NewsAdapter.PULLUP_LOAD_MORE);
    }

    int news_poage = 1;

    public void news_loadMore() {
        news_poage++;
        newsInterface.getNewsList(String.valueOf(news_poage), "5");
    }
    public void weather_success(Weather weathers){
        weather.setText(weathers.getType());
        aqi.setText(weathers.getApi());
        temperature.setText("  "+weathers.getMint()+"-"+weathers.getMaxt());
    }

}

