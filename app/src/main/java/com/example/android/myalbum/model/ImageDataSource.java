package com.example.android.myalbum.model;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;

import com.example.android.myalbum.Interface.IAlbumModel;
import com.example.android.myalbum.util.App;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by android on 17-8-24.
 */

public class ImageDataSource implements IAlbumModel {

    @Override
    public Observable<List<String>> loadAlbum() {
        Uri[] imageUris = {
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                MediaStore.Images.Media.INTERNAL_CONTENT_URI
        };

        return Observable.from(imageUris)
                .subscribeOn(Schedulers.io())
                .map(new Func1<Uri, List<String>>() {
                    @Override
                    public List<String> call(Uri uri) {
                        return loadAlbumCore(uri);
                    }
                });
    }

    @NonNull
    private List<String> loadAlbumCore(Uri uri) {
        List<String> imagePathList = new ArrayList<>();
        Cursor cursor = MediaStore.Images.Media.query(
                App.getAppContext().getContentResolver(),
                uri,
                new String[] { MediaStore.Images.Media.DATA });
        cursor.moveToFirst();
        while (cursor.moveToNext()) {
            String imagePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            imagePathList.add(imagePath);
        }
        return imagePathList;
    }
}
