package org.helenalocal.app;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.helenalocal.base.Product;
import org.helenalocal.Helena_Local_Hub.R;

import java.util.List;

public class ProductItemAdapter extends ArrayAdapter<Product> {
    private static String logTag = "ProductItemAdapter";
    private int _resource;

    public ProductItemAdapter(Context context, int resource, List<Product> items) {
        super(context, resource, items);
        _resource = resource;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Product p = getItem(position);

        LinearLayout productView;

        if (convertView != null) {
            productView = (LinearLayout)convertView;
        }
        else {
            productView = new LinearLayout(getContext());
            LayoutInflater li = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            li.inflate(_resource, productView, true);
        }

        TextView productDesc = (TextView)productView.findViewById(R.id.productDesc);
        TextView unitsAvail = (TextView)productView.findViewById(R.id.unitsAvailable);
        TextView costPerUnit = (TextView)productView.findViewById(R.id.costPerUnit);

        productDesc.setText(p.getProductDesc());
        unitsAvail.setText(String.format("%s units available", p.getUnitsAvailable()));
        costPerUnit.setText(String.format("$%.2f per %s", p.getUnitPrice(), p.getUnitDesc()));



        return productView;
    }
}
