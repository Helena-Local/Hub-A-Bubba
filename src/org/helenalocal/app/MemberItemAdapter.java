/*
 * Copyright (c) 2014. This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License for Helena Local Inc. All rights reseved.
 */

package org.helenalocal.app;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import org.helenalocal.base.Item;
import org.helenalocal.Helena_Local_Hub.R;
import org.helenalocal.utils.ImageCache;

import java.util.List;

public class MemberItemAdapter extends BaseAdapter implements View.OnClickListener {

    public interface IActionItemClickedListener {
        public void onProducerItemClicked(Item item);
        public void onAboutItemClicked(Item item);
        public void onRecipeItemClicked(Item item);
    }

    private static String LogTag = "MemberItemAdapter";
    private static final int ViewTypeSection = 0;
    private static final int ViewTypeProduct = 1;

    private List<Object> _itemList;
    private Context _context;
    private ImageCache _imageCache;
    private IActionItemClickedListener _clickListener;

    public MemberItemAdapter(Context context, ImageCache imageCache, List<Object> items, IActionItemClickedListener clickListener) {
        _context = context;
        _itemList = items;
        _imageCache = imageCache;
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
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return (_itemList.get(position) instanceof String) ? ViewTypeSection : ViewTypeProduct;
    }

    @Override
    public boolean isEnabled(int position) {
        // no items are clickable
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        int viewType = getItemViewType(position);

        View view;

        if (convertView != null) {
            view = convertView;
        }
        else {
            int layoutId = (viewType == ViewTypeSection) ? R.layout.product_share_listview_section : R.layout.product_share_listview_item;
            view = LayoutInflater.from(_context).inflate(layoutId, parent, false);
        }

        TextView textView;
        if (viewType == ViewTypeSection) {
            textView = (TextView)view.findViewById(R.id.textView);
            textView.setText((String)_itemList.get(position));
        }
        else {
            Item item = (Item)_itemList.get(position);

            ImageView imageView = (ImageView)view.findViewById(R.id.imageView);
            _imageCache.loadImage(imageView, item.getProductImageUrl(), R.drawable.default_product);

            textView = (TextView)view.findViewById(R.id.productDesc);
            textView.setText(item.getProductDesc());

            // action items
            textView = (TextView)view.findViewById(R.id.producerInfo);
            textView.setOnClickListener(this);
            textView.setTag(item);

            textView = (TextView)view.findViewById(R.id.productInfo);
            textView.setOnClickListener(this);
            textView.setTag(item);

            textView = (TextView)view.findViewById(R.id.recipeInfo);
            textView.setOnClickListener(this);
            textView.setTag(item);


//            View separator = (View)view.findViewById(R.id.separatorView);
//            if (position + 1 == _itemList.size()) {
//                // very last item in list
//                separator.setVisibility(View.INVISIBLE);
//            }
//            else {
//                // last item in a section
//                viewType = getItemViewType(position + 1);
//                if (viewType == ViewTypeSection) {
//                    separator.setVisibility(View.INVISIBLE);
//                }
//                else {
//                    // normal item. Due to recycling make sure the separator is visible
//                    separator.setVisibility(View.VISIBLE);
//                }
//            }
        }

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
