/*
 * Copyright (c) 2014. This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License for Helena Local Inc. All rights reseved.
 */

package org.montanafoodhub.app.event;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import org.montanafoodhub.Helena_Hub.R;
import org.montanafoodhub.app.FragmentBase;
import org.montanafoodhub.app.HubApplication;
import org.montanafoodhub.app.restaurant.RestaurantDetailActivity;
import org.montanafoodhub.app.utils.ImageCache;
import org.montanafoodhub.base.Ad;
import org.montanafoodhub.base.HubInit;
import org.montanafoodhub.base.get.AdHub;

import java.util.Random;

public class EventFragment extends FragmentBase {

    private static final String Tag = "EventFragment";
    private static Random random = new Random();

    @Override
    public int getTitleId() {
        return R.string.event_fragment_title;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeReceiver(HubInit.HubType.ITEM_HUB);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.events_fragment, container, false);

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

        TextView textView = (TextView) v.findViewById(R.id.primaryMsgTextView);
        textView.setText(R.string.event_fragment_welcome);

        textView = (TextView) v.findViewById(R.id.secondaryMsgTextView);
        textView.setVisibility(View.GONE);

        View view = v.findViewById(R.id.dismissContainer);
        view.setVisibility(View.GONE);

        return v;
    }
}