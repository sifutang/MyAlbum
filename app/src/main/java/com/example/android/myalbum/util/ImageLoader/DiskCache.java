package com.example.android.myalbum.util.ImageLoader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;

import com.example.android.myalbum.util.CloseUtils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by android on 17-8-17.
 */

public class DiskCache implements ImageCache{

    static String cacheDir = "sdcard/cache";

    @Override
    public Bitmap get(String url) {
        return BitmapFactory.decodeFile(cacheDir + url);
    }

    @Override
    public void put(String url, Bitmap bitmap) {

        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(cacheDir + url);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            CloseUtils.closeQuietly(fileOutputStream);
        }
    }
}
