package com.example.android.myalbum;

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
import com.example.android.myalbum.util.OnRecyclerViewItemListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by android on 17-8-23.
 */

public class ImageBrowserPresenter  implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "ImageBrowserPresenter";

    private static final String SELECTED_IMAGES_KEY = "selected_images";
    private static final int IMAGE_LOADER_EXTERNAL_ID = 0;
    private static final int IMAGE_LOADER_INTERNAL_ID = 1;

    private ImageBrowserActivity mContext;
    private ImageAdapter mAdapter;
    private List<String> mImagePathList;
    private ArrayList<String> mSelectedImagePathList;
    private List<View> mSelectedViews;

    private CursorLoader mCursorLoaderExternal;
    private CursorLoader mCursorLoaderInternal;

    public ImageBrowserPresenter(ImageBrowserActivity context) {
        this.mContext = context;

        this.mSelectedImagePathList = new ArrayList<>();
        this.mImagePathList = new ArrayList<>();
        this.mSelectedViews = new ArrayList<>();

        LoaderManager manager = mContext.requestLoaderManager();
        manager.initLoader(IMAGE_LOADER_EXTERNAL_ID, null, this);
        manager.initLoader(IMAGE_LOADER_INTERNAL_ID, null, this);
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


    private void configAdapter(RecyclerView recyclerView) {
        if (mAdapter == null) {
            mAdapter = new ImageAdapter(mContext, mImagePathList);
            recyclerView.setAdapter(mAdapter);
            mAdapter.setOnRecyclerViewItemListener(new OnRecyclerViewItemListener() {
                @Override
                public void onItemClick(View view, int position) {
                }

                @Override
                public void onItemLongClick(View view, int position) {
                    view.setBackgroundColor(Color.DKGRAY);
                    mSelectedViews.add(view);
                    mSelectedImagePathList.add(mImagePathList.get(position));
                }
            });
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case IMAGE_LOADER_EXTERNAL_ID:
                mCursorLoaderExternal = new CursorLoader(mContext.getApplicationContext(),
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        null,
                        null,
                        null,
                        MediaStore.Images.Media.DEFAULT_SORT_ORDER);
                return mCursorLoaderExternal;

            case IMAGE_LOADER_INTERNAL_ID:
                mCursorLoaderInternal = new CursorLoader(mContext.getApplicationContext(),
                        MediaStore.Images.Media.INTERNAL_CONTENT_URI,
                        null,
                        null,
                        null,
                        MediaStore.Images.Media.DEFAULT_SORT_ORDER);
                return mCursorLoaderInternal;
        }

        return null;
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        while (data.moveToNext()) {
            String imagePath = data.getString(data.getColumnIndex(MediaStore.Images.Media.DATA));
            mImagePathList.add(imagePath);
        }

        configAdapter(mContext.getImageRecyclerView());
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d(TAG, "onLoaderReset: ");
    }
}
