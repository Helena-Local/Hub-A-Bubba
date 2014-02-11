/*
 * Copyright (c) 2014. This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License for Helena Local Inc. All rights reseved.
 */

package org.helenalocal.app;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.util.Linkify;
import android.widget.ImageView;
import android.widget.TextView;
import org.helenalocal.Helena_Local_Hub.R;
import org.helenalocal.base.Buyer;
import org.helenalocal.base.Hub;

import java.io.InputStream;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RestaurantDetailActivity extends Activity {

    private static final String LogTag = "RestaurantDetailActivity";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.restaurant_detail_activity);

        String buyerId = getIntent().getStringExtra(RestaurantTab.BUYER_ID_KEY);
        final Buyer b = Hub.buyerMap.get(buyerId);

        // restaurant name
        TextView tv = (TextView) findViewById(R.id.nameTextView);
        tv.setText(b.getName());

        // restaurant address
        tv = (TextView)findViewById(R.id.addressTextView);
        tv.setText(b.getLocation());

        // set clickable link for phone number
        tv = (TextView)findViewById(R.id.callTextView);
        Linkify.addLinks(tv, Pattern.compile(tv.getText().toString()), "tel:", null, new Linkify.TransformFilter() {
            @Override
            public String transformUrl(Matcher match, String url) {
                return b.getPhone();
            }
        });

        // setup clickable link for web address
        tv = (TextView)findViewById(R.id.urlTextView);
        Linkify.addLinks(tv, Pattern.compile(tv.getText().toString()), "http://", null, new Linkify.TransformFilter() {
            @Override
            public String transformUrl(Matcher match, String url) {
                String website = b.getWebsiteUrl();
                if (website.startsWith("http://") == true) {
                    website = website.substring("http://".length(), website.length());
                }

                return website;
            }
        });

        // load image
        ImageView iv = (ImageView)findViewById(R.id.restaurantImageView);
        new ImageAsyncTask(iv).execute(b.getPhotoUrl());
    }

    private class ImageAsyncTask extends AsyncTask<String, Void, Bitmap> {

        private ImageView _imageView;

        public ImageAsyncTask(ImageView view) {
            _imageView = view;
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
        }
    }
}