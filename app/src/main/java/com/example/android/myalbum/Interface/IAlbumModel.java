package com.example.android.myalbum.Interface;

import java.util.List;

import rx.Observable;

/**
 * Created by android on 17-8-28.
 */

public interface IAlbumModel {

    Observable<List<String>> loadAlbum();
}
