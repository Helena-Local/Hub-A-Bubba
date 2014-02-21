/*
 * Copyright (c) 2014. This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License for Helena Local Inc. All rights reseved.
 */

package org.helenalocal.app;

import android.app.Application;
import org.helenalocal.utils.ImageCache;

public class HubApplication extends Application {

    private ImageCache _imageCache;

    public ImageCache getImageCache() {
        return _imageCache;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        _imageCache = new ImageCache();
    }
}
