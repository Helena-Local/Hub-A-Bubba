/*
 * Copyright (c) 2014. This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License for Helena Local Inc. All rights reseved.
 */

package org.helenalocal.app;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import org.helenalocal.Helena_Local_Hub.R;
import org.helenalocal.app.test.AsyncTesterTask;
import org.helenalocal.utils.ViewServer;

public class MainActivity extends ActionBarActivity {

    private static final String LAST_SELECTED_TAB = "LastSelectedTab";
    private static final String Tag = "MainActivity";


    private int _lastSelectedTab = 0;

    private void addTab(Class tabClass, int stringId) {
        ActionBar actionBar = getSupportActionBar();
        ActionBar.TabListener tabListener = new TabListener(this, tabClass);
        ActionBar.Tab tab = actionBar.newTab();
        tab.setText(stringId);
        tab.setTabListener(tabListener);
        actionBar.addTab(tab);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        addTab(EventsFragment.class, R.string.event_fragment_title);
        addTab(MemberFragment.class, R.string.member_fragment_title);
        addTab(RestaurantFragment.class, R.string.restaurant_fragment_title);
        addTab(ProducerFragment.class, R.string.producer_fragment_title);
        addTab(ProductFragement.class, R.string.product_fragment_title);

        if (savedInstanceState != null) {
            _lastSelectedTab = savedInstanceState.getInt(LAST_SELECTED_TAB);
        }

        ViewServer.get(this).addWindow(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(LAST_SELECTED_TAB, _lastSelectedTab);
    }

    @Override
    protected void onResume() {
        super.onResume();

        getSupportActionBar().getTabAt(_lastSelectedTab).select();

        ((HubApplication)getApplication()).startHubThreads();

        ViewServer.get(this).setFocusedWindow(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        ((HubApplication)getApplication()).stopHubThreads();

        _lastSelectedTab = getSupportActionBar().getSelectedTab().getPosition();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ViewServer.get(this).removeWindow(this);
    }


    public void onClick(View view) {
        AsyncTesterTask asyncTesterTask = new AsyncTesterTask(this, (HubApplication)getApplication());
        asyncTesterTask.execute(null);
    }
}