package com.example.android.myalbum.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.myalbum.util.ImageLoader.ImageLoader;
import com.example.android.myalbum.Interface.OnRecyclerViewItemListener;
import com.example.android.myalbum.R;

import java.util.List;

/**
 * Created by android on 17-8-17.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private static final String TAG = "ImageAdapter";

    private static final int ITEM_COLOR_STATE_NORMAL = Color.parseColor("#F5F5F5");
    private LayoutInflater mInflater;
    private List<String> mDatas;
    private OnRecyclerViewItemListener mOnRecyclerViewItemListener;

    public ImageAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    public ImageAdapter(Context context, List<String> datas) {
        mInflater = LayoutInflater.from(context);
        this.mDatas = datas;
    }

    public void setDatas(List<String> datas) {
        mDatas = datas;
    }

    public void setOnRecyclerViewItemListener(OnRecyclerViewItemListener mOnRecyclerViewItemListener) {
        this.mOnRecyclerViewItemListener = mOnRecyclerViewItemListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.image_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view, mOnRecyclerViewItemListener);
        viewHolder.imageView = view.findViewById(R.id.image_view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ImageLoader.getInstance().displayImage(mDatas.get(position), holder.imageView);
        holder.itemView.setBackgroundColor(ITEM_COLOR_STATE_NORMAL);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {

        private ImageView imageView;
        private OnRecyclerViewItemListener mOnRecyclerViewItemListener;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        public ViewHolder(View itemView, OnRecyclerViewItemListener mOnRecyclerViewItemListener) {
            super(itemView);
            this.mOnRecyclerViewItemListener = mOnRecyclerViewItemListener;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mOnRecyclerViewItemListener != null) {
                mOnRecyclerViewItemListener.onItemClick(view, getAdapterPosition());
            }
        }

        @Override
        public boolean onLongClick(View view) {
            if (mOnRecyclerViewItemListener != null) {
                mOnRecyclerViewItemListener.onItemLongClick(view, getAdapterPosition());
            }
            return true;
        }
    }
}
