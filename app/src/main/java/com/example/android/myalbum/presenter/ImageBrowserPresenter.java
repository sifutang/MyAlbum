package com.example.android.myalbum.presenter;

import com.example.android.myalbum.Interface.IAlbumModel;
import com.example.android.myalbum.Interface.IAlbumPresenter;
import com.example.android.myalbum.Interface.IAlbumView;
import com.example.android.myalbum.model.ImageDataSource;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by android on 17-8-23.
 */

public class ImageBrowserPresenter extends BasePresenter<IAlbumView> implements IAlbumPresenter {

    private IAlbumView mAlbumView;
    private IAlbumModel mAlbumModel;

    public ImageBrowserPresenter(IAlbumView albumView, IAlbumModel albumModel) {
        this.mAlbumView = albumView;
        this.mAlbumModel = albumModel;
    }

    // IAlbumPresenter
    @Override
    public void loadAlbum() {
        updateUI();
    }

    private void updateUI() {
        final List<String> imagePathList = new ArrayList<>();

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
