package com.example.test.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import butterknife.ButterKnife;

public abstract class BaseFragment extends Fragment {
    View view;//绑定的视图


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = initview(inflater, container);
        ButterKnife.bind(this, view);
        initdata();
        return view;
    }

    //布局文件初始化
    abstract View initview(@NonNull LayoutInflater inflater, @Nullable ViewGroup container);
    abstract void initdata();

}
