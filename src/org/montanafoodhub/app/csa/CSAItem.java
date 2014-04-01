/*
 * Copyright (c) 2014. This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License for Helena Local Inc. All rights reseved.
 */

package org.montanafoodhub.app.csa;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import org.montanafoodhub.Helena_Local_Hub.R;
import org.montanafoodhub.app.ListItem;
import org.montanafoodhub.base.Buyer;
import org.montanafoodhub.app.utils.ImageCache;

public class CSAItem extends ListItem {

    private Buyer _buyer;
    private ImageCache _imageCache;

    public Buyer getCSA() {
        return _buyer;
    }

    public CSAItem(Buyer buyer, ImageCache imageCache) {
        _buyer = buyer;
        _imageCache = imageCache;
    }

    @Override
    public int getViewId() {
        return R.layout.csa_list_item;
    }

    @Override
    public void loadView(View view) {
        ImageView imageView = (ImageView)view.findViewById(R.id.csaImageView);
        _imageCache.loadImage(imageView, _buyer.getIconUrl(), R.drawable.default_csa);

        TextView textView = (TextView)view.findViewById(R.id.csaNameTextView);
        textView.setText(_buyer.getName());

    }
}
