/*
 * Copyright (c) 2014. This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License for Helena Local Inc. All rights reseved.
 */

package org.helenalocal.base;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import org.helenalocal.base.post.GrowerHub;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;

/**
 * Created by abbie on 1/24/14.
 */
public abstract class Hub extends HubInit {

    private void broadcastActionView(Context context) {
        Intent intent = new Intent();
        intent.setType(HubType.PRODUCER_HUB.name());
        context.sendBroadcast(intent);
        Log.e(HubInit.logTag, "broadcast sent");
    }

    public static HashMap<String, Buyer> buyerMap = new HashMap<String, Buyer>();

    public static HashMap<String, Item> itemMap = new HashMap<String, Item>();

    public static HashMap<String, Order> orderMap = new HashMap<String, Order>();

    public static HashMap<String, Producer> producerMap = new HashMap<String, Producer>();

    public static HashMap<String, Certification> certificationMap = new HashMap<String, Certification>();

    public static GrowerHub growerHub = new GrowerHub();


    protected void writeToFile(Context context, String data, String fileName) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(fileName, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
            //broadcastActionView(context);
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
            //broadcastActionView(context);
        } catch (IOException e) {
            Log.e(HubInit.logTag, "File (" + fileName + ") write failed: " + e.toString());
        }
    }

}
