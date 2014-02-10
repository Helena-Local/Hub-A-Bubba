/*
 * Copyright (c) 2014. This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License for Helena Local Inc. All rights reseved.
 */

package org.helenalocal.app.test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

/**
 * Created by abbie on 2/8/14.
 */
public class TestReceiver extends BroadcastReceiver {
    private static final String Tag = "TestReceiver";

    public TestReceiver(Context context) {
        IntentFilter filter = new IntentFilter();
        filter.addAction("");
        // context.registerReceiver(this, )

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Log.w(Tag, "intent.getType() = " + intent.getType());
        } catch (Exception e) {
            Log.w(Tag, "Exception : " + e);
        }
    }
}
