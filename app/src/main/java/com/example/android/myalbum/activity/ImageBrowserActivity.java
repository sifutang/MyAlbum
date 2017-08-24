package com.example.android.myalbum.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.myalbum.presenter.ImageBrowserPresenter;
import com.example.android.myalbum.R;
import com.example.android.myalbum.util.ScreenSizeHelper;

/**
 * Created by android on 17-8-17.
 */

public class ImageBrowserActivity extends AppCompatActivity {

    public static final int REQUEST_READ_EXTERNAL_STORAGE = 0;

    private static final String TAG = "ImageBrowserActivity";
    private RecyclerView mImageRecyclerView;
    private ImageBrowserPresenter mImageBrowserPresenter;

    public RecyclerView getImageRecyclerView() {
        return mImageRecyclerView;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_browser);

        checkReadExternalStoragePermission();
        configRecyclerView();
        mImageBrowserPresenter = new ImageBrowserPresenter(this);
        mImageBrowserPresenter.updateUI();

        Log.d(TAG, "onCreate: width " + ScreenSizeHelper.getScreenWidthDip(this));
        Log.d(TAG, "onCreate: height " + ScreenSizeHelper.getScreenHeigthDip(this));

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
    }
}
