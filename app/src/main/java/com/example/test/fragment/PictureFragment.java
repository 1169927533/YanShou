package com.example.test.fragment;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.R;
import com.example.test.activity.LunBoPictureActivity;
import com.example.test.adapter.NewsAdapter;
import com.example.test.adapter.PictureAdapter;
import com.example.test.controller.NewsController;
import com.example.test.controller.NewsInterface;
import com.example.test.object.NewsObj;
import com.example.test.object.PictureObj;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class PictureFragment extends BaseFragment {


    @BindView(R.id.fra_recycleview)
    RecyclerView recyclerView;
    View view;
    PictureAdapter pictureAdapter;
    NewsInterface newsInterface;


    int news_poage = 1;//加载页数
    int lastVisibleItem;//最后一项显示数据

    public PictureFragment() {

    }

    @Override
    View initview(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        view = inflater.inflate(R.layout.fragment_picture, container, false);
        return view;
    }

    @Override
    void initdata() {
        pictureAdapter = new PictureAdapter(getContext(), PictureObj.list);
        recyclerView.setAdapter(pictureAdapter);
        final GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 4);

        recyclerView.setLayoutManager(layoutManager);

        newsInterface = new NewsController(this);
        newsInterface.getPictureList("1", "16");

        pictureAdapter.setItemClick(new PictureAdapter.ItemClick() {
            @Override
            public void itemclick(int position,View vv) {
                Intent intent = new Intent(getContext(), LunBoPictureActivity.class);
                intent.putExtra("page", position / 16 + 1);//这张图片在的页码位置
                intent.putExtra("position", position);//这张图片在的位置
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), vv, "shareonw");
                startActivity(intent, optionsCompat.toBundle());
            }

        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == pictureAdapter.getItemCount()) {
                    //判断是不是滑到了最后一项
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            news_loadMore();
                            pictureAdapter.changeMoreStatus(NewsAdapter.PULLUP_LOAD_MORE);
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
    }

    public void success(List<PictureObj> success_newslist) {
        int size = PictureObj.list.size();
        PictureObj.list.addAll(success_newslist);
        pictureAdapter.notifyItemRangeChanged(size, PictureObj.list.size());
        pictureAdapter.changeMoreStatus(NewsAdapter.PULLUP_LOAD_MORE);
    }


    public void news_loadMore() {
        news_poage++;
        newsInterface.getPictureList(String.valueOf(news_poage), "16");
    }

}
