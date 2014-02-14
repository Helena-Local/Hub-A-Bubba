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
import org.helenalocal.base.Hub;
import org.helenalocal.base.HubInit;
import org.helenalocal.base.Item;

import java.io.*;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by abbie on 1/24/14.
 */
public class ItemHub extends Hub implements Runnable {
    private static Context context;
    private static Calendar lastRefreshTS;
    private String fileName = "HL-ItemHub.csv";


    public ItemHub(Context context) {
        this.context = context;
    }

    private void parseCSV(HashMap<String, Item> myItemMap, InputStream inputStream) throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        TextUtils.SimpleStringSplitter simpleStringSplitter = new TextUtils.SimpleStringSplitter(',');
        String receiveString = "";
        boolean firstTime = true;
        while ((receiveString = bufferedReader.readLine()) != null) {
            if (firstTime) {
                // remove header
                firstTime = false;
            } else {
                // build item
                Item item = new Item();
                simpleStringSplitter.setString(receiveString);
                Iterator<String> iterator = simpleStringSplitter.iterator();

                // IID (Item ID)	PID (Producer ID)	InCsaThisWeek	Category	Product Description	Product Url	Product Image Url	Units Available	Units Desc	Unit Price	Notes
                if (iterator.hasNext()) {
                    String itemId = iterator.next();
                    if (!itemId.equals("")) {
                        item.setIID(itemId);
                    }
                }
                if (iterator.hasNext()) {
                    String producerId = iterator.next();
                    if (!producerId.equals("")) {
                        item.setPID(producerId);
                    }
                }
                if (iterator.hasNext()) {
                    String inCsaThisWeek = iterator.next();
                    if ((!inCsaThisWeek.equals("")) && (inCsaThisWeek.equals("Y"))) {
                        item.setInCsaThisWeek(true);
                    }
                }
                if (iterator.hasNext()) {
                    String category = iterator.next();
                    if (!category.equals("")) {
                        item.setCategory(category);
                    }
                }
                if (iterator.hasNext()) {
                    String productDesc = iterator.next();
                    if (!productDesc.equals("")) {
                        item.setProductDesc(productDesc);
                    }
                }
                if (iterator.hasNext()) {
                    String productUrl = iterator.next();
                    if (!productUrl.equals("")) {
                        item.setProductUrl(productUrl);
                    }
                }
                if (iterator.hasNext()) {
                    String productImageUrl = iterator.next();
                    if (!productImageUrl.equals("")) {
                        item.setProductImageUrl(productImageUrl);
                    }
                }
                if (iterator.hasNext()) {
                    String unitsAvailable = iterator.next();
                    if (!unitsAvailable.equals("")) {
                        item.setUnitsAvailable(Integer.valueOf(unitsAvailable));
                    }
                }
                if (iterator.hasNext()) {
                    String unitDesc = iterator.next();
                    if (!unitDesc.equals("")) {
                        item.setUnitDesc(unitDesc);
                    }
                }
                if (iterator.hasNext()) {
                    String unitPrice = iterator.next();
                    if (!unitPrice.equals("")) {
                        Object o = unitPrice.replace("$", "");
                        item.setUnitPrice(Double.parseDouble(o.toString()));
                    }
                }
                if (iterator.hasNext()) {
                    String note = iterator.next();
                    if (!note.equals("")) {
                        item.setNote(note);
                    }
                }
                myItemMap.put(item.getIID(), item);
            }
        }
    }

    protected HashMap<String, Item> readFromFile(Context context) {
        HashMap<String, Item> myItemMap = new HashMap<String, Item>();
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
                parseCSV(myItemMap, inputStream);
                inputStream.close();
            }
        } catch (FileNotFoundException e) {
            Log.e(HubInit.logTag, "File  (" + fileName + ") not found: " + e.toString());
        } catch (IOException e) {
            Log.e(HubInit.logTag, "Can not read file  (" + fileName + ") : " + e.toString());
        }
        Log.w(HubInit.logTag, "Number of items loaded: " + myItemMap.size());
        return myItemMap;
    }

    public HashMap<String, Item> getItemMap() throws IOException {
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(itemHubDataUrl);
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
            Hub.itemMap = new ItemHub(context).getItemMap();
            Log.w(logTag, "ItemHub().getItemMap loaded...");
        } catch (IOException e) {
            Log.w(logTag, "ItemHub().getItemMap couldn't be loaded...");
        }

    }

}
