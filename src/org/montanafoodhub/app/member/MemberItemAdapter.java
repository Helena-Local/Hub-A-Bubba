/*
 * Copyright (c) 2014. This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License for Helena Local Inc. All rights reseved.
 */

package org.montanafoodhub.app.member;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import org.montanafoodhub.app.ListItem;
import org.montanafoodhub.base.Item;
import org.montanafoodhub.Helena_Local_Hub.R;

import java.util.List;

public class MemberItemAdapter extends BaseAdapter implements View.OnClickListener {

    private static String LogTag = "MemberItemAdapter";
    private static final int ViewTypeUndefined = -1;
    private static final int ViewTypeMarquee = 0;
    private static final int ViewTypeSection = 1;
    private static final int ViewTypeProduct = 2;
    private static final int ViewTypeCount = 3;

    private List<ListItem> _itemList;
    private Context _context;
    private ProductItem.IActionItemClickedListener _clickListener;

    public MemberItemAdapter(Context context, List<ListItem> items, ProductItem.IActionItemClickedListener clickListener) {
        _context = context;
        _itemList = items;
        _clickListener = clickListener;
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

        ListItem item = _itemList.get(position);
        if (item instanceof MarqueeItem) {
            type = ViewTypeMarquee;
        }
        else if (item instanceof SectionItem) {
            type = ViewTypeSection;
        }
        else if (item instanceof ProductItem) {
            type = ViewTypeProduct;
        }

        return type;
    }

    @Override
    public boolean isEnabled(int position) {
        // no items are clickable
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ListItem listItem = _itemList.get(position);
        View view;

        if (convertView != null) {
            view = convertView;
        }
        else {
            view = LayoutInflater.from(_context).inflate(listItem.getViewId(), parent, false);
        }

        listItem.loadView(view);

        return view;
    }

    @Override
    public void onClick(View v) {
        Item item = (Item)v.getTag();

        if (v.getId() == R.id.producerInfo) {
            _clickListener.onProducerItemClicked(item);
        }
        else if (v.getId() == R.id.productInfo) {
            _clickListener.onAboutItemClicked(item);
        }
        else if (v.getId() == R.id.recipeInfo) {
            _clickListener.onRecipeItemClicked(item);
        }
   }
}
