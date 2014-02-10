/*
 * Copyright (c) 2014. This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License for Helena Local Inc. All rights reseved.
 */

package org.helenalocal.app;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import org.helenalocal.Helena_Local_Hub.R;
import org.helenalocal.app.test.AsyncTesterTask;
import org.helenalocal.base.HubInit;
import org.helenalocal.base.get.*;
import org.helenalocal.utils.ViewServer;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MainActivity extends ActionBarActivity {
    private static ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(5);
    private static ScheduledFuture buyerHubScheduledFuture;
    private static ScheduledFuture<?> itemHubScheduledFuture;
    private static ScheduledFuture<?> orderHubScheduledFuture;
    private static ScheduledFuture<?> producerHubScheduledFuture;
    private static ScheduledFuture<?> certificationHubScheduledFuture;

    private static final String Tag = "MainActivity";

    private void addTab(Class tabClass, int stringId) {
        ActionBar actionBar = getSupportActionBar();
        ActionBar.TabListener tabListener = new TabListener(this, tabClass);
        ActionBar.Tab tab = actionBar.newTab();
        tab.setText(stringId);
        tab.setTabListener(tabListener);
        actionBar.addTab(tab);
    }

    public static void startHubThreads(Context context) {
        Log.w(Tag, "startHubThreads exec.getQueue().size() = " + exec.getQueue().size());
        // schedule hub refreshes...
        certificationHubScheduledFuture = exec.scheduleWithFixedDelay(new CertificationHub(context), 0, HubInit.getCertificateDelay(), TimeUnit.MINUTES);
        itemHubScheduledFuture = exec.scheduleWithFixedDelay(new ItemHub(context), 0, HubInit.getItemDelay(), TimeUnit.MINUTES);
        orderHubScheduledFuture = exec.scheduleWithFixedDelay(new OrderHub(context), 0, HubInit.getOrderDelay(), TimeUnit.MINUTES);
        buyerHubScheduledFuture = exec.scheduleWithFixedDelay(new BuyerHub(context), 0, HubInit.getBuyerDelay(), TimeUnit.MINUTES);
        producerHubScheduledFuture = exec.scheduleWithFixedDelay(new ProducerHub(context), 0, HubInit.getProducerDelay(), TimeUnit.MINUTES);
        Log.w(Tag, "startHubThreads exec.getQueue().size() = " + exec.getQueue().size());
    }

    public static void stopHubThreads() {
        Log.w(Tag, "stopHubThreads exec.getQueue().size() = " + exec.getQueue().size());
        if (exec.getQueue().size() != 0) {
            buyerHubScheduledFuture.cancel(false);
            itemHubScheduledFuture.cancel(false);
            orderHubScheduledFuture.cancel(false);
            producerHubScheduledFuture.cancel(false);
            certificationHubScheduledFuture.cancel(false);
        }
        exec.shutdownNow();
        exec = null;
        exec = new ScheduledThreadPoolExecutor(4);
        Log.w(Tag, "stopHubThreads exec.getQueue().size() = " + exec.getQueue().size());
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
        startHubThreads(this);
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