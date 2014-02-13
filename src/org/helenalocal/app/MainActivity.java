/*
 * Copyright (c) 2014. This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License for Helena Local Inc. All rights reseved.
 */

package org.helenalocal.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageSwitcher;
import org.helenalocal.Helena_Local_Hub.R;
import org.helenalocal.app.service.HubInitService;
import org.helenalocal.app.test.AsyncTesterTask;
import org.helenalocal.base.HubInit;
import org.helenalocal.base.get.*;
import org.helenalocal.utils.ViewServer;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MainActivity extends ActionBarActivity {

    public static final String ACTION_HUB_INIT_FINISHED = "org.helenalocal.intent.action.HUB_INIT_FINISHED";

    private static ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(5);
    private static ScheduledFuture buyerHubScheduledFuture;
    private static ScheduledFuture<?> itemHubScheduledFuture;
    private static ScheduledFuture<?> orderHubScheduledFuture;
    private static ScheduledFuture<?> producerHubScheduledFuture;
    private static ScheduledFuture<?> certificationHubScheduledFuture;

    private static final String LAST_SELECTED_TAB = "LastSelectedTab";
    private static final String Tag = "MainActivity";


    private int _lastSelectedTab = 0;
    private boolean _initializing = false;
    private boolean _paused;
    private BroadcastReceiver _hubInitReceiver;

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

    //***********************
    // Example from here:
    // http://android-er.blogspot.com/2013/11/android-example-imageswitcher.html
    //***********************

    Button buttonNext;
    ImageSwitcher imageSwitcher;

    Animation slide_in_left, slide_out_right;

    int imageResources[] = {
            android.R.drawable.ic_dialog_alert,
            android.R.drawable.ic_dialog_dialer,
            android.R.drawable.ic_dialog_email,
            android.R.drawable.ic_dialog_info,
            android.R.drawable.ic_dialog_map};

    int curIndex;
    //*********** End ************

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _paused = false;

        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        addTab(HomeTab.class, R.string.home_tab_text);
        addTab(MemberTab.class, R.string.member_tab_text);
        addTab(ProductTab.class, R.string.product_tab_text);
        addTab(GrowerTab.class, R.string.grower_tab_text);
        addTab(RestaurantTab.class, R.string.restaurant_tab_text);

        if (savedInstanceState != null) {
            _lastSelectedTab = savedInstanceState.getInt(LAST_SELECTED_TAB);
        }

        startInitialize();

        //***********************
        // Example from here:
        // http://android-er.blogspot.com/2013/11/android-example-imageswitcher.html
        //***********************
        buttonNext = (Button) findViewById(R.id.nextButton);
        imageSwitcher = (ImageSwitcher) findViewById(R.id.imageSwitcher);

        /*
        slide_in_left = AnimationUtils.loadAnimation(this,
                android.R.anim.slide_in_left);
        slide_out_right = AnimationUtils.loadAnimation(this,
                android.R.anim.slide_out_right);

        imageSwitcher.setInAnimation(slide_in_left);
        imageSwitcher.setOutAnimation(slide_out_right);

        imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {

            @Override
            public View makeView() {

                ImageView imageView = new ImageView(MainActivity.this);
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

                ViewGroup.LayoutParams params = new ImageSwitcher.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

                imageView.setLayoutParams(params);
                return imageView;

            }
        });

        curIndex = 0;
        imageSwitcher.setImageResource(imageResources[curIndex]);

        buttonNext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (curIndex == imageResources.length - 1) {
                    curIndex = 0;
                    imageSwitcher.setImageResource(imageResources[curIndex]);
                } else {
                    imageSwitcher.setImageResource(imageResources[++curIndex]);
                }
            }
        });
        */
        //****** End *****************
        ViewServer.get(this).addWindow(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(LAST_SELECTED_TAB, _lastSelectedTab);
    }

    @Override
    public void onResume() {
        super.onResume();

        _paused = false;

        getSupportActionBar().getTabAt(_lastSelectedTab).select();

        if (_initializing == false) {
            startHubThreads(this);
            Log.w(Tag, "Scheduled hub refreshes...");
        }

        ViewServer.get(this).setFocusedWindow(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // todo - may need to stop the HubInitService if it is running?

        _paused = true;
        _lastSelectedTab = getSupportActionBar().getSelectedTab().getPosition();

        if (_initializing == false) {
            stopHubThreads();
            Log.w(Tag, "Stopped hub refreshes...");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ViewServer.get(this).removeWindow(this);
    }

    private void startInitialize() {
        _initializing = true;
        registerHubInitReceiver();
        startService(new Intent(this, HubInitService.class));
    }

    private void registerHubInitReceiver() {
        IntentFilter filter = new IntentFilter(ACTION_HUB_INIT_FINISHED);
        filter.addCategory(Intent.CATEGORY_DEFAULT);

        _hubInitReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                onHubInitFinished();
            }
        };

        LocalBroadcastManager.getInstance(this).registerReceiver(_hubInitReceiver, filter);
    }

    private void onHubInitFinished() {
        _initializing = false;

        LocalBroadcastManager.getInstance(this).unregisterReceiver(_hubInitReceiver);
        _hubInitReceiver = null;

        if (_paused == false) {
            startHubThreads(this);
        }
    }

    public void onClick(View view) {
        AsyncTesterTask asyncTesterTask = new AsyncTesterTask(this);
        asyncTesterTask.execute(null);
   }
}