/*
 * Copyright (c) 2014. This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License for Helena Local Inc. All rights reseved.
 */

package org.helenalocal.app;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.URL;

public class AsyncImageLoader extends AsyncTask<String, Void, Bitmap> {

    private ImageView _imageView;
    private int _defaultImageId;

    public AsyncImageLoader(ImageView view, int defaultImageId) {
        _imageView = view;
        _defaultImageId = defaultImageId;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        String url = params[0];
        Bitmap bm = null;

        try {
            InputStream stream = new URL(url).openStream();
            bm = BitmapFactory.decodeStream(stream);
            stream.close();
        } catch (Exception e) {

        }

        return bm;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        if (bitmap != null) {
            _imageView.setImageBitmap(bitmap);
        }
        else {
            // set the default image
            _imageView.setImageResource(_defaultImageId);
        }
    }
}
