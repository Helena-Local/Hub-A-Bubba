/*
 * Copyright (c) 2014. This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License for Helena Local Inc. All rights reseved.
 */

package org.helenalocal.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import org.helenalocal.Helena_Local_Hub.R;
import org.helenalocal.base.Ad;
import org.helenalocal.base.get.AdHub;
import org.helenalocal.utils.ImageCache;

import java.util.Random;

public class HomeTab extends Fragment {

    private static final String Tag = "HomeTab";
    private static Random random = new Random();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.home_tab, container, false);
        if (AdHub.adArr.size() > 0) {
            // choose ad here!
            final Ad ad = AdHub.adArr.get(random.nextInt(AdHub.adArr.size()));
            // pull from cache and display
            ImageCache imageCache = ((HubApplication) getActivity().getApplication()).getImageCache();
            ImageView imageView = (ImageView) v.findViewById(R.id.adImageView);
            imageCache.loadImage(imageView, ad.getImageUrl(), R.drawable.default_ad);
            if (ad.getImageUrl().isEmpty() == true) {
                imageView.setVisibility(View.GONE);
            } else {
                imageCache.loadImage(imageView, ad.getImageUrl(), R.drawable.default_ad);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (ad.getBID().isEmpty()) {
                            Log.w(Tag, "ad not assoc w/ buyer");
                        } else {
                            Log.w(Tag, "ad assoc w/ buyer");
                            Intent intent = new Intent(getActivity(), RestaurantDetailActivity.class);
                            intent.putExtra(RestaurantDetailActivity.EXTRA_BUYER_ID, ad.getBID());
                            startActivity(intent);
                        }
                    }
                });
            }
        }
        return v;
    }
}