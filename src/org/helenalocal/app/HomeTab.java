/*
 * Copyright (c) 2014. This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License for Helena Local Inc. All rights reseved.
 */

package org.helenalocal.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import org.helenalocal.Helena_Local_Hub.R;

public class HomeTab extends Fragment {

    private static final String Tag = "HomeTab";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.home_tab, container, false);
        // String url = Hub.adArr.get(1).getImageUrl();
        String url = "https://sites.google.com/site/helenalocalcsa2/ad1.png";
        // image
        ImageView imageView = (ImageView) v.findViewById(R.id.adImageView);
        // imageView.setImageResource(resId);
        // new AsyncImageLoader(imageView, R.drawable.default_ad).execute(url);

        //***********************
        // Example from here:
        // http://developer.android.com/training/displaying-bitmaps/display-bitmap.html
        // http://android-er.blogspot.com/2013/11/android-example-imageswitcher.html
        //***********************
        return v;
    }
}