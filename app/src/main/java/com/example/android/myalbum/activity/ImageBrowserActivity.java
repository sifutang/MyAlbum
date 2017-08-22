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

import com.example.android.myalbum.adapter.ImageAdapter;
import com.example.android.myalbum.util.OnRecyclerViewItemListener;
import com.example.android.myalbum.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by android on 17-8-17.
 */

public class ImageBrowserActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "ImageBrowserActivity";

    private static final String SELECTED_IMAGES_KEY = "selected_images";
    public static final int IMAGE_LOADER_EXTERNAL_ID = 0;
    public static final int IMAGE_LOADER_INTERNAL_ID = 1;

    private RecyclerView mImageRecyclerView;

    private List<String> mImagePathList;
    private ArrayList<String> mSelectedImagePathList;
    private ImageAdapter mAdapter;
    private CursorLoader mCursorLoaderExternal;
    private CursorLoader mCursorLoaderInternal;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_browser);

        checkReadExternalStoragePermission();

        configRecyclerView();

        mSelectedImagePathList = new ArrayList<>();
        mImagePathList = new ArrayList<>();

        LoaderManager manager = getSupportLoaderManager();
        manager.initLoader(IMAGE_LOADER_EXTERNAL_ID, null, this);
        manager.initLoader(IMAGE_LOADER_INTERNAL_ID, null, this);
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
                mAdapter.notifyDataSetChanged();
        }
    }

    private void configRecyclerView() {
        mImageRecyclerView = findViewById(R.id.recycler_view);
        mImageRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case IMAGE_LOADER_EXTERNAL_ID:
                mCursorLoaderExternal = new CursorLoader(getApplicationContext(),
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        null,
                        null,
                        null,
                        MediaStore.Images.Media.DEFAULT_SORT_ORDER);
                return mCursorLoaderExternal;

            case IMAGE_LOADER_INTERNAL_ID:
                mCursorLoaderInternal = new CursorLoader(getApplicationContext(),
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
            mImagePathList.add(imagePath);
            Log.d(TAG, "getImagesFromAlbum: " + imagePath);
        }

        initAdapter();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d(TAG, "onLoaderReset: ");
    }
}
