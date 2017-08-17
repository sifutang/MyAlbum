package com.example.android.myalbum;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by android on 17-8-17.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private List<Bitmap> mDatas;
    private OnRecyclerViewItemListener mOnRecyclerViewItemListener;

    public ImageAdapter(Context context, List<Bitmap> mDatas) {
        mInflater = LayoutInflater.from(context);
        this.mDatas = mDatas;
    }

    public void setmOnRecyclerViewItemListener(OnRecyclerViewItemListener mOnRecyclerViewItemListener) {
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
        holder.imageView.setImageBitmap(mDatas.get(position));
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
