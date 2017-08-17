package com.example.android.myalbum.util.ImageLoader;

import android.graphics.Bitmap;
import android.widget.ImageView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by android on 17-8-17.
 */

public class ImageLoader {

    ImageCache mImageCache = new MemoryCache();

    // 线程池, 线程数量为CPU的个数
    ExecutorService mExecutorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public ImageLoader(ImageCache mImageCache) {
        this.mImageCache = mImageCache;
    }

    public void setImageCache(ImageCache cache) {
        this.mImageCache = cache;
    }

    public void displayImage(final String url, final ImageView imageView) {
        final Bitmap bitmap = mImageCache.get(url);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
            return;
        }

        submitLoadRequest(url, imageView);
    }

    private void submitLoadRequest(final String url, final ImageView imageView) {
        imageView.setTag(url);
        mExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = pullImage(url);
                if (imageView.getTag().equals(url)) {
                    imageView.setImageBitmap(bitmap);
                }

                mImageCache.put(url, bitmap);
            }
        });
    }

    private Bitmap pullImage(String url) {
        // TODO: 17-8-17 bitmap from disk
        return null;
    }
}
