/*
 * Copyright (c) 2014. This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License for Helena Local Inc. All rights reseved.
 */

package org.helenalocal.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import org.helenalocal.Helena_Local_Hub.R;
import org.helenalocal.app.test.AsyncTesterTask;
import org.helenalocal.utils.ViewServer;

public class MainActivity extends NavigationDrawerActionBarActivity {

    public static final String EXTRA_DRAWER_ITEM_ID = "org.helenalocal.extra.drawer_item_id";

    //    private static final String LAST_SELECTED_TAB = "LastSelectedTab";
    private static final String Tag = "MainActivity";


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


//        if (savedInstanceState != null) {
//            _lastSelectedTab = savedInstanceState.getInt(LAST_SELECTED_TAB);
//        }

        ViewServer.get(this).addWindow(this);
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