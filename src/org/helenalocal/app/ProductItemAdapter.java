package org.helenalocal.app;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import org.helenalocal.Helena_Local_Hub.R;

import java.util.List;

public class ProductItemAdapter extends ArrayAdapter<ProductTab.Category> {

    private static String logTag = "ProductItemAdapter";

    public ProductItemAdapter(Context context, List<ProductTab.Category> items) {
        super(context, R.layout.product_category_listview_item, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ProductTab.Category category = getItem(position);

        View view;

        if (convertView != null) {
            view = convertView;
        }
        else {
            view = LayoutInflater.from(getContext()).inflate(R.layout.product_category_listview_item, parent, false);
        }

        TextView textView = (TextView)view.findViewById(R.id.categoryDescription);
        textView.setText(category.getDescription());

        int itemCount = category.getItemList().size();
        String countText = getContext().getResources().getQuantityString(R.plurals.category_count, itemCount, itemCount);
        textView = (TextView)view.findViewById(R.id.categoryCount);
        textView.setText(countText);

        return view;
    }
}
