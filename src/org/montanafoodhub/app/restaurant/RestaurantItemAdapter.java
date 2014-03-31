/*
 * Copyright (c) 2014. This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License for Helena Local Inc. All rights reseved.
 */

package org.montanafoodhub.app.restaurant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import org.montanafoodhub.app.InfoHeaderItem;
import org.montanafoodhub.app.ListItem;

import java.util.List;

public class RestaurantItemAdapter extends BaseAdapter {

    private static final String logTag = "RestaurantItemAdapter";

    private static final int ViewTypeUndefined = -1;
    private static final int ViewTypeInfoHeader = 0;
    private static final int ViewTypeBuyer = 1;
    private static final int ViewTypeCount = 2;

    private List<ListItem> _itemList;
    private Context _context;


    public RestaurantItemAdapter(Context context, List<ListItem> items) {
        _itemList = items;
        _context = context;
    }

    @Override
    public int getCount() {
        return _itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return _itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return ViewTypeCount;
    }

    @Override
    public int getItemViewType(int position) {

        int type = ViewTypeUndefined;

        Object item = _itemList.get(position);
        if (item instanceof InfoHeaderItem) {
            type = ViewTypeInfoHeader;
        }
        else if (item instanceof BuyerItem) {
            type = ViewTypeBuyer;
        }

        return type;
    }

    @Override
    public boolean isEnabled(int position) {

        return (getItemViewType(position) == ViewTypeBuyer) ? true : false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ListItem item = _itemList.get(position);

        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(_context).inflate(item.getViewId(), parent, false);
        }

        item.loadView(view);

        return view;
    }
}
