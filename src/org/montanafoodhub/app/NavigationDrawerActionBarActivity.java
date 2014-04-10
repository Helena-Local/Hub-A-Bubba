/*
 * Copyright (c) 2014. This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License for Helena Local Inc. All rights reseved.
 */

package org.montanafoodhub.app;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import org.montanafoodhub.Helena_Hub.R;
import org.montanafoodhub.app.utils.Feedback;

public abstract class NavigationDrawerActionBarActivity extends ActionBarActivity implements ListView.OnItemClickListener {

    // todo - create dynamic mapping
    public static int EVENT_HOME = 0;
    public static int CSA_HOME = 1;
    public static int RESTAURANT_HOME = 2;
    public static int PRODUCER_HOME = 3;
    public static int PRODUCT_HOME = 4;

    private static final int FeedbackRequestId = 999;

    public abstract CharSequence getActivityTitle();
    protected abstract int getHierarchialParent();

    protected DrawerLayout _drawerlayout;
    protected ActionBarToggle _drawerToggle;
    protected ListView _drawerListView;

    private String[] _drawerItems;
    private Feedback _feedback;

    public CharSequence getDrawerTitle() {
        return getResources().getString(R.string.app_name);
    }

    protected void setupDrawer() {
        _drawerItems = getResources().getStringArray(R.array.navigation_drawer_items);
        _drawerlayout = (DrawerLayout)findViewById(R.id.drawerLayout);
        _drawerListView = (ListView)findViewById(R.id.drawerList);

        _drawerListView.setAdapter(new ArrayAdapter<String>(this, R.layout.navigation_drawer_item, R.id.drawerItemTextView, _drawerItems));
        _drawerListView.setOnItemClickListener(this);

        _drawerToggle = new ActionBarToggle(this, _drawerlayout);
        _drawerlayout.setDrawerListener(_drawerToggle);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.drawer);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        boolean handled = true;
        if (_drawerToggle.onOptionsItemSelected(item) == false) {
            if (item.getItemId() == android.R.id.home) {
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                upIntent.putExtra(MainActivity.EXTRA_DRAWER_ITEM_ID, getHierarchialParent());
                NavUtils.navigateUpTo(this, upIntent);
            } else if (item.getItemId() == R.id.sendFeedback) {
                _feedback = new Feedback(this, getWindow());
                Intent i = _feedback.getFeedbackIntent();
                startActivityForResult(i, FeedbackRequestId);
            } else {
                handled = super.onOptionsItemSelected(item);
            }
        }

        return handled;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        _drawerToggle.syncState();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent upIntent = NavUtils.getParentActivityIntent(this);
        upIntent.putExtra(MainActivity.EXTRA_DRAWER_ITEM_ID, position);
        NavUtils.navigateUpTo(this, upIntent);

        _drawerlayout.closeDrawer(_drawerListView);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        _drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FeedbackRequestId) {
            _feedback.finish();
            _feedback = null;
        }
    }
}
