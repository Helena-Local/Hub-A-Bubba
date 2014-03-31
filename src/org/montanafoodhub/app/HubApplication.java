/*
 * Copyright (c) 2014. This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License for Helena Local Inc. All rights reseved.
 */

package org.montanafoodhub.app;

import android.app.Application;
import android.content.*;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import org.montanafoodhub.app.service.HubInitService;
import org.montanafoodhub.base.HubInit;
import org.montanafoodhub.base.get.*;
import org.montanafoodhub.utils.ImageCache;

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
    private boolean _firstAppRun = true;

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

        // setup the broadcast receiver for when the service has finished
        IntentFilter filter = new IntentFilter(HubInitService.HUB_INIT_FINISHED);
        filter.addCategory(Intent.CATEGORY_DEFAULT);

        _hubInitReceiver = getHubInitReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(_hubInitReceiver, filter);

        // start the service
        SharedPreferences prefs = getApplicationContext().getSharedPreferences(Preferences.File, Context.MODE_PRIVATE);
        _firstAppRun = prefs.getBoolean(Preferences.FIRST_RUN_AFTER_INSTALL, true);

        Intent startIntent = new Intent(this, HubInitService.class);
        startIntent.putExtra(HubInitService.EXTRA_APP_FIRST_RUN, _firstAppRun);
        startService(startIntent);
    }

    private BroadcastReceiver getHubInitReceiver() {
        return new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                onHubInitFinished();
            }
        };
    }

    private void onHubInitFinished() {
        Log.w(LogTag, "onHubInitfinished");

        LocalBroadcastManager.getInstance(this).unregisterReceiver(_hubInitReceiver);
        _hubInitReceiver = null;

        _hubInitialized = true;
        if (_startHubThreads == true) {
            startHubThreads(getApplicationContext());
        }

        if (_firstAppRun == true) {
            Log.w(LogTag, "Updating SharedPreferences");

            _firstAppRun = false;
            SharedPreferences prefs = getApplicationContext().getSharedPreferences(Preferences.File, Context.MODE_PRIVATE);
            SharedPreferences.Editor prefEdit = prefs.edit();
            prefEdit.putBoolean(Preferences.FIRST_RUN_AFTER_INSTALL, false);
            prefEdit.apply();
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
        Log.w(LogTag, "startHubThreads - firstAppRun: " + _firstAppRun);
        Log.w(LogTag, "startHubThreads exec.getQueue().size() = " + exec.getQueue().size());

        // if this was the first run after installation the data was just loaded and no need to load it immediately

        long initialDelay = (_firstAppRun == false) ? 0 : HubInit.getItemDelay();
        itemHubScheduledFuture = exec.scheduleWithFixedDelay(new ItemHub(context), initialDelay, HubInit.getItemDelay(), TimeUnit.MINUTES);

        initialDelay = (_firstAppRun == false) ? 0 : HubInit.getOrderDelay();
        orderHubScheduledFuture = exec.scheduleWithFixedDelay(new OrderHub(context), initialDelay, HubInit.getOrderDelay(), TimeUnit.MINUTES);

        initialDelay = (_firstAppRun == false) ? 0 : HubInit.getBuyerDelay();
        buyerHubScheduledFuture = exec.scheduleWithFixedDelay(new BuyerHub(context), initialDelay, HubInit.getBuyerDelay(), TimeUnit.MINUTES);

        initialDelay = (_firstAppRun == false) ? 0 : HubInit.getProducerDelay();
        producerHubScheduledFuture = exec.scheduleWithFixedDelay(new ProducerHub(context), initialDelay, HubInit.getProducerDelay(), TimeUnit.MINUTES);

        initialDelay = (_firstAppRun == false) ? 0 : HubInit.getCertificateDelay();
        certificationHubScheduledFuture = exec.scheduleWithFixedDelay(new CertificationHub(context), initialDelay, HubInit.getCertificateDelay(), TimeUnit.MINUTES);

        initialDelay = (_firstAppRun == false) ? 0 : HubInit.getAdDelay();
        adHubScheduledFuture = exec.scheduleWithFixedDelay(new AdHub(context), initialDelay, HubInit.getAdDelay(), TimeUnit.MINUTES);

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
