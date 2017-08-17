package com.example.android.myalbum.activity;

import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.Toast;

import com.example.android.myalbum.adapter.ImageAdapter;
import com.example.android.myalbum.db.ImageDataSource;
import com.example.android.myalbum.util.OnRecyclerViewItemListener;
import com.example.android.myalbum.R;
import com.example.android.myalbum.model.ImageInfo;
import com.example.android.myalbum.util.ImageHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by android on 17-8-17.
 */

public class ImageBrowserActivity extends AppCompatActivity {

    private static final String TAG = "ImageBrowserActivity";

    private RecyclerView mImageRecyclerView;
    private List<String> mDatas;
    private ArrayList<String> mSelectedDatas;
    private ImageAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_browser);
        addBackBarItem();

        List<ImageInfo> list = new ArrayList<>();
        ImageDataSource imageDataSource = new ImageDataSource(this);
        imageDataSource.getImagesFromAlbum(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, list);
        imageDataSource.getImagesFromAlbum(MediaStore.Images.Media.INTERNAL_CONTENT_URI, list);

        mSelectedDatas = new ArrayList<>();
        initImageDatas(list);
        configRecyclerView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
            case R.id.confirm:
                Intent intent = new Intent();
                intent.putStringArrayListExtra("selected_images", mSelectedDatas);
                Log.d(TAG, "onOptionsItemSelected: " + mSelectedDatas.toString());
                setResult(0, intent);
                finish();
                break;
            case R.id.cancel:
                break;
        }
        return super.onOptionsItemSelected(item);
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

    private void configRecyclerView() {
        mImageRecyclerView = findViewById(R.id.recycler_view);
        mImageRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL));
        mAdapter = new ImageAdapter(this, mDatas);
        mImageRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnRecyclerViewItemListener(new OnRecyclerViewItemListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(ImageBrowserActivity.this, "click", Toast.LENGTH_SHORT).show();
                mSelectedDatas.add(mDatas.get(position));
            }

            @Override
            public void onItemLongClick(View view, int position) {
                Toast.makeText(ImageBrowserActivity.this, "long click", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void initImageDatas(final List<ImageInfo> list) {
        mDatas = new ArrayList<>(list.size());

        for (int i = 0; i < list.size(); i++) {
            ImageInfo model = list.get(i);
            String imagePath = model.getPath();
            mDatas.add(imagePath);
        }
    }

}
