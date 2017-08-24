package com.example.android.myalbum.presenter;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.android.myalbum.activity.ImageBrowserActivity;
import com.example.android.myalbum.adapter.ImageAdapter;
import com.example.android.myalbum.model.ImageDataSource;
import com.example.android.myalbum.util.FetchDataListener;
import com.example.android.myalbum.util.OnRecyclerViewItemListener;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

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
//        updateUIViaLoaderManager();
        updateUIViaRxjava();
    }

    private void updateUIViaLoaderManager() {
        ImageDataSource dataSource = new ImageDataSource(mContext.getSupportLoaderManager(), mContext);
        dataSource.setListener(new FetchDataListener() {
            @Override
            public void fetchDataSourceSuccess(List<String> list) {
                configAdapter(mContext.getImageRecyclerView(), list);
            }
        });
    }

    private void updateUIViaRxjava() {
        Uri[] imageUris = {
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                MediaStore.Images.Media.INTERNAL_CONTENT_URI
        };

        Observable.from(imageUris)
                .subscribeOn(Schedulers.io())
                .map(new Func1<Uri, List<String>>() {
                    @Override
                    public List<String> call(Uri uri) {
                        List<String> imagePathList = new ArrayList<>();
                        Cursor cursor = MediaStore.Images.Media.query(mContext.getContentResolver(),
                                uri,
                                new String[] { MediaStore.Images.Media.DATA });
                        cursor.moveToFirst();
                        while (cursor.moveToNext()) {
                            String imagePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                            imagePathList.add(imagePath);
                        }
                        return imagePathList;
                    }
                })
                .subscribe(new Subscriber<List<String>>() {
                    @Override
                    public void onCompleted() {
                        configAdapter(mContext.getImageRecyclerView(), mImagePathList);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(List<String> list) {
                        mImagePathList.addAll(list);
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
