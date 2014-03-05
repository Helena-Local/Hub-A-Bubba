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
import org.helenalocal.base.Certification;
import org.helenalocal.base.Hub;
import org.helenalocal.base.HubInit;

import java.io.*;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by abbie on 1/24/14.
 */
public class CertificationHub extends Hub implements Runnable {
    private static boolean isFirstLoad = true;
    private static Context context;
    private static Calendar lastRefreshTS;
    private String fileName = "HL-CertificationHub.csv";


    public CertificationHub(Context context) {
        this.context = context;
    }

    private void parseCSV(HashMap<String, Certification> myCertificationMap, InputStream inputStream) throws IOException {
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
                Certification certification = new Certification();
                simpleStringSplitter.setString(receiveString);
                Iterator<String> iterator = simpleStringSplitter.iterator();
                // Certification ID List	Display Name	Website URL
                if (iterator.hasNext()) {
                    String cid = iterator.next();
                    if (!cid.equals("")) {
                        certification.setCID(cid);
                    }
                }
                if (iterator.hasNext()) {
                    String displayName = iterator.next();
                    if (!displayName.equals("")) {
                        certification.setDisplayName(displayName);
                    }
                }
                if (iterator.hasNext()) {
                    String websiteUrl = iterator.next();
                    if (!websiteUrl.equals("")) {
                        certification.setWebsiteUrl(websiteUrl);
                    }
                }
                if (iterator.hasNext()) {
                    String iconUrl = iterator.next();
                    if (!iconUrl.equals("")) {
                        certification.setIconUrl(iconUrl);
                    }
                }
                myCertificationMap.put(certification.getCID(), certification);
            }
        }
    }

    protected HashMap<String, Certification> readFromFile(Context context) {
        HashMap<String, Certification> myCertificationMap = new HashMap<String, Certification>();
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
                parseCSV(myCertificationMap, inputStream);
            }
            inputStream.close();
        } catch (FileNotFoundException e) {
            Log.e(HubInit.logTag, "File  (" + fileName + ") not found: " + e.toString());
        } catch (IOException e) {
            Log.e(HubInit.logTag, "Can not read file  (" + fileName + ") : " + e.toString());
        }
        Log.w(HubInit.logTag, "Number of certifications loaded: " + myCertificationMap.size());
        return myCertificationMap;
    }

    public HashMap<String, Certification> getCertificationMap() throws IOException {
        HashMap<String, Certification> out;
        if (isFirstLoad) {
            // try to load disk file first.
            isFirstLoad = false;
            out = readFromFile(context);
        } else {
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet(certificationHubDataUrl);
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
            out = readFromFile(context);
        }
        return out;
    }

    public static Calendar getLastRefreshTS() {
        return lastRefreshTS;
    }

    public static List<Certification> buildCertificationList(String certificationStr) {
        ArrayList<Certification> certifications = new ArrayList<Certification>();
        // parse each certificate out
        TextUtils.SimpleStringSplitter certStringSplitter = new TextUtils.SimpleStringSplitter(';');
        certStringSplitter.setString(certificationStr);
        Iterator<String> certIterator = certStringSplitter.iterator();
        // loop over certifications
        while (certIterator.hasNext()) {
            String certLine = certIterator.next();
            if (!certLine.equals("")) {
                TextUtils.SimpleStringSplitter certUrlStringSplitter = new TextUtils.SimpleStringSplitter('~');
                certUrlStringSplitter.setString(certLine);
                Iterator<String> certPartIterator = certUrlStringSplitter.iterator();
                if (certPartIterator.hasNext()) {
                    String cid = certPartIterator.next();
                    Certification aCert = new Certification();
                    if (!cid.equals("") && Hub.certificationMap.containsKey(cid)) {
                        // loads all template values
                        aCert.setCID(Hub.certificationMap.get(cid).getCID());
                        aCert.setDisplayName(Hub.certificationMap.get(cid).getDisplayName());
                        aCert.setWebsiteUrl(Hub.certificationMap.get(cid).getWebsiteUrl());
                        aCert.setIconUrl(Hub.certificationMap.get(cid).getIconUrl());
                        // check for optional url suffix
                        if (certPartIterator.hasNext()) {
                            String urlSuffix = certPartIterator.next();
                            if (!urlSuffix.equals("")) {
                                // append suffix
                                aCert.setWebsiteUrl(aCert.getWebsiteUrl() + urlSuffix);
                            }
                        }
                    }
                    certifications.add(aCert);
                }
            }
        }
        return certifications;
    }

    @Override
    public void run() {
        try {
            Hub.certificationMap = new CertificationHub(context).getCertificationMap();
            broadcastRefresh(context, HubType.CERTIFICATION_HUB);
            Log.w(logTag, "OrderHub().getCertificationMapMap loaded...");
        } catch (IOException e) {
            Log.w(logTag, "OrderHub().getCertificationMap couldn't be loaded...");
        }

    }

}
