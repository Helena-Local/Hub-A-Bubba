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
import org.helenalocal.base.Order;

import java.io.*;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by abbie on 1/24/14.
 */
public class OrderHub extends Hub implements Runnable {
    private static Context context;
    private static Calendar lastRefreshTS;
    private String fileName = "HL-OrderHub.csv";


    public OrderHub(Context context) {
        this.context = context;
    }

    private void parseCSV(HashMap<String, Order> myOrderMap, InputStream inputStream) throws IOException {
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
                // build Order
                Order order = new Order();
                simpleStringSplitter.setString(receiveString);
                Iterator<String> iterator = simpleStringSplitter.iterator();

                // Date	OID (Order ID)	IID (Item ID)	PID (Producer ID)	BID (Buyer ID)	Buyer Url
                if (iterator.hasNext()) {
                    String date = iterator.next();
                    if (!date.equals("")) {
                        order.setDate(date);
                    }
                }
                if (iterator.hasNext()) {
                    String orderId = iterator.next();
                    if (!orderId.equals("")) {
                        order.setOrderID(orderId);
                    }
                }
                if (iterator.hasNext()) {
                    String itemId = iterator.next();
                    if (!itemId.equals("")) {
                        order.setItemID(itemId);
                    }
                }
                if (iterator.hasNext()) {
                    String producerId = iterator.next();
                    if (!producerId.equals("")) {
                        order.setProducerID(producerId);
                    }
                }
                if (iterator.hasNext()) {
                    String buyerId = iterator.next();
                    if (!buyerId.equals("")) {
                        order.setBuyerID(buyerId);
                    }
                }
                if (iterator.hasNext()) {
                    String buyerUrl = iterator.next();
                    if (!buyerUrl.equals("")) {
                        order.setBuyerUrl(buyerUrl);
                    }
                }
                myOrderMap.put(order.getBuyerID(), order);
            }
        }
    }

    protected HashMap<String, Order> readFromFile(Context context) {
        HashMap<String, Order> myOrderMap = new HashMap<String, Order>();
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
                parseCSV(myOrderMap, inputStream);
                inputStream.close();
            }
        } catch (FileNotFoundException e) {
            Log.e(HubInit.logTag, "File  (" + fileName + ") not found: " + e.toString());
        } catch (IOException e) {
            Log.e(HubInit.logTag, "Can not read file  (" + fileName + ") : " + e.toString());
        }
        Log.w(HubInit.logTag, "Number of orders loaded: " + myOrderMap.size());
        return myOrderMap;
    }

    public HashMap<String, Order> getOrderMap() throws IOException {
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(orderHubDataUrl);
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
            Hub.orderMap = new OrderHub(context).getOrderMap();
            broadcastRefresh(context, HubType.ORDER_HUB);
            Log.w(logTag, "OrderHub().getOrderMap loaded...");
        } catch (IOException e) {
            Log.w(logTag, "OrderHub().getOrderMap couldn't be loaded...");
        }

    }

}
