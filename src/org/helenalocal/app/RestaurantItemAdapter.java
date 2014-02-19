/*
 * Copyright (c) 2014. This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License for Helena Local Inc. All rights reseved.
 */

package org.helenalocal.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.helenalocal.Helena_Local_Hub.R;
import org.helenalocal.base.Buyer;
import org.helenalocal.base.Item;

import java.util.List;

public class RestaurantItemAdapter extends ArrayAdapter<Buyer> {

    private static String logTag = "RestaurantItemAdapter";
    private int _resource;

    public RestaurantItemAdapter(Context context, int resource, List<Buyer> items) {
        super(context, resource, items);
        _resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Buyer buyer = getItem(position);

        LinearLayout view;

        if (convertView != null) {
            view = (LinearLayout)convertView;
        }
        else {
            view = new LinearLayout(getContext());
            LayoutInflater li = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            li.inflate(_resource, view, true);
        }

        ImageView imageView = (ImageView)view.findViewById(R.id.restaurantImageView);
        imageView.setImageBitmap(null);
        new AsyncImageLoader(imageView, R.drawable.default_restaurant).execute(buyer.getIconUrl());

        TextView textView = (TextView)view.findViewById(R.id.restaurantNameTextView);
        textView.setText(buyer.getName());

        textView = (TextView)view.findViewById(R.id.cityTextView);
        textView.setText(buyer.getLocationDisplay());

        return view;
    }

}
