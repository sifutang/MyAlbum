package com.example.android.myalbum.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
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
    private List<Bitmap> mDatas;
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

        initImageDatas(list);

        mImageRecyclerView = findViewById(R.id.recycler_view);
        mImageRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL));
        mAdapter = new ImageAdapter(this, mDatas);
        mImageRecyclerView.setAdapter(mAdapter);

        mAdapter.setmOnRecyclerViewItemListener(new OnRecyclerViewItemListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(ImageBrowserActivity.this, "click", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, int position) {
                Toast.makeText(ImageBrowserActivity.this, "long click", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addBackBarItem() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initImageDatas(List<ImageInfo> list) {
        mDatas = new ArrayList<>(list.size());

        for (int i = 0; i < 20; i++) {
            ImageInfo model = list.get(i);
            String imagePath = model.getPath();

            Bitmap bitmap = ImageHelper.getThumbnail(imagePath, 100, 100);
            mDatas.add(bitmap);
        }
    }
}
