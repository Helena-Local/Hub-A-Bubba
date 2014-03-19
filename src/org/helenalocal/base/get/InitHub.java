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
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.helenalocal.base.Hub;
import org.helenalocal.base.HubInit;

import java.io.*;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;

/**
 * Created by abbie on 1/24/14.
 */
public class InitHub extends Hub implements Runnable {
    private static Context context;
    private static Calendar lastRefreshTS;
    private String fileName = "HL-InitHub.csv";
    private int connectionTimeOutSec = 5;
    private int socketTimeoutSec = 5;


    public InitHub(Context context) {
        this.context = context;
        final HttpParams httpParameters = new DefaultHttpClient().getParams();
        HttpConnectionParams.setConnectionTimeout(httpParameters, connectionTimeOutSec * 1000);
        HttpConnectionParams.setSoTimeout(httpParameters, socketTimeoutSec * 1000);
    }

    private void parseCSV(InputStream inputStream) throws IOException {
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
                simpleStringSplitter.setString(receiveString);
                Iterator<String> iterator = simpleStringSplitter.iterator();

                // Hub Name	Data Version	Grower Hub Data URL
                // Producer Hub Data Url	Order Hub Data Url
                // Item Hub Data Url	Buyer Hub Data Url
                // Hub Email To	Hub Email Subject	Buyer Delay
                // Item Delay	Order Delay	Producer Delay	Data Version Notes
                Log.w(HubInit.logTag, "+++++ Start init +++++");
                if (iterator.hasNext()) {
                    String hubName = iterator.next();
                    if (!hubName.equals("")) {
                        HubInit.setHubName(hubName);
                    }
                    Log.w(HubInit.logTag, "hubName = " + HubInit.getHubName());
                }
                if (iterator.hasNext()) {
                    String dataVersion = iterator.next();
                    if (!dataVersion.equals("")) {
                        HubInit.setDataVersion(dataVersion);
                    }
                    Log.w(HubInit.logTag, "dataVersion = " + HubInit.getDataVersion());
                }
                if (iterator.hasNext()) {
                    String growerHubDataUrl = iterator.next();
                    if (!growerHubDataUrl.equals("")) {
                        HubInit.setGrowerHubDataUrl(growerHubDataUrl);
                    }
                    Log.w(HubInit.logTag, "growerHubDataUrl = " + HubInit.getGrowerHubDataUrl());
                }
                if (iterator.hasNext()) {
                    String producerHubDataUrl = iterator.next();
                    if (!producerHubDataUrl.equals("")) {
                        HubInit.setProducerHubDataUrl(producerHubDataUrl);
                    }
                    Log.w(HubInit.logTag, "producerHubDataUrl = " + HubInit.getProducerHubDataUrl());
                }
                if (iterator.hasNext()) {
                    String orderHubDataUrl = iterator.next();
                    if (!orderHubDataUrl.equals("")) {
                        HubInit.setOrderHubDataUrl(orderHubDataUrl);
                    }
                    Log.w(HubInit.logTag, "orderHubDataUrl = " + HubInit.getOrderHubDataUrl());
                }
                if (iterator.hasNext()) {
                    String itemHubDataUrl = iterator.next();
                    if (!itemHubDataUrl.equals("")) {
                        HubInit.setItemHubDataUrl(itemHubDataUrl);
                    }
                    Log.w(HubInit.logTag, "itemHubDataUrl = " + HubInit.getInitHubDataUrl());
                }
                if (iterator.hasNext()) {
                    String buyerHubDataUrl = iterator.next();
                    if (!buyerHubDataUrl.equals("")) {
                        HubInit.setBuyerHubDataUrl(buyerHubDataUrl);
                    }
                    Log.w(HubInit.logTag, "buyerHubDataUrl = " + HubInit.getBuyerHubDataUrl());
                }
                if (iterator.hasNext()) {
                    String certificationHubDataUrl = iterator.next();
                    if (!certificationHubDataUrl.equals("")) {
                        HubInit.setCertificationHubDataUrl(certificationHubDataUrl);
                    }
                    Log.w(HubInit.logTag, "certificationHubDataUrl = " + HubInit.getCertificationHubDataUrl());
                }
                if (iterator.hasNext()) {
                    String adHubDataUrl = iterator.next();
                    if (!adHubDataUrl.equals("")) {
                        HubInit.setAdHubDataUrl(adHubDataUrl);
                    }
                    Log.w(HubInit.logTag, "adHubDataUrl = " + HubInit.getAdHubDataUrl());
                }
                if (iterator.hasNext()) {
                    String hubEmailTo = iterator.next();
                    if (!hubEmailTo.equals("")) {
                        HubInit.setHubEmailTo(hubEmailTo);
                    }
                    Log.w(HubInit.logTag, "hubEmailTo = " + HubInit.getHubEmailTo());
                }
                if (iterator.hasNext()) {
                    String hubEmailSubject = iterator.next();
                    if (!hubEmailSubject.equals("")) {
                        HubInit.setHubEmailSubject(hubEmailSubject);
                    }
                    Log.w(HubInit.logTag, "hubEmailSubject = " + HubInit.getHubEmailSubject());
                }
                if (iterator.hasNext()) {
                    String buyerDelay = iterator.next();
                    if (!buyerDelay.equals("")) {
                        HubInit.setBuyerDelay(Long.parseLong(buyerDelay.trim()));
                    }
                    Log.w(HubInit.logTag, "buyerDelay = " + HubInit.getBuyerDelay());
                }
                if (iterator.hasNext()) {
                    String itemDelay = iterator.next();
                    if (!itemDelay.equals("")) {
                        HubInit.setItemDelay(Long.parseLong(itemDelay.trim()));
                    }
                    Log.w(HubInit.logTag, "itemDelay = " + HubInit.getItemDelay());
                }
                if (iterator.hasNext()) {
                    String orderDelay = iterator.next();
                    if (!orderDelay.equals("")) {
                        HubInit.setOrderDelay(Long.parseLong(orderDelay.trim()));
                    }
                    Log.w(HubInit.logTag, "orderDelay = " + HubInit.getOrderDelay());
                }
                if (iterator.hasNext()) {
                    String producerDelay = iterator.next();
                    if (!producerDelay.equals("")) {
                        HubInit.setProducerDelay(Long.parseLong(producerDelay.trim()));
                    }
                    Log.w(HubInit.logTag, "producerDelay = " + HubInit.getProducerDelay());
                }
                if (iterator.hasNext()) {
                    String certificateDelay = iterator.next();
                    if (!certificateDelay.equals("")) {
                        HubInit.setCertificateDelay(Long.parseLong(certificateDelay.trim()));
                    }
                    Log.w(HubInit.logTag, "certificateDelay = " + HubInit.getCertificateDelay());
                }
                if (iterator.hasNext()) {
                    String adDelay = iterator.next();
                    if (!adDelay.equals("")) {
                        HubInit.setAdDelay(Long.parseLong(adDelay.trim()));
                    }
                    Log.w(HubInit.logTag, "adDelay = " + HubInit.getAdDelay());
                }
                if (iterator.hasNext()) {
                    String adPlayRate = iterator.next();
                    if (!adPlayRate.equals("")) {
                        HubInit.setAdPlayRate(Long.parseLong(adPlayRate.trim()));
                    }
                    Log.w(HubInit.logTag, "adPlayRate = " + HubInit.getAdDelay());
                }
                if (iterator.hasNext()) {
                    String dataVersionNotes = iterator.next();
                    if (!dataVersionNotes.equals("")) {
                        HubInit.setDataVersionNotes(dataVersionNotes);
                    }
                    Log.w(HubInit.logTag, "dataVersionNotes = " + HubInit.getDataVersionNotes());
                }
                Log.w(HubInit.logTag, "++++++ End init ++++++");
            }
        }
    }

    protected void readFromFile(Context context) {
        try {
            // getItem the time the file was last changed here
            File myFile = new File(context.getFilesDir() + "/" + fileName);
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            String lastRefreshTSStr = sdf.format(myFile.lastModified());
            Log.w(HubInit.logTag, "Using file (" + fileName + ") last modified on : " + lastRefreshTSStr);
            lastRefreshTS = sdf.getCalendar();

            // *** last line in the init wins!!!
            InputStream inputStream = context.openFileInput(fileName);
            if (inputStream != null) {
                parseCSV(inputStream);
            }
            inputStream.close();
        } catch (FileNotFoundException e) {
            Log.e(HubInit.logTag, "File  (" + fileName + ") not found: " + e.toString());
        } catch (IOException e) {
            Log.e(HubInit.logTag, "Can not read file  (" + fileName + ") : " + e.toString());
        }
        Log.w(HubInit.logTag, "Hub name initialized to: " + HubInit.getHubName());
    }

    public void getInitHub() throws IOException {
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(initHubDataUrl);
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
        readFromFile(context);
    }

    public static Calendar getLastRefreshTS() {
        return lastRefreshTS;
    }

    @Override
    public void run() {
        try {
            new InitHub(context).getInitHub();
            broadcastRefresh(context, HubType.INIT_HUB);
            Log.w(logTag, "InitHub().getInitHub loaded...");
        } catch (IOException e) {
            Log.w(logTag, "InitHub().getInitHub couldn't be loaded...");
        }
    }

    @Override
    public void refresh() {
        // since we always load our data from the server simply call run() for now.
        run();
    }
}
