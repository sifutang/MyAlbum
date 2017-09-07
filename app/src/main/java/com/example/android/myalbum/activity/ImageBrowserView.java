package com.example.android.myalbum.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.android.myalbum.Interface.IAlbumView;
import com.example.android.myalbum.adapter.ImageAdapter;
import com.example.android.myalbum.model.ImageDataSource;
import com.example.android.myalbum.presenter.ImageBrowserPresenter;
import com.example.android.myalbum.R;
import com.example.android.myalbum.Interface.OnRecyclerViewItemListener;
import com.example.android.myalbum.util.ScreenSizeHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by android on 17-8-17.
 */

public class ImageBrowserView extends BaseActivity<IAlbumView, ImageBrowserPresenter> implements IAlbumView {
    public static final int REQUEST_READ_EXTERNAL_STORAGE = 0;

    private static final String SELECTED_IMAGES_KEY = "selected_images";
    private RecyclerView mRecyclerView;
    private ImageAdapter mAdapter;
    private List<String> mDatas;
    private ArrayList<String> mSelectedDatas;
    private List<View> mSelectedViews;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_browser);

        checkReadExternalStoragePermission();
        initCollection();
        configRecyclerView();

        mPresenter.loadAlbum();
    }

    private void initCollection() {
        mDatas = new ArrayList<>();
        mSelectedViews = new ArrayList<>();
        mSelectedDatas = new ArrayList<>();
    }

    @Override
    protected ImageBrowserPresenter createPresenter() {
        return new ImageBrowserPresenter(this, new ImageDataSource());
    }

    private void checkReadExternalStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE }, REQUEST_READ_EXTERNAL_STORAGE);
            } else {
                //
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults.length != 1 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                // request permission failed
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.confirm:
                selectImages(mSelectedDatas);
                break;
            case R.id.cancel:
                cancel();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void configRecyclerView() {
        mRecyclerView = findViewById(R.id.recycler_view);
        int widthDip =  ScreenSizeHelper.getScreenWidthDip(this);
        int col = (int) Math.floor(widthDip / 100);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, col));

        mAdapter = new ImageAdapter(getApplicationContext(), mDatas);
        mAdapter.setOnRecyclerViewItemListener(new OnRecyclerViewItemListener() {
            @Override
            public void onItemClick(View view, int position) {
            }

            @Override
            public void onItemLongClick(View view, int position) {
                view.setBackgroundColor(Color.DKGRAY);
                mSelectedViews.add(view);
                mSelectedDatas.add(mDatas.get(position));
            }
        });

        mRecyclerView.setAdapter(mAdapter);
    }

    // IAlbum interface
    @Override
    public void addPictures(List<String> pictures) {
        mDatas.addAll(pictures);
        mAdapter.setDatas(mDatas);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void selectImages(ArrayList<String> selectPictures) {
        mSelectedDatas = selectPictures;

        Intent intent = new Intent();
        intent.putStringArrayListExtra(SELECTED_IMAGES_KEY, selectPictures);
        setResult(0, intent);
        finish();
    }

    @Override
    public void cancel() {
        mSelectedDatas.clear();
        for (View view : mSelectedViews) {
            view.setBackgroundColor(Color.parseColor("#F5F5F5"));
        }
    }
}
