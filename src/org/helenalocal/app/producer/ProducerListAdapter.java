package org.helenalocal.app.producer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import org.helenalocal.Helena_Local_Hub.R;
import org.helenalocal.base.Producer;
import org.helenalocal.utils.ImageCache;

import java.util.List;

public class ProducerListAdapter extends ArrayAdapter<Producer> {
    private static String logTag = "ProducerListAdapter";
    private ImageCache _imageCache;

    public ProducerListAdapter(Context context, List<Producer> items, ImageCache imageCache) {
        super(context, R.layout.producer_listview_item, items);
        _imageCache = imageCache;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Producer producer = getItem(position);

        View view;

        if (convertView != null) {
            view = convertView;
        }
        else {
            LayoutInflater li = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = li.inflate(R.layout.producer_listview_item, parent, false);
        }

        ImageView imageView = (ImageView)view.findViewById(R.id.producerImageView);
        imageView.setImageBitmap(null);
        _imageCache.loadImage(imageView, producer.getIconUrl(), R.drawable.default_producer);

        TextView textView = (TextView)view.findViewById(R.id.producerNameTextView);
        textView.setText(producer.getName());

        textView = (TextView)view.findViewById(R.id.cityTextView);
        textView.setText(producer.getLocationDisplay());

        return view;
    }

}