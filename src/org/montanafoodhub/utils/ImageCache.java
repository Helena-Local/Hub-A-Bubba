/*
 * Copyright (c) 2014. This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License for Helena Local Inc. All rights reseved.
 */

package org.montanafoodhub.utils;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.ImageView;
import org.montanafoodhub.app.AsyncImageLoader;

public class ImageCache {

    private final static String LogTag = "ImageCache";

    private LruCache<String, Bitmap> _cache;

    public ImageCache() {

        // set the cache to be 1/8 of the total memory (kilobytes)
        int maxMemory = (int)(Runtime.getRuntime().maxMemory() / 1024);
        int cacheSize = maxMemory / 8;

        Log.w(LogTag, "Cache Size: " + cacheSize);

        _cache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // todo bitmap.getAllocationByteCount()
                return (bitmap.getRowBytes() * bitmap.getHeight()) / 1024;
            }
        };
    }

    public void loadImage(ImageView imageView, String url, int defaultId) {

        // The imageView may be being recycled. If there is already a loader associated with this imageView then cancel it.
        AsyncImageLoader asyncLoader = getAsyncLoader(imageView);
        if (asyncLoader != null) {
            asyncLoader.cancel(true);
        }

        // if the image is already in the cache load it, otherwise go get it.
        Bitmap bm = getImageFromCache(url);
        if (bm != null) {
            imageView.setImageBitmap(bm);
        }
        else {
            imageView.setImageBitmap(null);
            asyncLoader = new AsyncImageLoader(imageView, defaultId, this);
            imageView.setTag(asyncLoader);
            asyncLoader.execute(url);
        }
    }

    public void addImageToCache(String key, Bitmap bitmap) {
        synchronized (_cache) {
            if (getImageFromCache(key) == null) {
                Log.w(LogTag, "Adding image to cache: " + key);
                _cache.put(key, bitmap);
            }
        }
    }

    public synchronized Bitmap getImageFromCache(String key) {
        Bitmap bm;

        synchronized (_cache) {
            bm = _cache.get(key);
        }

        return bm;
    }

    private AsyncImageLoader getAsyncLoader(ImageView imageView) {
        AsyncImageLoader loader = null;
        Object tag = imageView.getTag();
        if (tag instanceof AsyncImageLoader) {
            loader = (AsyncImageLoader)tag;
        }

        return loader;
    }
}
