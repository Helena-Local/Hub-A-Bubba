/*
 * Copyright (c) 2014. This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License for Helena Local Inc. All rights reseved.
 */

package org.helenalocal.base.get;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.helenalocal.base.Ad;
import org.helenalocal.base.Hub;
import org.helenalocal.base.HubInit;

import java.io.*;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by abbie on 1/24/14.
 */
public class AdHub extends Hub implements Runnable {
    private static Context context;
    private static Calendar lastRefreshTS;
    private String fileName = "HL-AdHub.csv";


    public AdHub(Context context) {
        this.context = context;
    }

    private void parseCSV(HashMap<String, Ad> myAdMap, InputStream inputStream) throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        TextUtils.SimpleStringSplitter simpleStringSplitter = new TextUtils.SimpleStringSplitter(',');
        String receiveString = "";
        boolean firstTime = true;
        int pos = 0;
        while ((receiveString = bufferedReader.readLine()) != null) {
            if (firstTime) {
                // remove header
                firstTime = false;
            } else {
                // build item
                Ad ad = new Ad();
                simpleStringSplitter.setString(receiveString);
                Iterator<String> iterator = simpleStringSplitter.iterator();

                // Image URL	BID
                if (iterator.hasNext()) {
                    String imageUrl = iterator.next();
                    if (!imageUrl.equals("")) {
                        ad.setImageUrl(imageUrl);
                    }
                }
                if (iterator.hasNext()) {
                    String bid = iterator.next();
                    if (!bid.equals("")) {
                        ad.setBID(bid);
                    }
                }
                myAdMap.put(String.valueOf(pos++), ad);
            }
        }
    }

    protected HashMap<String, Ad> readFromFile(Context context) {
        HashMap<String, Ad> myAdMap = new HashMap<String, Ad>();
        try {
            // getItem the time the file was last changed here
            File myFile = new File(context.getFilesDir() + "/" + fileName);
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            String lastRefreshTSStr = sdf.format(myFile.lastModified());
            Log.w(HubInit.logTag, "Using file (" + fileName + ") last modified on : " + lastRefreshTSStr);
            lastRefreshTS = sdf.getCalendar();

            // create products from the file here
            InputStream inputStream = context.openFileInput(fileName);
            if (inputStream != null) {
                parseCSV(myAdMap, inputStream);
                inputStream.close();
            }
        } catch (FileNotFoundException e) {
            Log.e(HubInit.logTag, "File  (" + fileName + ") not found: " + e.toString());
        } catch (IOException e) {
            Log.e(HubInit.logTag, "Can not read file  (" + fileName + ") : " + e.toString());
        }
        Log.w(HubInit.logTag, "Number of Ad loaded: " + myAdMap.size());
        return myAdMap;
    }

    public HashMap<String, Ad> getBuyerMap() throws IOException {
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(adHubDataUrl);
        try {
            // first try the net
            HttpResponse response = client.execute(request);
            Log.w(HubInit.logTag, "HTTP execute Response.getStatusLine() = " + response.getStatusLine());

            // make net version local
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            writeToFile(context, rd, fileName);
            Log.w(HubInit.logTag, "Wrote file from the net to device...");
        } catch (UnknownHostException e) {
            Log.w(HubInit.logTag, "Couldn't getItem the file from the net just using file from device... ");
        }

        // regardless of net work with file
        return readFromFile(context);
    }

    public static Calendar getLastRefreshTS() {
        return lastRefreshTS;
    }

    @Override
    public void run() {
        try {
            Hub.adMap = new AdHub(context).getBuyerMap();
            Log.w(logTag, "AdHub().getAdMap loaded...");
        } catch (IOException e) {
            Log.w(logTag, "AdHub().getAdMap couldn't be loaded...");
        }

    }
}
