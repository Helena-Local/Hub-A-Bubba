/*
 * Copyright (c) 2014. This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License for Helena Local Inc. All rights reseved.
 */

package org.helenalocal.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.view.View;
import android.widget.AdapterView;
import org.helenalocal.Helena_Local_Hub.R;
import org.helenalocal.base.Hub;
import org.helenalocal.base.Producer;

public class ProducerDetailActivity extends NavigationDrawerActionBarActivity {

    public static final String EXTRA_PRODUCER_ID = "org.helenalocal.extra.producer_id";

    private static final String LogTag = "ProducerDetailActivity";

    @Override
    public CharSequence getActivityTitle() {
        return getTitle();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String producerId = getIntent().getStringExtra(EXTRA_PRODUCER_ID);
        Producer producer = Hub.producerMap.get(producerId);
        setTitle(producer.getName());

        Bundle args = new Bundle();
        args.putString(EXTRA_PRODUCER_ID, producerId);

        Fragment frag = new ProducerDetailFragment();
        frag.setArguments(args);

        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.mainContent, frag)
                .commit();

        setupDrawer();
        _drawerToggle.setDrawerIndicatorEnabled(false);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        super.onItemClick(parent, view, position, id);

        Intent upIntent = NavUtils.getParentActivityIntent(this);
        upIntent.putExtra(MainActivity.EXTRA_DRAWER_ITEM_ID, position);
        NavUtils.navigateUpTo(this, upIntent);
    }
}