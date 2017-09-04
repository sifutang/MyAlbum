package com.example.android.myalbum.Interface;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by android on 17-8-28.
 */

public interface IAlbumView {

    void addPictures(List<String> pictures);

    void selectImages(ArrayList<String> selectPictures);

    void cancel();
}
