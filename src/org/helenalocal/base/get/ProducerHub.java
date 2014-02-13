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
import org.apache.http.util.EntityUtils;
import org.helenalocal.base.Hub;
import org.helenalocal.base.HubInit;
import org.helenalocal.base.Item;
import org.helenalocal.base.Producer;

import java.io.*;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by abbie on 1/24/14.
 */
public class ProducerHub extends Hub implements Runnable {
    private static Context context;
    private static Calendar lastRefreshTS;
    private String fileName = "HL-ProducerHub.csv";


    public ProducerHub(Context context) {
        this.context = context;
        logTag = HubType.PRODUCER_HUB.name();
    }

    private void parseCSV(HashMap<String, Producer> myProducerMap, InputStream inputStream) throws IOException {
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
                Producer producer = new Producer();
                simpleStringSplitter.setString(receiveString);
                Iterator<String> iterator = simpleStringSplitter.iterator();

                // PID (Producer ID)	Name	ContactEmail	WebsiteUrl	PhotoUrl	Location	Certification String List Quote
                if (iterator.hasNext()) {
                    String producerId = iterator.next();
                    if (!producerId.equals("")) {
                        producer.setPID(producerId);
                    }
                }
                if (iterator.hasNext()) {
                    String name = iterator.next();
                    if (!name.equals("")) {
                        producer.setName(name);
                    }
                }
                if (iterator.hasNext()) {
                    String contactEmail = iterator.next();
                    if (!contactEmail.equals("")) {
                        producer.setContactEmail(contactEmail);
                    }
                }
                if (iterator.hasNext()) {
                    String websiteUrl = iterator.next();
                    if (!websiteUrl.equals("")) {
                        producer.setWebsiteUrl(websiteUrl);
                    }
                }
                if (iterator.hasNext()) {
                    String photoUrl = iterator.next();
                    if (!photoUrl.equals("")) {
                        producer.setPhotoUrl(photoUrl);
                    }
                }
                if (iterator.hasNext()) {
                    String location = iterator.next();
                    if (!location.equals("")) {
                        producer.setLocation(location);
                    }
                }
                if (iterator.hasNext()) {
                    String certificationStr = iterator.next();
                    if (!certificationStr.equals("")) {
                        producer.setCertifications(CertificationHub.buildCertificationList(certificationStr));
                    }
                }
                if (iterator.hasNext()) {
                    String quote = iterator.next();
                    if (!quote.equals("")) {
                        producer.setQuote(quote);
                    }
                }
                if (iterator.hasNext()) {
                    String iconUrl = iterator.next();
                    if (!iconUrl.equals("")) {
                        producer.setIconUrl(iconUrl);
                    }
                }
                myProducerMap.put(producer.getPID(), producer);
            }
        }
    }

    protected HashMap<String, Producer> readFromFile(Context context) {
        HashMap<String, Producer> myProducerMap = new HashMap<String, Producer>();
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
                parseCSV(myProducerMap, inputStream);
                inputStream.close();
            }
        } catch (FileNotFoundException e) {
            Log.e(HubInit.logTag, "File  (" + fileName + ") not found: " + e.toString());
        } catch (IOException e) {
            Log.e(HubInit.logTag, "Can not read file  (" + fileName + ") : " + e.toString());
        }
        Log.w(HubInit.logTag, "Number of producers loaded: " + myProducerMap.size());
        return myProducerMap;
    }

    public HashMap<String, Producer> getProducerMap() throws IOException {
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(producerHubDataUrl);
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

    // This is used for submitting producer availability
    public void setItem(Context context, Producer producer, Item item, String growerAgreementId) throws Exception {
        HttpClient client = new DefaultHttpClient();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String deliveryDateStr = dateFormat.format(item.getDeliveryDate().getTime());
        String urlSuffix = "entry.614024895=" + URLEncoder.encode(producer.getName(), "UTF-8") + "&entry.125556965=" + URLEncoder.encode(producer.getContactEmail(), "UTF-8") +
                "&entry.445217294=" + URLEncoder.encode(item.getCategory(), "UTF-8") + "&entry.1031627276=" + URLEncoder.encode(item.getProductDesc(), "UTF-8") + "&entry.2052032218=" + URLEncoder.encode(growerAgreementId, "UTF-8") +
                "&entry.365352229=" + URLEncoder.encode(item.getUnitDesc(), "UTF-8") + "&entry.1533229608=" + URLEncoder.encode(item.getUnitsAvailable().toString(), "UTF-8") + "&entry.1142116839=" + URLEncoder.encode(deliveryDateStr, "UTF-8") +
                "&entry.508863227=" + URLEncoder.encode(item.getNote(), "UTF-8");

        HttpGet get = new HttpGet(growerHubDataUrl + urlSuffix);
        Log.w(HubInit.logTag, "get.getURI() = " + get.getURI());
        HttpResponse response = client.execute(get);
        Log.w(HubInit.logTag, "status line = " + response.getStatusLine());
        String responseBody = EntityUtils.toString(response.getEntity());
        Log.w(HubInit.logTag, responseBody);
        if (responseBody.contains("Your response has been recorded.")) {
            Log.w(HubInit.logTag, "Your response has been recorded.");
        } else {
            Log.w(HubInit.logTag, "Record failed validation!");
            throw new Exception("Record failed data validation on the server side.");
        }
    }

    public static Calendar getLastRefreshTS() {
        return lastRefreshTS;
    }

    @Override
    public void run() {
        try {
            Hub.producerMap = new ProducerHub(context).getProducerMap();
            Log.w(logTag, "ProducerHub().getProducerMap loaded...");
        } catch (IOException e) {
            Log.w(logTag, "ProducerHub().getProducerMap couldn't be loaded...");
        }

    }
}
