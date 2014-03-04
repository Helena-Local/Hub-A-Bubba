/*
 * Copyright (c) 2014. This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License for Helena Local Inc. All rights reseved.
 */

package org.helenalocal.app;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import org.helenalocal.app.service.HubInitService;
import org.helenalocal.base.HubInit;
import org.helenalocal.base.get.*;
import org.helenalocal.utils.ImageCache;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class HubApplication extends Application {

    private static final String LogTag = "HubApplication";

    private ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(6);
    private ScheduledFuture<?> buyerHubScheduledFuture;
    private ScheduledFuture<?> itemHubScheduledFuture;
    private ScheduledFuture<?> orderHubScheduledFuture;
    private ScheduledFuture<?> producerHubScheduledFuture;
    private ScheduledFuture<?> certificationHubScheduledFuture;
    private ScheduledFuture<?> adHubScheduledFuture;


    private ImageCache _imageCache;
    private BroadcastReceiver _hubInitReceiver;
    private boolean _hubInitialized = false;
    private boolean _startHubThreads = false;

    public ImageCache getImageCache() {
        return _imageCache;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.w(LogTag, "onCreate");

        _imageCache = new ImageCache();

        startHubInitService();
    }

    private void startHubInitService() {
        IntentFilter filter = new IntentFilter(HubInitService.HUB_INIT_FINISHED);
        filter.addCategory(Intent.CATEGORY_DEFAULT);

        _hubInitReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                onHubInitFinished();
            }
        };

        LocalBroadcastManager.getInstance(this).registerReceiver(_hubInitReceiver, filter);
        startService(new Intent(this, HubInitService.class));
    }

    private void onHubInitFinished() {
        Log.w(LogTag, "onHubInitfinished");

        LocalBroadcastManager.getInstance(this).unregisterReceiver(_hubInitReceiver);
        _hubInitReceiver = null;

        _hubInitialized = true;
        if (_startHubThreads == true) {
            startHubThreads(getApplicationContext());
        }
    }

    public void startHubThreads() {
        if (_hubInitialized == false) {
            Log.w(LogTag, "startHubThreads - queueing up");
            _startHubThreads = true;
        }
        else {
            Log.w(LogTag, "startHubThreads - starting");
            startHubThreads(getApplicationContext());
        }
    }

    private void startHubThreads(Context context) {
        Log.w(LogTag, "startHubThreads exec.getQueue().size() = " + exec.getQueue().size());
        // schedule hub refreshes...
        itemHubScheduledFuture = exec.scheduleWithFixedDelay(new ItemHub(context), 0, HubInit.getItemDelay(), TimeUnit.MINUTES);
        orderHubScheduledFuture = exec.scheduleWithFixedDelay(new OrderHub(context), 0, HubInit.getOrderDelay(), TimeUnit.MINUTES);
        buyerHubScheduledFuture = exec.scheduleWithFixedDelay(new BuyerHub(context), 0, HubInit.getBuyerDelay(), TimeUnit.MINUTES);
        producerHubScheduledFuture = exec.scheduleWithFixedDelay(new ProducerHub(context), 0, HubInit.getProducerDelay(), TimeUnit.MINUTES);
        certificationHubScheduledFuture = exec.scheduleWithFixedDelay(new CertificationHub(context), 0, HubInit.getCertificateDelay(), TimeUnit.MINUTES);
        adHubScheduledFuture = exec.scheduleWithFixedDelay(new AdHub(context), 0, HubInit.getAdDelay(), TimeUnit.MINUTES);
        Log.w(LogTag, "startHubThreads exec.getQueue().size() = " + exec.getQueue().size());
    }

    public void stopHubThreads() {
        _startHubThreads = false;

        if (_hubInitialized == true) {
            Log.w(LogTag, "stopHubThreads exec.getQueue().size() = " + exec.getQueue().size());
            if (exec.getQueue().size() != 0) {
                buyerHubScheduledFuture.cancel(false);
                itemHubScheduledFuture.cancel(false);
                orderHubScheduledFuture.cancel(false);
                producerHubScheduledFuture.cancel(false);
                certificationHubScheduledFuture.cancel(false);
                adHubScheduledFuture.cancel(false);
            }
            exec.shutdownNow();
            exec = null;
            exec = new ScheduledThreadPoolExecutor(4);
            Log.w(LogTag, "stopHubThreads exec.getQueue().size() = " + exec.getQueue().size());
        }
    }
}
