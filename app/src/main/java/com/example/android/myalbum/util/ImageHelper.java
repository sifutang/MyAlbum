package com.example.android.myalbum.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.WindowManager;

/**
 * Created by android on 17-8-17.
 */

public class ImageHelper {

    public static Bitmap getThumbnail(String path, int width, int heigth) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        int originWidth = options.outWidth;
        int originHeight = options.outHeight;

        // Figure out how much to scale down by.
        int inSampleSize = 1;
        if (originHeight > heigth || originWidth > width) {
            float heightScale = originHeight / heigth;
            float widthScale = originWidth / width;

            inSampleSize = Math.round(heightScale > widthScale ? heightScale: widthScale);
        }

        options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;

        // Read in and create final bitmap
        return BitmapFactory.decodeFile(path, options);

//        Bitmap decodeFile = BitmapFactory.decodeFile(path, options);
//        Bitmap result = Bitmap.createScaledBitmap(decodeFile, width, heigth, false);
//        decodeFile.recycle();
//
//        return result;
    }
}
