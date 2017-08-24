package com.example.android.myalbum.model;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by android on 17-8-24.
 */

public class ImageDataSource implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "ImageDataSource";

    public interface FetchDataListener {
        void fetchDataSourceSuccess(List<String> list);
    }

    private static final int IMAGE_LOADER_EXTERNAL_ID = 0;
    private static final int IMAGE_LOADER_INTERNAL_ID = 1;

    private LoaderManager mLoaderManager;
    private Context mContext;
    private FetchDataListener mListener;
    private CursorLoader mCursorLoaderExternal;
    private CursorLoader mCursorLoaderInternal;
    private List<String> mDates;

    public ImageDataSource(LoaderManager loaderManager, Context context) {
        mLoaderManager = loaderManager;
        mContext = context;
        mDates = new ArrayList<>();

        mLoaderManager.initLoader(IMAGE_LOADER_EXTERNAL_ID, null, this);
        mLoaderManager.initLoader(IMAGE_LOADER_INTERNAL_ID, null, this);
    }

    public void setListener(FetchDataListener listener) {
        mListener = listener;
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
            mDates.add(imagePath);
        }

        if (mListener != null) {
            mListener.fetchDataSourceSuccess(mDates);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d(TAG, "onLoaderReset: ");
    }
}
