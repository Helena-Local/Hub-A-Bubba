/*
 * Copyright (c) 2014. This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License for Helena Local Inc. All rights reseved.
 */

package org.helenalocal.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import org.helenalocal.Helena_Local_Hub.R;
import org.helenalocal.base.*;

import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

public class RestaurantDetailActivity extends Activity {

    private static final String LogTag = "RestaurantDetailActivity";

    private Buyer _buyer;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.restaurant_detail_activity);

        String buyerId = getIntent().getStringExtra(RestaurantTab.BUYER_ID_KEY);
        _buyer = Hub.buyerMap.get(buyerId);


        // load image
        ImageView iv = (ImageView)findViewById(R.id.restaurantImageView);
        new ImageAsyncTask(iv).execute(_buyer.getPhotoUrl());

        // restaurant name
        TextView tv = (TextView) findViewById(R.id.nameTextView);
        tv.setText(_buyer.getName());

        // restaurant address
        tv = (TextView)findViewById(R.id.addressTextView);
        tv.setText(_buyer.getLocation());

        // quote
        tv = (TextView)findViewById(R.id.quoteTextView);
        if (_buyer.getQuote().isEmpty() == false) {
            tv.setText(String.format("\"%s\"", _buyer.getQuote()));
        }
        else {
            tv.setVisibility(View.GONE);
        }

        loadCertifications();
        loadProducts();
    }

    private void loadCertifications() {

        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.certificationLayout);
        linearLayout.removeAllViews();

        CertItemClickListener clickListener = new CertItemClickListener();

        for (Certification cert : _buyer.getCertifications()) {
            RelativeLayout relativeLayout = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.certification, null);
            relativeLayout.setOnClickListener(clickListener);
            relativeLayout.setTag(cert);

            ImageView imageView = (ImageView) relativeLayout.findViewById(R.id.imageView);
            new ImageAsyncTask(imageView).execute(cert.getIconUrl());

            TextView textView = (TextView)relativeLayout.findViewById(R.id.textView);
            textView.setText(cert.getDisplayName());

            linearLayout.addView(relativeLayout);
        }
    }

    private void loadProducts() {

        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.productsLayout);
        linearLayout.removeAllViews();

        ProductItemClickListener clickListener = new ProductItemClickListener();

        Log.w(LogTag, "Buyer ID: " + _buyer.getBID());

        for (Map.Entry entry : Hub.orderMap.entrySet())
        {
            Order order1 = (Order)entry.getValue();
            Item item1 = Hub.itemMap.get(order1.getItemID());

            Log.w(LogTag, String.format("Order: ID - %s Item - %s BuyerId - %s", order1.getOrderID(), item1.getProductDesc(), order1.getBuyerID()));

            if (entry.getKey().equals(_buyer.getBID()))
            {

                Order order = (Order)entry.getValue();
                Item item = Hub.itemMap.get(order.getItemID());

                RelativeLayout relativeLayout = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.certification, null);
                relativeLayout.setOnClickListener(clickListener);

                TextView textView = (TextView)relativeLayout.findViewById(R.id.textView);
                textView.setText(item.getProductDesc());

                linearLayout.addView(relativeLayout);
            }
        }
    }

    public void onClickCall(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + _buyer.getPhone()));
        startActivity(intent);
    }

    public void onClickMap(View view) {
        // todo - verify that the maps application is installed (apparently it is not on a kindle)

        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            String address = String.format("%s %s", _buyer.getName(), _buyer.getLocation());
            String data = String.format("geo:0,0?q=%s", URLEncoder.encode(address, "UTF-8"));

            intent.setData(Uri.parse(data));
            startActivity(intent);
        } catch (Exception e) {
        }
    }

    public void onClickUrl(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(_buyer.getWebsiteUrl()));
        startActivity(intent);
    }

    private class CertItemClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            Certification cert = (Certification)v.getTag();

            if (cert.getWebsiteUrl().isEmpty() == false) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(cert.getWebsiteUrl()));
                startActivity(intent);
            }
        }
    }

    private class ProductItemClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Log.w(LogTag, "ProductItemClickListener: onClick");
        }
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
            else {
                // set the default image
                _imageView.setImageResource(R.drawable.default_restaurant);
            }
        }
    }
}