package com.example.android.myalbum.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.android.myalbum.IImageBrowserView;
import com.example.android.myalbum.ImageBrowserPresenter;
import com.example.android.myalbum.adapter.ImageAdapter;
import com.example.android.myalbum.util.OnRecyclerViewItemListener;
import com.example.android.myalbum.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by android on 17-8-17.
 */

public class ImageBrowserActivity extends AppCompatActivity implements IImageBrowserView {

    private static final String TAG = "ImageBrowserActivity";

    private RecyclerView mImageRecyclerView;

    private ImageBrowserPresenter mImageBrowserPresenter = new ImageBrowserPresenter(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_browser);

        checkReadExternalStoragePermission();

        configRecyclerView();
    }

    private void checkReadExternalStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                Log.d(TAG, "getImagesFromAlbum: READ permission is not granted");
                requestPermissions(new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE }, 0);
            } else {

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 0:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "onRequestPermissionsResult: success");
                } else {
                    Log.d(TAG, "onRequestPermissionsResult: no permission");
                }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.confirm:
                mImageBrowserPresenter.selectImages();
                break;
            case R.id.cancel:
                mImageBrowserPresenter.cancle();
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
        mImageRecyclerView = findViewById(R.id.recycler_view);
        mImageRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        mImageBrowserPresenter.configAdapter(mImageRecyclerView);
    }
}
