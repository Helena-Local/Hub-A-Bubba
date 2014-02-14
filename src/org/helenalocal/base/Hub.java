/*
 * Copyright (c) 2014. This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License for Helena Local Inc. All rights reseved.
 */

package org.helenalocal.base;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import org.helenalocal.base.get.ProducerHub;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by abbie on 1/24/14.
 */
public abstract class Hub extends HubInit {

    public static final String HUB_DATA_REFRESH = "org.helenalocal.intent.action.HUB_DATA_REFRESH";
    public static final String EXTRA_HUB_TYPE = "org.helenalocal.extra.hubtype";

    public static HashMap<String, Buyer> buyerMap = new HashMap<String, Buyer>();

    public static HashMap<String, Item> itemMap = new HashMap<String, Item>();

    public static List<Order> orderArr = new ArrayList<Order>();

    public static HashMap<String, Producer> producerMap = new HashMap<String, Producer>();

    public static HashMap<String, Certification> certificationMap = new HashMap<String, Certification>();

    public static List<Ad> adArr = new ArrayList<Ad>();

    public static ProducerHub producerHub = new ProducerHub(null);

    protected void writeToFile(Context context, String data, String fileName) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(fileName, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e(HubInit.logTag, "File (" + fileName + ") write failed: " + e.toString());
        }
    }

    protected void writeToFile(Context context, BufferedReader rd, String fileName) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(fileName, Context.MODE_PRIVATE));
            String line = "";
            while ((line = rd.readLine()) != null) {
                outputStreamWriter.write(line + '\n');
            }
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e(HubInit.logTag, "File (" + fileName + ") write failed: " + e.toString());
        }
    }

    protected void broadcastRefresh(Context context, HubType type) {
        Intent intent = new Intent();
        intent.setAction(HUB_DATA_REFRESH);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.putExtra(EXTRA_HUB_TYPE, type);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

        Log.w(HubInit.logTag, "broadcast sent");
    }
}
