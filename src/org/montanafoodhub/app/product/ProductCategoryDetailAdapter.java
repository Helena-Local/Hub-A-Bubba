/*
 * Copyright (c) 2014. This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License for Helena Local Inc. All rights reseved.
 */

package org.montanafoodhub.app.product;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import org.montanafoodhub.Helena_Hub.R;
import org.montanafoodhub.base.Item;

import java.util.List;

public class ProductCategoryDetailAdapter extends ArrayAdapter<Item> {

    public ProductCategoryDetailAdapter(Context context, List<Item> items) {
        super(context, R.layout.product_listview_item, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Item item = getItem(position);

        View view;

        if (convertView != null) {
            view = convertView;
        }
        else {
            view = LayoutInflater.from(getContext()).inflate(R.layout.product_listview_item, parent, false);
        }


        TextView textView = (TextView)view.findViewById(R.id.productDesc);
        textView.setText(item.getProductDesc());

        textView = (TextView)view.findViewById(R.id.unitsAvailable);
        textView.setText(String.format("%s units available", item.getUnitsAvailable()));

        textView = (TextView)view.findViewById(R.id.costPerUnit);
        textView.setText(String.format("$%.0f per %s", item.getUnitPrice(), item.getUnitDesc()));

        return view;
    }
}
