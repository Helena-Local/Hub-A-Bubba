package org.helenalocal.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import org.helenalocal.Helena_Local_Hub.R;
import org.helenalocal.base.*;
import org.helenalocal.base.get.BuyerHub;
import org.helenalocal.base.get.ItemHub;
import org.helenalocal.base.get.OrderHub;
import org.helenalocal.base.get.ProducerHub;
import org.helenalocal.base.post.GrowerHub;
import org.helenalocal.utils.ViewServer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MainActivity extends ActionBarActivity {

    private static final String Tag = "MainActivity";

    private static void initGetHubs(Context _context) {
        // schedule hub refreshes...
        ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(4);
        exec.scheduleWithFixedDelay(new BuyerHub(_context), 0, Hub.buyerDelay, TimeUnit.MINUTES);
        exec.scheduleWithFixedDelay(new ItemHub(_context), 0, Hub.itemDelay, TimeUnit.MINUTES);
        exec.scheduleWithFixedDelay(new OrderHub(_context), 0, Hub.orderDelay, TimeUnit.MINUTES);
        exec.scheduleWithFixedDelay(new ProducerHub(_context), 0, Hub.producerDelay, TimeUnit.MINUTES);
    }

    public void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);

        // init all hubs which are all individual scheduled threads
        initGetHubs(this.getApplicationContext());

//        setContentView(R.layout.main_activity);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        addTab(HomeTab.class, R.string.home_tab_text);
        addTab(MemberTab.class, R.string.member_tab_text);
        addTab(ProductTab.class, R.string.product_tab_text);
        addTab(GrowerTab.class, R.string.grower_tab_text);
        addTab(RestaurantTab.class, R.string.restaurant_tab_text);

        ViewServer.get(this).addWindow(this);
    }

    private void addTab(Class tabClass, int stringId) {

        ActionBar actionBar = getSupportActionBar();

        ActionBar.TabListener tabListener = new TabListener(this, tabClass);
        ActionBar.Tab tab = actionBar.newTab();

        tab.setText(stringId);
        tab.setTabListener(tabListener);
        actionBar.addTab(tab);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ViewServer.get(this).removeWindow(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        ViewServer.get(this).setFocusedWindow(this);
    }

    public void onClick(View view) {

        AsyncTesterTask asyncTesterTask = new AsyncTesterTask(this);
        asyncTesterTask.execute(null);
   }
}