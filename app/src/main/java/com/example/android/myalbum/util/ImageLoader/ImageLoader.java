package com.example.android.myalbum.util.ImageLoader;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.example.android.myalbum.util.ImageHelper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by android on 17-8-17.
 */

public final class ImageLoader {

    // TODO: 17-8-17 使用Build模式进行重构, ImageLoader的配置选项需要提取出来
    private static final String TAG = "ImageLoader";

    private static ImageLoader sInstance;

    private volatile ImageCache mImageCache = new MemoryCache();

    // 线程池, 线程数量为CPU的个数
    ExecutorService mExecutorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private ImageLoader() { }
    public static ImageLoader getInstance() {
        if (sInstance == null) {
            synchronized (ImageLoader.class) {
                if (sInstance == null) {
                    sInstance = new ImageLoader();
                }
            }
        }

        return sInstance;
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

    // TODO: 17-8-17 需要重构, 从磁盘加载和网络下载 
    private Bitmap pullImage(String url) {
//        Bitmap bitmap = ImageHelper.getThumbnail(url, 100, 100);
//        Bitmap bitmap = ImageHelper.createImageThumbnail(url);
        Bitmap bitmap = ImageHelper.getImageThumbnail(url, 100, 100);
        return bitmap;
    }
}
