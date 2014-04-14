/*
 * Copyright (c) 2014. This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License for Helena Local Inc. All rights reseved.
 */

package org.montanafoodhub.app.restaurant;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import org.montanafoodhub.Helena_Hub.R;
import org.montanafoodhub.app.FragmentBase;
import org.montanafoodhub.app.HubApplication;
import org.montanafoodhub.app.controls.ActionBar;
import org.montanafoodhub.app.producer.ProducerDetailActivity;
import org.montanafoodhub.app.utils.ActivityUtils;
import org.montanafoodhub.app.utils.ImageCache;
import org.montanafoodhub.base.*;
import org.montanafoodhub.base.get.OrderHub;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RestaurantDetailFragment extends FragmentBase implements ActionBar.ActionBarClickListener, View.OnClickListener{

    private static final String LogTag = RestaurantDetailFragment.class.getSimpleName();
    private Buyer _buyer;

    @Override
    public int getTitleId() {
        // todo - figure out title stuff
        return 0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.restaurant_detail_fragment, container, false);

        Bundle args = getArguments();
        String buyerId = args.getString(RestaurantDetailActivity.EXTRA_BUYER_ID);
        _buyer = Hub.buyerMap.get(buyerId);


        // restaurant image
        ImageCache imageCache = ((HubApplication) getActivity().getApplication()).getImageCache();
        ImageView imageView = (ImageView) view.findViewById(R.id.restaurantImageView);
        imageCache.loadImage(imageView, _buyer.getIconUrl(), R.drawable.default_restaurant);

        // restaurant name
        TextView textView = (TextView) view.findViewById(R.id.restaurantNameTextView);
        textView.setText(_buyer.getName());

        // restaurant address
        textView = (TextView) view.findViewById(R.id.restaurantAddressTextView);
        textView.setText(_buyer.getLocation());

        ActionBar actionBar = (ActionBar) view.findViewById(R.id.actionBar);
        actionBar.setOnClickActionListener(this);

        loadCertifications(view);
        loadProducts(view);

        return view;
    }

    private void loadCertifications(View view) {

        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.certificationLayout);
        linearLayout.removeAllViews();

        ImageCache imageCache = ((HubApplication) getActivity().getApplication()).getImageCache();

        for (Certification cert : _buyer.getCertifications()) {
            RelativeLayout relativeLayout = (RelativeLayout) LayoutInflater.from(getActivity()).inflate(R.layout.certification, null);
            relativeLayout.setOnClickListener(this);
            relativeLayout.setTag(cert);

            ImageView imageView = (ImageView) relativeLayout.findViewById(R.id.certificationImageview);
            imageCache.loadImage(imageView, cert.getIconUrl(), R.drawable.default_certification);

            TextView textView = (TextView)relativeLayout.findViewById(R.id.certificationText);
            textView.setText(cert.getDisplayName());

            linearLayout.addView(relativeLayout);
        }
    }

    private void loadProducts(View view) {

        TableLayout tableLayout = (TableLayout) view.findViewById(R.id.productsLayout);
        tableLayout.removeAllViews();

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
        ImageCache imageCache = ((HubApplication) getActivity().getApplication()).getImageCache();
        for (Order order : buyerOrder) {
            Item item = Hub.itemMap.get(order.getItemID());
            Producer producer = Hub.producerMap.get(order.getProducerID());

            TableRow tableRow = (TableRow) LayoutInflater.from(getActivity()).inflate(R.layout.restaurant_product, null);
            tableRow.setOnClickListener(this);
            tableRow.setTag(producer);

            ImageView imageView = (ImageView) tableRow.findViewById(R.id.recentProductImageView);
            imageCache.loadImage(imageView, producer.getIconUrl(), R.drawable.default_producer);

            TextView textView = (TextView) tableRow.findViewById(R.id.recentProductLeftText);
            textView.setText(item.getProductDesc());

            textView = (TextView) tableRow.findViewById(R.id.recentProductRightText);
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            textView.setText(String.format(getResources().getString(R.string.product_purchase_date), dateFormat.format(order.getDate().getTime())));

            tableLayout.addView(tableRow);

            Log.w(LogTag, String.format("Order: ID - %s Item - %s BuyerId - %s", order.getOrderID(), item.getProductDesc(), order.getBuyerID()));
        }
    }

    @Override
    public void onClick(View v) {
        Object tag = v.getTag();
        if (tag instanceof Certification) {
            onClickCertification((Certification) tag);
        }
        else if (tag instanceof Producer) {
            onClickProducer((Producer) tag);
        }
    }

    @Override
    public void onActionClicked(ActionBar.Action action) {
        if (action == ActionBar.Action.Left) {
            onClickCall();
        }
        else if (action == ActionBar.Action.Center) {
            onClickMap();
        }
        else if (action == ActionBar.Action.Right) {
            onClickUrl();
        }
    }

    private void onClickCall() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + _buyer.getPhone()));
        ActivityUtils.startImplicitActivity(getActivity(), intent, R.string.no_phone_application, LogTag);
    }

    private void onClickMap() {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            String address = String.format("%s %s", _buyer.getName(), _buyer.getLocation());
            String data = String.format("geo:0,0?q=%s", URLEncoder.encode(address, "UTF-8"));
            intent.setData(Uri.parse(data));
            ActivityUtils.startImplicitActivity(getActivity(), intent, R.string.no_maps_application, LogTag);
        } catch (Exception e) {
        }
    }

    private void onClickUrl() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(_buyer.getWebsiteUrl()));
        ActivityUtils.startImplicitActivity(getActivity(), intent, R.string.no_web_browser_application, LogTag);
    }

    private void onClickCertification(Certification cert) {

        if (cert.getWebsiteUrl().isEmpty() == false) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(cert.getWebsiteUrl()));
            ActivityUtils.startImplicitActivity(getActivity(), intent, R.string.no_web_browser_application, LogTag);
        }
    }

    private void onClickProducer(Producer producer) {

        Intent intent = new Intent(getActivity(), ProducerDetailActivity.class);
        intent.putExtra(ProducerDetailActivity.EXTRA_PRODUCER_ID, producer.getPID());
        startActivity(intent);
    }
}