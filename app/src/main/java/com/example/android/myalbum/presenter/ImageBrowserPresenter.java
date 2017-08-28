package com.example.android.myalbum.presenter;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.android.myalbum.Interface.IAlbumModel;
import com.example.android.myalbum.Interface.IAlbumPresenter;
import com.example.android.myalbum.Interface.IAlbumView;
import com.example.android.myalbum.activity.ImageBrowserView;
import com.example.android.myalbum.model.ImageDataSource;
import com.example.android.myalbum.util.FetchDataListener;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by android on 17-8-23.
 */

public class ImageBrowserPresenter implements IAlbumPresenter {

    private IAlbumView mAlbumView;
//    private IAlbumModel mAlbumModel;

    public ImageBrowserPresenter(IAlbumView albumView) {
        this.mAlbumView = albumView;
    }

    private void updateUIViaLoaderManager() {
        ImageDataSource dataSource = new ImageDataSource(ImageBrowserView.getLoderManager(), ImageBrowserView.getAppContext());
        dataSource.loadAlbumFromLocal();
        dataSource.setListener(new FetchDataListener() {
            @Override
            public void fetchDataSourceSuccess(List<String> list) {
                mAlbumView.addPictures(list);
            }
        });
    }

    private void updateUIViaRxjava() {
        Uri[] imageUris = {
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                MediaStore.Images.Media.INTERNAL_CONTENT_URI
        };

        final List<String> imagePathList = new ArrayList<>();

        Observable.from(imageUris)
                .subscribeOn(Schedulers.io())
                .map(new Func1<Uri, List<String>>() {
                    @Override
                    public List<String> call(Uri uri) {
                        List<String> imagePathList = new ArrayList<>();
                        Cursor cursor = MediaStore.Images.Media.query(
                                ImageBrowserView.getAppContext().getContentResolver(),
                                uri,
                                new String[] { MediaStore.Images.Media.DATA });
                        cursor.moveToFirst();
                        while (cursor.moveToNext()) {
                            String imagePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                            imagePathList.add(imagePath);
                        }
                        return imagePathList;
                    }
                })
                .subscribe(new Subscriber<List<String>>() {
                    @Override
                    public void onCompleted() {
                        mAlbumView.addPictures(imagePathList);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(List<String> list) {
                        imagePathList.addAll(list);
                    }
                });
    }

    // IAlbumPresenter
    @Override
    public void loadAlbum() {

//        updateUIViaRxjava();

        updateUIViaLoaderManager();
    }
}
