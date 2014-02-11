/*
 * Copyright (c) 2014. This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License for Helena Local Inc. All rights reseved.
 */

package org.helenalocal.base.get;

import android.content.Context;
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

import java.net.URLEncoder;
import java.text.SimpleDateFormat;

/**
 * Created by abbie on 1/24/14.
 */
public class GrowerHub extends Hub {
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public GrowerHub() {
        logTag = HubType.GROWER_HUB.name();
    }

    public void setItem(Context context, Producer producer, Item item, String growerAgreementId) throws Exception {
        HttpClient client = new DefaultHttpClient();
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
}
