package com.example.android.myalbum.util.ImageLoader;

import android.graphics.Bitmap;
/**
 * Created by android on 17-8-17.
 */

public interface ImageCache {

    Bitmap get(String url);

    void put(String url, Bitmap bitmap);
}
