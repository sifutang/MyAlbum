package com.example.android.myalbum.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.android.myalbum.adapter.ImageAdapter;
import com.example.android.myalbum.db.ImageDataSource;
import com.example.android.myalbum.util.OnRecyclerViewItemListener;
import com.example.android.myalbum.R;
import com.example.android.myalbum.model.ImageInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by android on 17-8-17.
 */

public class ImageBrowserActivity extends AppCompatActivity {

    public static final String SELECTED_IMAGES_KEY = "selected_images";

    private static final String TAG = "ImageBrowserActivity";

    private RecyclerView mImageRecyclerView;
    private List<String> mImagePathList;
    private ArrayList<String> mSelectedImagePathList;
    private ImageAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_browser);
        addBackBarItem();

        checkReadExternalStoragePermission();

        initImageDatasViaAsync();
        configRecyclerView();

        mSelectedImagePathList = new ArrayList<>();
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
            case android.R.id.home:
                this.finish();
                break;
            case R.id.confirm:
                sendSelectedImageIntent();
                finish();
                break;
            case R.id.cancel:
                mSelectedImagePathList.clear();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void sendSelectedImageIntent() {
        Intent intent = new Intent();
        intent.putStringArrayListExtra(SELECTED_IMAGES_KEY, mSelectedImagePathList);
        setResult(0, intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void addBackBarItem() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initImageDatasViaAsync() {
        mImagePathList = new ArrayList<>();

        ImageDataSource imageDataSource = new ImageDataSource(this);
        imageDataSource.getImagesFromAlbum(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new ImageDataSource.FetchDataHandler() {
            @Override
            public void onFetchDataSuccessHandler(final List<ImageInfo> list) {

                initImageDatasCore(list);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "onFetchDataSuccessHandler: current thread:" + Thread.currentThread().getName());
                        initAdapter();
                    }
                });
            }
        });

        imageDataSource.getImagesFromAlbum(MediaStore.Images.Media.INTERNAL_CONTENT_URI, new ImageDataSource.FetchDataHandler() {
            @Override
            public void onFetchDataSuccessHandler(List<ImageInfo> list) {
                initImageDatasCore(list);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "onFetchDataSuccessHandler: current thread:" + Thread.currentThread().getName());
                        initAdapter();
                    }
                });
            }
        });
    }

    private void initAdapter() {
        if (mAdapter == null) {
            mAdapter = new ImageAdapter(ImageBrowserActivity.this, mImagePathList);
            mImageRecyclerView.setAdapter(mAdapter);
            mAdapter.setOnRecyclerViewItemListener(new OnRecyclerViewItemListener() {
                @Override
                public void onItemClick(View view, int position) {

                }

                @Override
                public void onItemLongClick(View view, int position) {
                    view.setBackgroundColor(Color.DKGRAY);
                    mSelectedImagePathList.add(mImagePathList.get(position));
                }
            });
        } else {
//            if (mImageRecyclerView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE || mImageRecyclerView.isComputingLayout() == false) {
                mAdapter.notifyDataSetChanged();
//            }
        }
    }

    private void initImageDatasCore(List<ImageInfo> list) {
        for (int i = 0; i < list.size(); i++) {
            ImageInfo model = list.get(i);
            String imagePath = model.getPath();
            mImagePathList.add(imagePath);
        }
    }

    private void configRecyclerView() {
        mImageRecyclerView = findViewById(R.id.recycler_view);
        mImageRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
    }

}
