package com.example.android.myalbum;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.util.List;

/**
 * Created by android on 17-8-16.
 */

public class ImageDataSource {

    private static final String TAG = "ImageDataSource";

    private Context context;

    public ImageDataSource(Context context) {
        this.context = context;
    }

    public void getImagesFromAlbum(Uri uri, List<ImageInfo> list) {
        Cursor cursor = MediaStore.Images.Media.query(
                context.getContentResolver(),
                uri,
                new String[] {
                        MediaStore.Images.Media.TITLE,
                        MediaStore.Images.Media.DISPLAY_NAME,
                        MediaStore.Images.Media.SIZE,
                        MediaStore.Images.Media.DATA,
                        MediaStore.Images.Media.DESCRIPTION
                }
        );

        if (cursor != null) {
            while (cursor.moveToNext()) {
                ImageInfo model = new ImageInfo();
                model.setTitle(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.TITLE)));
                model.setPath(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA)));
                model.setSize(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.SIZE)));
                model.setDesc(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DESCRIPTION)));
                model.setDispalyName(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)));
                list.add(model);

                Log.d(TAG, "getImagesFromAlbum: " + model);
            }
        }
    }
}
