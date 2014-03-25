/*
 * Copyright (c) 2014. This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License for Helena Local Inc. All rights reseved.
 */

package org.helenalocal.app.producer;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import org.helenalocal.Helena_Local_Hub.R;
import org.helenalocal.app.ListItem;
import org.helenalocal.base.Producer;
import org.helenalocal.utils.ImageCache;

public class ProducerItem extends ListItem {

    private ImageCache _imageCache;
    private Producer _producer;
    public Producer getProducer() { return _producer; }

    public ProducerItem(Producer producer, ImageCache imageCache) {
        _producer = producer;
        _imageCache = imageCache;
    }

    @Override
    public int getViewId() {
        return R.layout.producer_listview_item;
    }

    @Override
    public void loadView(View view) {
        ImageView imageView = (ImageView)view.findViewById(R.id.producerImageView);
        imageView.setImageBitmap(null);
        _imageCache.loadImage(imageView, _producer.getIconUrl(), R.drawable.default_producer);

        TextView textView = (TextView)view.findViewById(R.id.producerNameTextView);
        textView.setText(_producer.getName());

        textView = (TextView)view.findViewById(R.id.cityTextView);
        textView.setText(_producer.getLocationDisplay());
    }
}
