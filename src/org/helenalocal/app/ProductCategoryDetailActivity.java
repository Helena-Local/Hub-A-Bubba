/*
 * Copyright (c) 2014. This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License for Helena Local Inc. All rights reseved.
 */

package org.helenalocal.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import org.helenalocal.Helena_Local_Hub.R;

public class ProductCategoryDetailActivity extends NavigationDrawerActionBarActivity {

    private static final String LogTag = "ProductCategoryDetailActivity";

    @Override
    public CharSequence getActivityTitle() {
        return getTitle();
    }

    @Override
    protected int getHierarchialParent() {
        return PRODUCT_HOME;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String category = getIntent().getStringExtra(ProductFragement.CATEGORY_NAME_EXTRA);
        setTitle(category);

        Bundle args = new Bundle();
        args.putString(ProductFragement.CATEGORY_NAME_EXTRA, category);

        Fragment frag = new ProductCategoryDetailFragment();
        frag.setArguments(args);

        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.mainContent, frag)
                .commit();

        setupDrawer();
        _drawerToggle.setDrawerIndicatorEnabled(false);
    }
}