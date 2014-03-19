/*
 * Copyright (c) 2014. This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License for Helena Local Inc. All rights reseved.
 */

package org.helenalocal.app.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import org.helenalocal.base.get.*;

public class HubInitService extends IntentService {

    public static final String HUB_INIT_FINISHED = "org.helenalocal.intent.action.HUB_INIT_FINISHED";
    public static final String EXTRA_APP_FIRST_RUN = "org.helenalocal.extra.app_first_run";

    private static final String LogTag = "HubInitService";

    public HubInitService() {
        super("org.helenalocal.HubInitService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        boolean firstRunAfterInstall = intent.getBooleanExtra(EXTRA_APP_FIRST_RUN, true);

        if (firstRunAfterInstall == true) {
            new InitHub(this).refresh();
            new CertificationHub(this).refresh();
            new AdHub(this).refresh();
            new OrderHub(this).refresh();
            new ItemHub(this).refresh();
            new BuyerHub(this).refresh();
            new ProducerHub(this).refresh();
        }
        else {
            new InitHub(this).run();
            new CertificationHub(this).run();
            new AdHub(this).run();
            new OrderHub(this).run();
            new ItemHub(this).run();
            new BuyerHub(this).run();
            new ProducerHub(this).run();
        }


        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(HUB_INIT_FINISHED);
        broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
    }
}
