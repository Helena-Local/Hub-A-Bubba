/*
 * Copyright (c) 2014. This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License for Helena Local Inc. All rights reseved.
 */

package org.helenalocal.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import org.helenalocal.Helena_Local_Hub.R;
import org.helenalocal.app.test.AsyncTesterTask;
import org.helenalocal.utils.ViewServer;

public class MainActivity extends NavigationDrawerActionBarActivity {

    public static final String EXTRA_DRAWER_ITEM_ID = "org.helenalocal.extra.drawer_item_id";

    private static final String LogTag = "MainActivity";
    private static final String PREFS_FIRST_RUN = "FirstRun";
    private static final int DRAWER_OPEN_DELAY_MS = 750;
    private static final String LAST_SELECTED_FRAGMENT = "LastSelectedFragment";


    private FragmentBase _currentFrag;
    private int _lastSelectedFragment = 0;

    @Override
    public String getActivityTitle() {
        String title = getResources().getString(_currentFrag.getTitleId());
        return title;
    }

    @Override
    protected int getHierarchialParent() {
        return EVENT_HOME;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupDrawer();

        if (savedInstanceState != null) {
            _lastSelectedFragment = savedInstanceState.getInt(LAST_SELECTED_FRAGMENT);
        }
        else {
            Intent intent = getIntent();
            _lastSelectedFragment = intent.getIntExtra(EXTRA_DRAWER_ITEM_ID, 0);
        }

        // pop the drawer open on the first run after installation to let the user know it is there
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        boolean firstRun = prefs.getBoolean(PREFS_FIRST_RUN, true);
        if (firstRun == true) {
            SharedPreferences.Editor prefEdit = prefs.edit();
            prefEdit.putBoolean(PREFS_FIRST_RUN, false);
            prefEdit.apply();

            new Handler().postDelayed(openDrawerRunnable(), DRAWER_OPEN_DELAY_MS);
        }

        ViewServer.get(this).addWindow(this);
    }

    private Runnable openDrawerRunnable() {
        return new Runnable() {
            @Override
            public void run() {
                _drawerlayout.openDrawer(GravityCompat.START);
            }
        };
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(LAST_SELECTED_FRAGMENT, _lastSelectedFragment);
    }

    @Override
    protected void onResume() {
        super.onResume();

        selectItem(_lastSelectedFragment);

        ((HubApplication)getApplication()).startHubThreads();

        ViewServer.get(this).setFocusedWindow(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        ((HubApplication)getApplication()).stopHubThreads();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ViewServer.get(this).removeWindow(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        selectItem(position);
        _drawerlayout.closeDrawer(_drawerListView);
    }

    private void selectItem(int position) {

        switch (position) {
            case 0:
                _currentFrag = new EventsFragment();
                break;
            case 1:
                _currentFrag = new MemberFragment();
                break;
            case 2:
                _currentFrag = new RestaurantFragment();
                break;
            case 3:
                _currentFrag = new ProducerFragment();
                break;
            case 4:
                _currentFrag = new ProductFragement();
                break;
        }

        Bundle args = new Bundle();
        args.putInt(EXTRA_DRAWER_ITEM_ID, position);
        _currentFrag.setArguments(args);

        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.mainContent, _currentFrag)
                .commit();

        _lastSelectedFragment = position;
        _drawerListView.setItemChecked(position, true);
        setTitle(getActivityTitle());
    }


    public void onClick(View view) {
        AsyncTesterTask asyncTesterTask = new AsyncTesterTask(this, (HubApplication)getApplication());
        asyncTesterTask.execute(null);
    }
}