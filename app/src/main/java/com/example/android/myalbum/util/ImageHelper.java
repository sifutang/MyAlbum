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

        options.inSampleSize = originWidth / width;
        options.inSampleSize = options.inSampleSize > originHeight / heigth ? options.inSampleSize : originHeight / heigth;
        options.inJustDecodeBounds = false;
        Bitmap decodeFile = BitmapFactory.decodeFile(path, options);
        Bitmap result = Bitmap.createScaledBitmap(decodeFile, width, heigth, false);
        decodeFile.recycle();

        return result;
    }
}
