package com.example.android.myalbum.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

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

    private static final String TAG = "ImageBrowserActivity";
    public static final String SELECTED_IMAGES_KEY = "selected_images";

    private RecyclerView mImageRecyclerView;
    private List<String> mDatas;
    private ArrayList<String> mSelectedDatas;
    private ImageAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_browser);

        addBackBarItem();
        initImageDatas();
        configRecyclerView();

        mSelectedDatas = new ArrayList<>();
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
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void sendSelectedImageIntent() {
        Intent intent = new Intent();
        intent.putStringArrayListExtra(SELECTED_IMAGES_KEY, mSelectedDatas);
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

    private void initImageDatas() {
        List<ImageInfo> list = getAllImagesFromDevice();
        mDatas = new ArrayList<>(list.size());

        for (int i = 0; i < list.size(); i++) {
            ImageInfo model = list.get(i);
            String imagePath = model.getPath();
            mDatas.add(imagePath);
        }
    }

    private List<ImageInfo> getAllImagesFromDevice() {
        List<ImageInfo> list = new ArrayList<>();
        ImageDataSource imageDataSource = new ImageDataSource(this);
        imageDataSource.getImagesFromAlbum(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, list);
        imageDataSource.getImagesFromAlbum(MediaStore.Images.Media.INTERNAL_CONTENT_URI, list);
        return list;
    }

    private void configRecyclerView() {
        mImageRecyclerView = findViewById(R.id.recycler_view);
        mImageRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        mAdapter = new ImageAdapter(this, mDatas);
        mImageRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnRecyclerViewItemListener(new OnRecyclerViewItemListener() {
            @Override
            public void onItemClick(View view, int position) {
                mSelectedDatas.add(mDatas.get(position));
            }

            @Override
            public void onItemLongClick(View view, int position) {

                // TODO: 17-8-17 长按的时候, 选择图片
            }
        });
    }

}
