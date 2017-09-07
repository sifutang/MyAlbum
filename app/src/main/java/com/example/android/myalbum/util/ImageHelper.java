package com.example.android.myalbum.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
/**
 * Created by android on 17-8-17.
 */

public class ImageHelper {

    private static final String TAG = "ImageHelper";

    public static Bitmap getThumbnail(String path, int width, int heigth) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        int originWidth = options.outWidth;
        int originHeight = options.outHeight;

        int inSampleSize = 1;
        if (originHeight > heigth || originWidth > width) {
            float heightScale = originHeight / heigth;
            float widthScale = originWidth / width;

            inSampleSize = Math.round(heightScale > widthScale ? heightScale: widthScale);
        }

        options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, options);
    }

    public static Bitmap getImageThumbnail(String filePath, int width, int height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        int originWidth = options.outWidth;
        int originHeight = options.outHeight;
        int scaleWidth = originWidth / width;
        int scaleHeight = originHeight / height;

        int scale = scaleWidth < scaleHeight ? scaleWidth : scaleHeight;
        if (scale <= 1) {
            scale = 1;
        }

        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;

        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);

        return bitmap;
    }

    public static Bitmap createImageThumbnail(String filePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        options.inSampleSize = computeSampleSize(options, -1, 128 * 128);
        options.inJustDecodeBounds = false;

        return  BitmapFactory.decodeFile(filePath, options);
    }

    private static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);
        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }

        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        float width = options.outWidth;
        float height = options.outHeight;

        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(width * height / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(width / minSideLength), Math.floor(height / minSideLength));

        if (upperBound < lowerBound) {
            return lowerBound;
        }

        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }


}
