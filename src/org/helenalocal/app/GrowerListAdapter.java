package org.helenalocal.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.helenalocal.Helena_Local_Hub.R;
import org.helenalocal.base.Producer;

import java.util.List;

public class GrowerListAdapter extends ArrayAdapter<Producer> {
    private static String logTag = "GrowerListAdapter";
    private int _resourceId;

    public GrowerListAdapter(Context context, int resource, List<Producer> items) {
        super(context, resource, items);
        _resourceId = resource;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Producer producer = getItem(position);

        LinearLayout view;

        if (convertView != null) {
            view = (LinearLayout)convertView;
        }
        else {
            view = new LinearLayout(getContext());
            LayoutInflater li = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            li.inflate(_resourceId, view, true);
        }

        ImageView imageView = (ImageView)view.findViewById(R.id.producerImageView);
        imageView.setImageBitmap(null);
        new AsyncImageLoader(imageView, R.drawable.default_producer).execute(producer.getIconUrl());

        TextView textView = (TextView)view.findViewById(R.id.producerNameTextView);
        textView.setText(producer.getName());

        textView = (TextView)view.findViewById(R.id.cityTextView);
        textView.setText(producer.getLocationDisplay());

        return view;
    }

}