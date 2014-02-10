/*
 * Copyright (c) 2014. This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License for Helena Local Inc. All rights reseved.
 */

package org.helenalocal.app;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import org.helenalocal.Helena_Local_Hub.R;
import org.helenalocal.base.Buyer;
import org.helenalocal.base.Hub;

public class RestaurantDetailActivity extends Activity {

    private static final String Tag = "RestaurantDetailActivity";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.restaurant_detail_activity);

        String buyerId = getIntent().getStringExtra(RestaurantTab.BUYER_ID_KEY);
        Buyer b = Hub.buyerMap.get(buyerId);

        TextView tv = (TextView) findViewById(R.id.nameTextView);
        tv.setText(b.getName());
    }
}