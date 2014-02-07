/*
 * Copyright (c) 2014. This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License for Helena Local Inc. All rights reseved.
 */

package org.helenalocal.app;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.helenalocal.base.Item;
import org.helenalocal.Helena_Local_Hub.R;

import java.util.List;

public class MemberItemAdapter extends ArrayAdapter<Item> {
    private static String logTag = "MemberItemAdapter";
    private int _resource;

    public MemberItemAdapter(Context context, int resource, List<Item> items) {
        super(context, resource, items);
        _resource = resource;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Item item = getItem(position);

        LinearLayout productView;

        if (convertView != null) {
            productView = (LinearLayout)convertView;
        }
        else {
            productView = new LinearLayout(getContext());
            LayoutInflater li = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            li.inflate(_resource, productView, true);
        }

        TextView productDesc = (TextView)productView.findViewById(R.id.productDesc);
        TextView unitsAvail = (TextView)productView.findViewById(R.id.unitsAvailable);
        TextView costPerUnit = (TextView)productView.findViewById(R.id.costPerUnit);

        productDesc.setText(item.getProductDesc());
        unitsAvail.setText(String.format("%s units available", item.getUnitsAvailable()));
        costPerUnit.setText(String.format("$%.2f per %s", item.getUnitPrice(), item.getUnitDesc()));



        return productView;
    }
}
