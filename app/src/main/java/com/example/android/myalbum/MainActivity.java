package com.example.android.myalbum;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.example.android.myalbum.util.ImageHelper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private RecyclerView mRecyclerView;
    private ImageAdapter mAdapter;
    private List<Bitmap> mDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        List<ImageInfo> list = new ArrayList<ImageInfo>();
        ImageDataSource imageDataSource = new ImageDataSource(this);
        imageDataSource.getImagesFromAlbum(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, list);
        imageDataSource.getImagesFromAlbum(MediaStore.Images.Media.INTERNAL_CONTENT_URI, list);

        Log.d(TAG, "onCreate: list.size() = " + list.size());

        initImageDatas(list);
        initButton(R.id.select_image_btn);
        initRecyclerView();
    }

    private void initRecyclerView() {
        mRecyclerView = findViewById(R.id.select_image_recycler);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mAdapter = new ImageAdapter(this, mDatas);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initImageDatas(List<ImageInfo> list) {
        mDatas = new ArrayList<>(9);

        for (int i = 0; i < 9; i++) {
            ImageInfo model = list.get(i);
            String imagePath = model.getPath();

            Bitmap bitmap = ImageHelper.getThumbnail(imagePath, 150, 150);
            mDatas.add(bitmap);
        }
    }

    private void initButton(int resource_id) {
        Button selectImageBtn = findViewById(R.id.select_image_btn);
        selectImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ImageBrowserActivity.class);
                startActivity(intent);
            }
        });
    }
}
