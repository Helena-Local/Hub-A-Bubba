package org.helenalocal.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

        Producer p = getItem(position);

        LinearLayout growerView;

        if (convertView != null) {
            growerView = (LinearLayout)convertView;
        }
        else {
            growerView = new LinearLayout(getContext());
            LayoutInflater li = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            li.inflate(_resourceId, growerView, true);
        }

        TextView growerName = (TextView)growerView.findViewById(R.id.growerNameTextView);
        growerName.setText(p.getName());


        return growerView;
    }

}