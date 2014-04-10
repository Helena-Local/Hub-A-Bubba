/*
 * Copyright (c) 2014. This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License for Helena Local Inc. All rights reseved.
 */

package org.montanafoodhub.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import org.montanafoodhub.Helena_Hub.R;
import org.montanafoodhub.app.csa.CSAFragment;
import org.montanafoodhub.app.event.EventFragment;
import org.montanafoodhub.app.producer.ProducerFragment;
import org.montanafoodhub.app.product.ProductFragement;
import org.montanafoodhub.app.restaurant.RestaurantFragment;
import org.montanafoodhub.app.test.AsyncTesterTask;
import org.montanafoodhub.app.utils.Feedback;
import org.montanafoodhub.app.utils.ViewServer;

public class MainActivity extends NavigationDrawerActionBarActivity {

    public static final String EXTRA_DRAWER_ITEM_ID = "org.montanafoodhub.extra.drawer_item_id";

    private static final String LogTag = "MainActivity";
    private static final int DRAWER_OPEN_DELAY_MS = 750;
    private static final String LAST_SELECTED_FRAGMENT = "LastSelectedFragment";


    private FragmentBase _currentFrag;
    private int _lastSelectedFragment = 0;
    private SharedPreferences.OnSharedPreferenceChangeListener _preferenceChangedListener;

    private ProgressDialog _waitScreen;

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

        ViewServer.get(this).addWindow(this);
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

        setupFirstAppRun();
        ((HubApplication)getApplication()).startHubThreads();

        ViewServer.get(this).setFocusedWindow(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        teardownFirstAppRun();
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

    private void setupFirstAppRun() {
        SharedPreferences prefs = getSharedPreferences(Preferences.File, Context.MODE_PRIVATE);
        boolean firstRun = prefs.getBoolean(Preferences.FIRST_RUN_AFTER_INSTALL, true);
        if (firstRun == true) {

            // toss up a wait screen while the data loads for the first time.
            setupWaitScreen();

            // wait for the load to finish
            registerPreferenceChangedListener();
        }
    }

    private void teardownFirstAppRun() {
        teardownWaitScreen();
        unregisterPreferenceChangedListener();
    }

    private void setupWaitScreen() {
        _waitScreen = new ProgressDialog(this);
        _waitScreen.setMessage(getResources().getString(R.string.appInitializing));
        _waitScreen.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        _waitScreen.setCanceledOnTouchOutside(false);
        _waitScreen.show();
    }

    private void teardownWaitScreen() {
        if (_waitScreen != null) {
            _waitScreen.dismiss();
            _waitScreen = null;
        }
    }

    private void registerPreferenceChangedListener() {
        SharedPreferences prefs = getSharedPreferences(Preferences.File, Context.MODE_PRIVATE);
        _preferenceChangedListener = getPreferenceChangedListener();
        prefs.registerOnSharedPreferenceChangeListener(_preferenceChangedListener);
    }

    private void unregisterPreferenceChangedListener() {
        if (_preferenceChangedListener != null) {

            SharedPreferences prefs = getSharedPreferences(Preferences.File, Context.MODE_PRIVATE);
            prefs.unregisterOnSharedPreferenceChangeListener(_preferenceChangedListener);
            _preferenceChangedListener = null;
        }
    }


    private SharedPreferences.OnSharedPreferenceChangeListener getPreferenceChangedListener() {
        return new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

                if (key.equals(Preferences.FIRST_RUN_AFTER_INSTALL)) {
                    teardownWaitScreen();
                    unregisterPreferenceChangedListener();

                    // pop the drawer open on the first run after installation to let the user know it is there
                    new Handler().postDelayed(openDrawerRunnable(), DRAWER_OPEN_DELAY_MS);
                }
            }
        };
    }

    private Runnable openDrawerRunnable() {
        return new Runnable() {
            @Override
            public void run() {
                _drawerlayout.openDrawer(GravityCompat.START);
            }
        };
    }

    private void selectItem(int position) {

        switch (position) {
            case 0:
                _currentFrag = new EventFragment();
                break;
            case 1:
                _currentFrag = new CSAFragment();
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