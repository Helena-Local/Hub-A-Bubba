/*
 * Copyright (c) 2014. This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License for Helena Local Inc. All rights reseved.
 */

package org.helenalocal.app.member;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import org.helenalocal.app.InfoHeaderItem;
import org.helenalocal.app.ListItem;

import java.util.List;

public class CSAItemAdapter extends BaseAdapter {

    private static final String LogTag = "CSAItemAdapter";

    private final int ViewTypeUndefined = -1;
    private final int ViewTypeInfoHeader = 0;
    private final int ViewTypeCSA = 1;
    private final int ViewTypeCount = 2;

    private List<ListItem> _csaList;
    private Context _context;

    public CSAItemAdapter(Context context, List<ListItem> items) {
        _context = context;
        _csaList = items;
    }

    @Override
    public int getCount() {
        return _csaList.size();
    }

    @Override
    public Object getItem(int position) {
        return _csaList.get(position);
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

        ListItem item = _csaList.get(position);
        if (item instanceof InfoHeaderItem) {
            type = ViewTypeInfoHeader;
        }
        else if (item instanceof CSAItem) {
            type = ViewTypeCSA;
        }

        return type;
    }

    @Override
    public boolean isEnabled(int position) {
        return (getItemViewType(position) == ViewTypeCSA) ? true : false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ListItem item = _csaList.get(position);

        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(_context).inflate(item.getViewId(), parent, false);
        }

        item.loadView(view);

        return view;
    }
}
