/*
 * Copyright (c) 2014. This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License for Helena Local Inc. All rights reseved.
 */

package org.montanafoodhub.app;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;
import org.montanafoodhub.app.utils.ImageCache;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;

public class AsyncImageLoader extends AsyncTask<String, Void, Bitmap> {

    private static final String LogTag = "AsyncImageLoader";
    private final WeakReference<ImageView> _imageViewReference;
    private ImageCache _imageCache;
    private int _defaultImageId;
    private boolean _isLoaded = false;

    public boolean isLoaded() {
        return _isLoaded;
    }

    public AsyncImageLoader(ImageView view, int defaultImageId, ImageCache imageCache) {
        _imageViewReference = new WeakReference<ImageView>(view);
        _defaultImageId = defaultImageId;
        _imageCache = imageCache;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        String url = params[0];
        Bitmap bm = null;

        try {
            InputStream stream = new URL(url).openStream();
            bm = BitmapFactory.decodeStream(stream);
            stream.close();

            _imageCache.addImageToCache(url, bm);

            _isLoaded = true;
        } catch (Exception e) {

        }

        return bm;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);

        if (isCancelled()) {
            _isLoaded = false;
        }
        else {
            if (_imageViewReference.get() != null) {

                ImageView imageView = _imageViewReference.get();

                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                }
                else {
                    // set the default image
                    imageView.setImageResource(_defaultImageId);
                }
            }
        }
    }
}
