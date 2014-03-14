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
    //    private static final String LAST_SELECTED_TAB = "LastSelectedTab";


    private FragmentBase _currentFrag;
//    private int _lastSelectedTab = 0;

    @Override
    public String getActivityTitle() {
        return _currentFrag.getTitle();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupDrawer();

        Intent intent = getIntent();
        int selectedItem = intent.getIntExtra(EXTRA_DRAWER_ITEM_ID, 0);
        selectItem(selectedItem);

        // pop the drawer open on the first run after installation to let the user know it is there
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        boolean firstRun = prefs.getBoolean(PREFS_FIRST_RUN, true);
        if (firstRun == true) {
            SharedPreferences.Editor prefEdit = prefs.edit();
            prefEdit.putBoolean(PREFS_FIRST_RUN, false);
            prefEdit.apply();

            new Handler().postDelayed(openDrawerRunnable(), DRAWER_OPEN_DELAY_MS);
        }


//        if (savedInstanceState != null) {
//            _lastSelectedTab = savedInstanceState.getInt(LAST_SELECTED_TAB);
//        }

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

//        outState.putInt(LAST_SELECTED_TAB, _lastSelectedTab);
    }

    @Override
    protected void onResume() {
        super.onResume();

//        getSupportActionBar().getTabAt(_lastSelectedTab).select();

        ((HubApplication)getApplication()).startHubThreads();

        ViewServer.get(this).setFocusedWindow(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        ((HubApplication)getApplication()).stopHubThreads();

//        _lastSelectedTab = getSupportActionBar().getSelectedTab().getPosition();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ViewServer.get(this).removeWindow(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        super.onItemClick(parent, view, position, id);
        selectItem(position);
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

//        _drawerListView.setItemChecked(position, true);
    }

    public void onClick(View view) {
        AsyncTesterTask asyncTesterTask = new AsyncTesterTask(this, (HubApplication)getApplication());
        asyncTesterTask.execute(null);
    }
}