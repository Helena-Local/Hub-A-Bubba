/*
 * Copyright (c) 2014. This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License for Helena Local Inc. All rights reseved.
 */

package org.montanafoodhub.app.restaurant;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import org.montanafoodhub.Helena_Hub.R;
import org.montanafoodhub.app.ListItem;
import org.montanafoodhub.app.utils.ImageCache;
import org.montanafoodhub.base.Buyer;

public class BuyerItem extends ListItem {

    private Buyer _buyer;
    private ImageCache _imageCache;

    public Buyer getBuyer() { return _buyer; }

    public BuyerItem(Buyer buyer, ImageCache imageCache) {
        _buyer = buyer;
        _imageCache = imageCache;
    }

    @Override
    public int getViewId() {
        return R.layout.restautant_listview_item ;
    }

    @Override
    public void loadView(View view) {
        ImageView imageView = (ImageView)view.findViewById(R.id.restaurantImageView);
        _imageCache.loadImage(imageView, _buyer.getIconUrl(), R.drawable.default_restaurant);

        TextView textView = (TextView)view.findViewById(R.id.restaurantNameTextView);
        textView.setText(_buyer.getName());

        textView = (TextView)view.findViewById(R.id.cityTextView);
        textView.setText(_buyer.getLocationDisplay());

    }
}
