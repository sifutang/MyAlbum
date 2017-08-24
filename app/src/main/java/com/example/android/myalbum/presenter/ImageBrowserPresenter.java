package com.example.android.myalbum.presenter;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.android.myalbum.activity.ImageBrowserActivity;
import com.example.android.myalbum.adapter.ImageAdapter;
import com.example.android.myalbum.model.ImageDataSource;
import com.example.android.myalbum.util.OnRecyclerViewItemListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by android on 17-8-23.
 */

public class ImageBrowserPresenter {

    private static final String TAG = "ImageBrowserPresenter";

    private static final String SELECTED_IMAGES_KEY = "selected_images";

    private ImageBrowserActivity mContext;
    private ImageAdapter mAdapter;
    private List<String> mImagePathList;
    private ArrayList<String> mSelectedImagePathList;
    private List<View> mSelectedViews;

    public ImageBrowserPresenter(ImageBrowserActivity context) {
        this.mContext = context;
        this.mSelectedImagePathList = new ArrayList<>();
        this.mImagePathList = new ArrayList<>();
        this.mSelectedViews = new ArrayList<>();
    }

    public void selectImages() {
        Intent intent = new Intent();
        intent.putStringArrayListExtra(SELECTED_IMAGES_KEY, mSelectedImagePathList);
        mContext.setResult(0, intent);
        mContext.finish();
    }

    public void cancle() {
        mSelectedImagePathList.clear();
        for (View view : mSelectedViews) {
            view.setBackgroundColor(Color.parseColor("#F5F5F5"));
        }
    }

    public void updateUI() {
        ImageDataSource dataSource = new ImageDataSource(mContext.getSupportLoaderManager(), mContext);
        dataSource.setListener(new ImageDataSource.FetchDataListener() {
            @Override
            public void fetchDataSourceSuccess(List<String> list) {
                configAdapter(mContext.getImageRecyclerView(), list);
            }
        });
    }

    private void configAdapter(RecyclerView recyclerView, final List<String> list) {
        if (mAdapter == null) {
            mAdapter = new ImageAdapter(mContext, list);
            recyclerView.setAdapter(mAdapter);
            mAdapter.setOnRecyclerViewItemListener(new OnRecyclerViewItemListener() {
                @Override
                public void onItemClick(View view, int position) {
                }

                @Override
                public void onItemLongClick(View view, int position) {
                    view.setBackgroundColor(Color.DKGRAY);
                    mSelectedViews.add(view);
                    mSelectedImagePathList.add(list.get(position));
                }
            });
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }
}
