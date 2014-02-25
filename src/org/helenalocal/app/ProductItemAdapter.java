package org.helenalocal.app;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import org.helenalocal.base.Item;
import org.helenalocal.Helena_Local_Hub.R;

import java.util.List;

public class ProductItemAdapter extends BaseAdapter {

    private static String logTag = "ProductItemAdapter";
    private static final int ViewTypeSection = 0;
    private static final int ViewTypeProduct = 1;

    private List<Object> _itemList;
    private Context _context;

    public ProductItemAdapter(Context context, List<Object> items) {
        _context = context;
        _itemList = items;
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
            int layoutId = (viewType == ViewTypeSection) ? R.layout.product_listview_section : R.layout.product_listview_item;
            view = LayoutInflater.from(_context).inflate(layoutId, parent, false);
        }

        TextView textView;
        if (viewType == ViewTypeSection) {
            textView = (TextView)view.findViewById(R.id.textView);
            textView.setText((String)_itemList.get(position));
        }
        else {
            Item item = (Item)_itemList.get(position);

            textView = (TextView)view.findViewById(R.id.productDesc);
            textView.setText(item.getProductDesc());

            textView = (TextView)view.findViewById(R.id.unitsAvailable);
            textView.setText(String.format("%s units available", item.getUnitsAvailable()));

            textView = (TextView)view.findViewById(R.id.costPerUnit);
            textView.setText(String.format("$%.2f per %s", item.getUnitPrice(), item.getUnitDesc()));
        }

        return view;
    }
}
