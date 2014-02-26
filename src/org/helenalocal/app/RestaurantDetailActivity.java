/*
 * Copyright (c) 2014. This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License for Helena Local Inc. All rights reseved.
 */

package org.helenalocal.app;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import org.helenalocal.Helena_Local_Hub.R;
import org.helenalocal.base.*;
import org.helenalocal.base.get.OrderHub;
import org.helenalocal.utils.ImageCache;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RestaurantDetailActivity extends Activity {

    public static final String EXTRA_BUYER_ID = "org.helenalocal.extra.buyer_id";

    private static final String LogTag = "RestaurantDetailActivity";

    private Buyer _buyer;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.restaurant_detail_activity);

        String buyerId = getIntent().getStringExtra(EXTRA_BUYER_ID);
        _buyer = Hub.buyerMap.get(buyerId);


        // load image
        ImageCache imageCache = ((HubApplication)getApplication()).getImageCache();
        ImageView imageView = (ImageView)findViewById(R.id.restaurantImageView);
        imageCache.loadImage(imageView, _buyer.getIconUrl(), R.drawable.default_restaurant);

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

        ImageCache imageCache = ((HubApplication)getApplication()).getImageCache();
        CertItemClickListener clickListener = new CertItemClickListener();

        for (Certification cert : _buyer.getCertifications()) {
            RelativeLayout relativeLayout = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.certification, null);
            relativeLayout.setOnClickListener(clickListener);
            relativeLayout.setTag(cert);

            ImageView imageView = (ImageView) relativeLayout.findViewById(R.id.imageView);
            imageCache.loadImage(imageView, cert.getIconUrl(), R.drawable.default_certification);

            TextView textView = (TextView)relativeLayout.findViewById(R.id.textView);
            textView.setText(cert.getDisplayName());

            linearLayout.addView(relativeLayout);
        }
    }

    private void loadProducts() {

        TableLayout tableLayout = (TableLayout)findViewById(R.id.productsLayout);
        tableLayout.removeAllViews();

        ProductItemClickListener clickListener = new ProductItemClickListener();

        Log.w(LogTag, "Buyer ID: " + _buyer.getBID());

        List<Order> buyerOrder = OrderHub.getOrdersForBuyer(_buyer.getBID());

        // now sort buyer orders
        Collections.sort(buyerOrder, new Comparator<Order>() {
            public int compare(Order o1, Order o2) {
                if (o1.getDate() == null || o2.getDate() == null)
                    return 0;
                return o2.getDate().compareTo(o1.getDate());
            }
        });

        // now display
        ImageCache imageCache = ((HubApplication)getApplication()).getImageCache();
        for (Order order : buyerOrder) {
            Item item = Hub.itemMap.get(order.getItemID());
            Producer producer = Hub.producerMap.get(order.getProducerID());

            TableRow tableRow = (TableRow) LayoutInflater.from(this).inflate(R.layout.restaurant_product, null);
            tableRow.setOnClickListener(clickListener);
            tableRow.setTag(producer);

            ImageView imageView = (ImageView) tableRow.findViewById(R.id.imageView);
            imageCache.loadImage(imageView, producer.getIconUrl(), R.drawable.default_producer);

            TextView textView = (TextView) tableRow.findViewById(R.id.productNameTextView);
            textView.setText(item.getProductDesc());

            textView = (TextView) tableRow.findViewById(R.id.purchaseDateTextView);
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            textView.setText(String.format(getResources().getString(R.string.product_purchase_date), dateFormat.format(order.getDate().getTime())));

            tableLayout.addView(tableRow);

            Log.w(LogTag, String.format("Order: ID - %s Item - %s BuyerId - %s", order.getOrderID(), item.getProductDesc(), order.getBuyerID()));
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
            Producer producer = (Producer)v.getTag();

            Intent intent = new Intent(RestaurantDetailActivity.this, GrowerDetailActivity.class);
            intent.putExtra(GrowerDetailActivity.EXTRA_PRODUCER_ID, producer.getPID());
            startActivity(intent);
        }
    }
}