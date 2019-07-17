package com.example.test.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.test.R;
import com.example.test.object.PictureObj;

import java.util.ArrayList;
import java.util.List;

public class PictureAdapter extends RecyclerView.Adapter {
    private List<PictureObj> img_list;//新闻列表数据
    private Context context;
    private static final int TYPE_IOTEM = 0;//普通的item view
    private static final int TYPE_FOOTER = 1;//底部的Footview
    private int load_more_status = 0;//默认下拉状态为0
    public static final int PULLUP_LOAD_MORE = 0; //上拉加载更多
    public static final int LOADING_MORE = 1; //正在加载中

    public PictureAdapter(Context context, List<PictureObj> img_list) {
        this.img_list = img_list;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_IOTEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_picture, parent, false);
            MyViewHolder viewHolder = new MyViewHolder(view);
            return viewHolder;
        } else if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loadmore, parent, false);
            FootViewHolder footViewHolder = new FootViewHolder(view);
            return footViewHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MyViewHolder) {
            Glide.with(context).load(img_list.get(position).getImg()).into(((MyViewHolder) holder).viewpage_image);
            ((MyViewHolder) holder).img_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClick.itemclick(position,holder.itemView);
                }
            });
        } else {
            FootViewHolder footViewHolder = (FootViewHolder) holder;
            switch (load_more_status) {
                case PULLUP_LOAD_MORE:
                    footViewHolder.load_more_text.setText("上拉加载更多...");
                    break;
                case LOADING_MORE:
                    footViewHolder.load_more_text.setText("正在加载更多数据...");
                    break;
            }
        }

    }

    @Override
    public int getItemCount() {
        return img_list.size() + 1;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView viewpage_image;//图片
        LinearLayout img_item;//整个item


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            img_item = (LinearLayout) itemView.findViewById(R.id.img_item);
            viewpage_image = (ImageView) itemView.findViewById(R.id.viewpage_image);

        }
    }

    public class FootViewHolder extends RecyclerView.ViewHolder {
        TextView load_more_text;//加载时的文字提醒

        public FootViewHolder(@NonNull View itemView) {
            super(itemView);
            load_more_text = (TextView) itemView.findViewById(R.id.load_more_text);
        }
    }

    @Override
    public int getItemViewType(int position) {
        //如果当前为最底下的item则显示到达最底部
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_IOTEM;
        }
    }

    public void changeMoreStatus(int status) {
        load_more_status = status;
        notifyDataSetChanged();
    }

    public interface ItemClick {
        void itemclick(int position,View view);
    }

    public ItemClick itemClick;

    public void setItemClick(ItemClick itemClick) {
        this.itemClick = itemClick;
    }
}