/*
 * Copyright (c) 2014. This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License for Helena Local Inc. All rights reseved.
 */

package org.montanafoodhub.app;

import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import org.montanafoodhub.Helena_Hub.R;

public class ActionBarToggle extends ActionBarDrawerToggle {

    private NavigationDrawerActionBarActivity _activity;

    public ActionBarToggle(NavigationDrawerActionBarActivity activity, DrawerLayout drawerLayout) {
        super(activity, drawerLayout, R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_closed);

        _activity = activity;
    }

    @Override
    public void onDrawerOpened(View drawerView) {
        super.onDrawerOpened(drawerView);

        _activity.getSupportActionBar().setTitle(_activity.getDrawerTitle());
        _activity.supportInvalidateOptionsMenu();

    }

    @Override
    public void onDrawerClosed(View drawerView) {
        super.onDrawerClosed(drawerView);

        _activity.getSupportActionBar().setTitle(_activity.getActivityTitle());
        _activity.supportInvalidateOptionsMenu();
    }
}
