/*
 * Copyright (c) 2014. This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License for Helena Local Inc. All rights reseved.
 */

package org.montanafoodhub.base.get;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.montanafoodhub.base.Buyer;
import org.montanafoodhub.base.Hub;
import org.montanafoodhub.base.HubInit;

import java.io.*;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by abbie on 1/24/14.
 */
public class BuyerHub extends Hub implements Runnable {
    private static boolean isFirstLoad = true;
    private static Context context;
    private static Calendar lastRefreshTS;
    private String fileName = "HL-BuyerHub.csv";


    public BuyerHub(Context context) {
        this.context = context;
    }

    private void parseCSV(HashMap<String, Buyer> myBuyerMap, InputStream inputStream) throws IOException {
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
                Buyer buyer = new Buyer();
                simpleStringSplitter.setString(receiveString);
                Iterator<String> iterator = simpleStringSplitter.iterator();

                // BID (Buyer ID)	Name	ContactEmail	Hours	Phone	WebsiteUrl	PhotoUrl	Location	Service Level (0-off, 1-on, 2-premium)	Certification ID List
                if (iterator.hasNext()) {
                    String buyerId = iterator.next();
                    if (!buyerId.equals("")) {
                        buyer.setBID(buyerId);
                    }
                }
                if (iterator.hasNext()) {
                    String name = iterator.next();
                    if (!name.equals("")) {
                        buyer.setName(name);
                    }
                }
                if (iterator.hasNext()) {
                    String contactEmail = iterator.next();
                    if (!contactEmail.equals("")) {
                        buyer.setContactEmail(contactEmail);
                    }
                }
                if (iterator.hasNext()) {
                    String hours = iterator.next();
                    if (!hours.equals("")) {
                        buyer.setHours(hours);
                    }
                }
                if (iterator.hasNext()) {
                    String phone = iterator.next();
                    if (!phone.equals("")) {
                        buyer.setPhone(phone);
                    }
                }
                if (iterator.hasNext()) {
                    String websiteUrl = iterator.next();
                    if (!websiteUrl.equals("")) {
                        buyer.setWebsiteUrl(websiteUrl);
                    }
                }
                if (iterator.hasNext()) {
                    String photoUrl = iterator.next();
                    if (!photoUrl.equals("")) {
                        buyer.setPhotoUrl(photoUrl);
                    }
                }
                if (iterator.hasNext()) {
                    String iconUrl = iterator.next();
                    if (!iconUrl.equals("")) {
                        buyer.setIconUrl(iconUrl);
                    }
                }
                if (iterator.hasNext()) {
                    String location = iterator.next();
                    if (!location.equals("")) {
                        buyer.setLocation(location);
                    }
                }
                if (iterator.hasNext()) {
                    String locationDisplay = iterator.next();
                    if (!locationDisplay.equals("")) {
                        buyer.setLocationDisplay(locationDisplay);
                    }
                }
                if (iterator.hasNext()) {
                    String certificationStr = iterator.next();
                    if (!certificationStr.equals("")) {
                        buyer.setCertifications(CertificationHub.buildCertificationList(certificationStr));
                    }
                }
                if (iterator.hasNext()) {
                    String quote = iterator.next();
                    if (!quote.equals("")) {
                        buyer.setQuote(quote);
                    }
                }
                if (iterator.hasNext()) {
                    String buyerType = iterator.next();
                    if (!buyerType.equals("")) {
                        try {
                            // this should help validate the csv file format we are reading.
                            buyer.setBuyerType(Integer.valueOf(buyerType.trim()));;
                        } catch (Exception e) {
                            Log.e(HubInit.logTag, "SKIPPING - ERROR in csv buyer tab this line: >" + receiveString + "<");
                        }
                    }
                }
                myBuyerMap.put(buyer.getBID(), buyer);
            }
        }
    }

    protected HashMap<String, Buyer> readFromFile(Context context) {
        HashMap<String, Buyer> myBuyerMap = new HashMap<String, Buyer>();
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
                parseCSV(myBuyerMap, inputStream);
            }
            inputStream.close();
        } catch (FileNotFoundException e) {
            Log.e(HubInit.logTag, "File  (" + fileName + ") not found: " + e.toString());
        } catch (IOException e) {
            Log.e(HubInit.logTag, "Can not read file  (" + fileName + ") : " + e.toString());
        }
        Log.w(HubInit.logTag, "Number of buyers loaded: " + myBuyerMap.size());
        return myBuyerMap;
    }

    public HashMap<String, Buyer> getBuyerMap() throws IOException {
        HashMap<String, Buyer> out = new HashMap<String, Buyer>();
        try {
            if (isFirstLoad) {
                // try to load disk file first.
                out = readFromFile(context);
            } else {
                out = loadFromServer(context);
            }
        } catch (IOException ie) {
            Log.w(logTag, "BuyerHub().getBuyerMap couldn't be loaded...");
        } finally {
            isFirstLoad = false;
        }
        return out;
    }

    protected HashMap<String, Buyer> loadFromServer(Context context) throws IOException {
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(buyerHubDataUrl);
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

    // This is used as one component in determining restaurant rank/sort order in the display list
    public static int getOrderItemCnt(String bid) {
        int out = 0;
        for (int j = 0; j < Hub.orderArr.size(); j++) {
            if (Hub.orderArr.get(j).getBuyerID().equalsIgnoreCase(bid)) {
                out++;
            }
        }
        return out;
    }

    @Override
    public void run() {
        try {
            Hub.buyerMap = new BuyerHub(context).getBuyerMap();
            broadcastRefresh(context, HubType.BUYER_HUB);
            Log.w(logTag, "BuyerHub().getBuyerMap loaded...");
        } catch (IOException e) {
            Log.w(logTag, "BuyerHub().getBuyerMap couldn't be loaded...");
        }
    }

    @Override
    public void refresh() {
        try {
            Hub.buyerMap = loadFromServer(context);
            broadcastRefresh(context, HubType.BUYER_HUB);
            Log.w(logTag, "BuyerHub().refresh loaded...");
        } catch (IOException e) {
            Log.w(logTag, "BuyerHub().refresh couldn't be loaded...");
        }
    }
}
