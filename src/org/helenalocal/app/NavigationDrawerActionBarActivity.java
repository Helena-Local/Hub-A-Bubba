package org.helenalocal.app;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import org.helenalocal.Helena_Local_Hub.R;

public abstract class NavigationDrawerActionBarActivity extends ActionBarActivity implements ListView.OnItemClickListener {

    // todo - create dynamic mapping
    public static int EVENT_HOME = 0;
    public static int CSA_HOME = 1;
    public static int RESTAURANT_HOME = 2;
    public static int PRODUCER_HOME = 3;
    public static int PRODUCT_HOME = 4;

    public abstract CharSequence getActivityTitle();
    protected abstract int getHierarchialParent();

    protected DrawerLayout _drawerlayout;
    protected ActionBarToggle _drawerToggle;
    protected ListView _drawerListView;

    private String[] _drawerItems;

    public CharSequence getDrawerTitle() {
        return getResources().getString(R.string.app_name);
    }

    protected void setupDrawer() {
        _drawerItems = getResources().getStringArray(R.array.navigation_drawer_items);
        _drawerlayout = (DrawerLayout)findViewById(R.id.drawerLayout);
        _drawerListView = (ListView)findViewById(R.id.drawerList);

        _drawerListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, _drawerItems));
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
    public boolean onOptionsItemSelected(MenuItem item) {

        boolean handled = true;
        if (_drawerToggle.onOptionsItemSelected(item) == false) {
            if (item.getItemId() == android.R.id.home) {
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                upIntent.putExtra(MainActivity.EXTRA_DRAWER_ITEM_ID, getHierarchialParent());
                NavUtils.navigateUpTo(this, upIntent);
            }
            else {
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
}
