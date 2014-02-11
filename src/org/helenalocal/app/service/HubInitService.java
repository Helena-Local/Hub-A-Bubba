/*
 * Copyright (c) 2014. This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License for Helena Local Inc. All rights reseved.
 */

package org.helenalocal.app.service;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import org.helenalocal.app.MainActivity;
import org.helenalocal.base.get.CertificationHub;
import org.helenalocal.base.get.InitHub;

public class HubInitService extends IntentService {

    private static final String LogTag = "HubInitService";

    public HubInitService() {
        super("HubInitService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        new InitHub(this).run();
        new CertificationHub(this).run();

        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(MainActivity.ACTION_HUB_INIT_FINISHED);
        broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
    }
}
