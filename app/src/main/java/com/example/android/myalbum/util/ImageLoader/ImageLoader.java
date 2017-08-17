package com.example.android.myalbum.util.ImageLoader;

import android.graphics.Bitmap;
import android.util.LruCache;
import android.widget.ImageView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by android on 17-8-17.
 */

public class ImageLoader {

    LruCache<String, Bitmap> mImageCache;

    private Bitmap bitmap;

    // 线程池, 线程数量为CPU的个数
    ExecutorService mExecutorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public ImageLoader(Bitmap bitmap) {
        this.bitmap = bitmap;
        initImageCache();
    }

    private void initImageCache() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 4;

        mImageCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
            }
        };
    }

    public void displayImage(final String url, final ImageView imageView) {
        imageView.setTag(url);
        mExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                if (imageView.getTag().equals(url)) {
                    imageView.setImageBitmap(bitmap);
                }

                mImageCache.put(url, bitmap);
            }
        });
    }
}
