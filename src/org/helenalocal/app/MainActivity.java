/*
 * Copyright (c) 2014. This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License for Helena Local Inc. All rights reseved.
 */

package org.helenalocal.app;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import org.helenalocal.Helena_Local_Hub.R;
import org.helenalocal.base.Hub;
import org.helenalocal.base.get.BuyerHub;
import org.helenalocal.base.get.ItemHub;
import org.helenalocal.base.get.OrderHub;
import org.helenalocal.base.get.ProducerHub;
import org.helenalocal.utils.ViewServer;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MainActivity extends ActionBarActivity {
    private static ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(4);
    ScheduledFuture buyerHubScheduledFuture;
    ScheduledFuture<?> itemHubScheduledFuture;
    ScheduledFuture<?> orderHubScheduledFuture;
    ScheduledFuture<?> producerHubScheduledFuture;

    private static final String Tag = "MainActivity";

    private void startHubThreads() {
        Log.w(Tag, "startHubThreads exec.getQueue().size() = " + exec.getQueue().size());
        // schedule hub refreshes...
        buyerHubScheduledFuture = exec.scheduleWithFixedDelay(new BuyerHub(this), 0, Hub.buyerDelay, TimeUnit.MINUTES);
        itemHubScheduledFuture = exec.scheduleWithFixedDelay(new ItemHub(this), 0, Hub.itemDelay, TimeUnit.MINUTES);
        orderHubScheduledFuture = exec.scheduleWithFixedDelay(new OrderHub(this), 0, Hub.orderDelay, TimeUnit.MINUTES);
        producerHubScheduledFuture = exec.scheduleWithFixedDelay(new ProducerHub(this), 0, Hub.producerDelay, TimeUnit.MINUTES);
        Log.w(Tag, "startHubThreads exec.getQueue().size() = " + exec.getQueue().size());
    }

    private void stopHubThreads() {
        Log.w(Tag, "stopHubThreads exec.getQueue().size() = " + exec.getQueue().size());
        if (exec.getQueue().size() != 0) {
            buyerHubScheduledFuture.cancel(false);
            itemHubScheduledFuture.cancel(false);
            orderHubScheduledFuture.cancel(false);
            producerHubScheduledFuture.cancel(false);
        }
        exec.shutdownNow();
        exec = null;
        exec = new ScheduledThreadPoolExecutor(4);
        Log.w(Tag, "stopHubThreads exec.getQueue().size() = " + exec.getQueue().size());
    }

    private void addTab(Class tabClass, int stringId) {
        ActionBar actionBar = getSupportActionBar();
        ActionBar.TabListener tabListener = new TabListener(this, tabClass);
        ActionBar.Tab tab = actionBar.newTab();
        tab.setText(stringId);
        tab.setTabListener(tabListener);
        actionBar.addTab(tab);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        addTab(HomeTab.class, R.string.home_tab_text);
        addTab(MemberTab.class, R.string.member_tab_text);
        addTab(ProductTab.class, R.string.product_tab_text);
        addTab(GrowerTab.class, R.string.grower_tab_text);
        addTab(RestaurantTab.class, R.string.restaurant_tab_text);

        ViewServer.get(this).addWindow(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public void onResume() {
        super.onResume();
        startHubThreads();
        Log.w(Tag, "Scheduled hub refreshes...");
        ViewServer.get(this).setFocusedWindow(this);
    }

    @Override
    protected void onPause() {
        stopHubThreads();
        Log.w(Tag, "Stopped hub refreshes...");
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ViewServer.get(this).removeWindow(this);
    }

    public void onClick(View view) {
        AsyncTesterTask asyncTesterTask = new AsyncTesterTask(this);
        asyncTesterTask.execute(null);
   }
}