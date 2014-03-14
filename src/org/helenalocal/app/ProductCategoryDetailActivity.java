/*
 * Copyright (c) 2014. This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License for Helena Local Inc. All rights reseved.
 */

package org.helenalocal.app;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import org.helenalocal.Helena_Local_Hub.R;
import org.helenalocal.base.Hub;
import org.helenalocal.base.Item;

import java.util.*;

public class ProductCategoryDetailActivity extends Activity {
    private static final String LogTag = "ProductCategoryDetailActivity";

    private String _category;
    private ProductCategoryDetailAdapter _adapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_category_detail_activity);

        _category = getIntent().getStringExtra(ProductFragement.CATEGORY_NAME_EXTRA);

        List<Item> itemList = new ArrayList<Item>();
        for (Item item : Hub.itemMap.values()) {
            if ((item.getUnitsAvailable() > 0) && (item.getCategory().equalsIgnoreCase(_category))) {
                itemList.add(item);
            }
        }

        _adapter = new ProductCategoryDetailAdapter(this, itemList);
        ListView listView = (ListView)findViewById(R.id.listView);
        listView.setAdapter(_adapter);
        _adapter.notifyDataSetChanged();
    }


    @Override
    protected void onResume() {
        super.onResume();

    }

}