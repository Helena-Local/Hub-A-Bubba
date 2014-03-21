/*
 * Copyright (c) 2014. This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License for Helena Local Inc. All rights reseved.
 */

package org.helenalocal.app.member;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import org.helenalocal.Helena_Local_Hub.R;
import org.helenalocal.base.Item;
import org.helenalocal.utils.ImageCache;

public class ProductItem extends ListItem implements View.OnClickListener{

    public interface IActionItemClickedListener {
        public void onProducerItemClicked(Item item);
        public void onAboutItemClicked(Item item);
        public void onRecipeItemClicked(Item item);
    }

    private Item _item;
    private ImageCache _imageCache;
    private IActionItemClickedListener _clickListener;

    public ProductItem(Item item, ImageCache imageCache, IActionItemClickedListener clickListener) {
        _item = item;
        _imageCache = imageCache;
        _clickListener = clickListener;
    }

    @Override
    public int getViewId() {
        return R.layout.product_share_listview_item;
    }

    @Override
    public void loadView(View view) {

        ImageView imageView = (ImageView)view.findViewById(R.id.imageView);
        _imageCache.loadImage(imageView, _item.getProductImageUrl(), R.drawable.default_product);

        TextView textView = (TextView)view.findViewById(R.id.productDesc);
        textView.setText(_item.getProductDesc());

        // action items
        textView = (TextView)view.findViewById(R.id.producerInfo);
        textView.setOnClickListener(this);
        textView.setTag(_item);

        textView = (TextView)view.findViewById(R.id.productInfo);
        textView.setOnClickListener(this);
        textView.setTag(_item);

        textView = (TextView)view.findViewById(R.id.recipeInfo);
        textView.setOnClickListener(this);
        textView.setTag(_item);
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
