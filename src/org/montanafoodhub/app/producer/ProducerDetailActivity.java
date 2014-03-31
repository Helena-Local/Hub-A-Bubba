/*
 * Copyright (c) 2014. This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License for Helena Local Inc. All rights reseved.
 */

package org.montanafoodhub.app.producer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import org.montanafoodhub.Helena_Local_Hub.R;
import org.montanafoodhub.app.NavigationDrawerActionBarActivity;
import org.montanafoodhub.base.Hub;
import org.montanafoodhub.base.Producer;

public class ProducerDetailActivity extends NavigationDrawerActionBarActivity {

    public static final String EXTRA_PRODUCER_ID = "org.montanafoodhub.extra.producer_id";

    private static final String LogTag = "ProducerDetailActivity";

    @Override
    public CharSequence getActivityTitle() {
        return getTitle();
    }

    @Override
    protected int getHierarchialParent() {
        return PRODUCER_HOME;
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
}