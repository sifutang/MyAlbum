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
    private IAlbumModel mAlbumModel;

    public ImageBrowserPresenter(IAlbumView albumView) {
        this.mAlbumView = albumView;
    }

    // IAlbumPresenter
    @Override
    public void loadAlbum() {
        updateUIViaRxjava();
    }

    private void updateUIViaRxjava() {
        final List<String> imagePathList = new ArrayList<>();

        mAlbumModel= new ImageDataSource();
        Observable<List<String>> observable = mAlbumModel.loadAlbum();
        observable.subscribe(new Subscriber<List<String>>() {
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
}
