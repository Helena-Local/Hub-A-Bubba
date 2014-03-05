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
import android.widget.TextView;
import org.helenalocal.Helena_Local_Hub.R;
import org.helenalocal.base.Buyer;
import org.helenalocal.utils.ImageCache;

import java.util.List;

public class RestaurantItemAdapter extends ArrayAdapter<Buyer> {

    private static String logTag = "RestaurantItemAdapter";
    private ImageCache _imageCache;

    public RestaurantItemAdapter(Context context, List<Buyer> items, ImageCache imageCache) {
        super(context, R.layout.restautant_listview_item, items);
        _imageCache = imageCache;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Buyer buyer = getItem(position);

        View view;

        if (convertView != null) {
            view = convertView;
        }
        else {
            LayoutInflater li = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = li.inflate(R.layout.restautant_listview_item, parent, false);
        }


        ImageView imageView = (ImageView)view.findViewById(R.id.restaurantImageView);
        _imageCache.loadImage(imageView, buyer.getIconUrl(), R.drawable.default_restaurant);

        TextView textView = (TextView)view.findViewById(R.id.restaurantNameTextView);
        textView.setText(buyer.getName());

        textView = (TextView)view.findViewById(R.id.cityTextView);
        textView.setText(buyer.getLocationDisplay());

        return view;
    }
}
