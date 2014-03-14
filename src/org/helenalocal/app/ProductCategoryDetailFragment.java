/*
 * Copyright (c) 2014. This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License for Helena Local Inc. All rights reseved.
 */

package org.helenalocal.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import org.helenalocal.Helena_Local_Hub.R;
import org.helenalocal.base.Hub;
import org.helenalocal.base.Item;

import java.util.ArrayList;
import java.util.List;

public class ProductCategoryDetailFragment extends Fragment {

    private static final String LogTag = "ProductCategoryDetailFragment";

    private ProductCategoryDetailAdapter _adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.product_category_detail_fragment, container, false);

        Bundle args = getArguments();
        String category = args.getString(ProductFragement.CATEGORY_NAME_EXTRA);

        List<Item> itemList = new ArrayList<Item>();
        for (Item item : Hub.itemMap.values()) {
            if ((item.getUnitsAvailable() > 0) && (item.getCategory().equalsIgnoreCase(category))) {
                itemList.add(item);
            }
        }

        _adapter = new ProductCategoryDetailAdapter(getActivity(), itemList);
        ListView listView = (ListView) view.findViewById(R.id.listView);
        listView.setAdapter(_adapter);
        _adapter.notifyDataSetChanged();

        return view;
    }
}