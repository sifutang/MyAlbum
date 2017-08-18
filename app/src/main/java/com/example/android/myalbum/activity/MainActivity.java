package com.example.android.myalbum.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.android.myalbum.adapter.ImageAdapter;
import com.example.android.myalbum.db.ImageDataSource;
import com.example.android.myalbum.R;
import com.example.android.myalbum.model.ImageInfo;
import com.example.android.myalbum.util.ImageHelper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int SELECT_IMAGE_CODE = 1000;

    private RecyclerView mRecyclerView;
    private ImageAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initButton(R.id.select_image_btn);
        initRecyclerView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case SELECT_IMAGE_CODE:
                if (data != null) {
                    ArrayList<String> selectedIamges = data.getStringArrayListExtra("selected_images");
                    mAdapter = new ImageAdapter(this, selectedIamges);
                    mRecyclerView.setAdapter(mAdapter);
                }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initRecyclerView() {
        mRecyclerView = findViewById(R.id.select_image_recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
    }

    private void initButton(int resource_id) {
        Button selectImageBtn = findViewById(R.id.select_image_btn);
        selectImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ImageBrowserActivity.class);
                startActivityForResult(intent, SELECT_IMAGE_CODE);
            }
        });
    }
}
