/*
 * Copyright (c) 2014. This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License for Helena Local Inc. All rights reseved.
 */

package org.helenalocal.base.post;

import android.content.Context;
import android.util.Log;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.helenalocal.base.Hub;
import org.helenalocal.base.HubInit;
import org.helenalocal.base.Item;
import org.helenalocal.base.Producer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by abbie on 1/24/14.
 */
public class GrowerHub extends Hub {

    public GrowerHub() {
        logTag = HubType.GROWER_HUB.name();
    }

    public void setItem(Context context, Producer producer, Item item, String growerAgreementId) throws Exception {

        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(growerHubDataUrl);

        List<BasicNameValuePair> nvp = new ArrayList<BasicNameValuePair>();
        // Producer Name
        nvp.add(new BasicNameValuePair("entry.614024895", producer.getName()));
        // Producer/Response Email Address
        nvp.add(new BasicNameValuePair("entry.125556965", producer.getContactEmail()));
        // Item Category
        nvp.add(new BasicNameValuePair("entry.445217294", item.getCategory()));
        // Item Description
        nvp.add(new BasicNameValuePair("entry.1031627276", item.getProductDesc()));
        // Grower Agreement ID
        nvp.add(new BasicNameValuePair("entry.2052032218", growerAgreementId));
        // Unit Description
        nvp.add(new BasicNameValuePair("entry.365352229", item.getUnitDesc()));
        // Units Available
        nvp.add(new BasicNameValuePair("entry.1533229608", item.getUnitsAvailable().toString()));

        // Delivery Date

        // single digit
        nvp.add(new BasicNameValuePair("entry.1142116839_month", new SimpleDateFormat("M").format(item.getDeliveryDate().getTime()).trim()));
        // 1,2
        nvp.add(new BasicNameValuePair("entry.1142116839_day", new SimpleDateFormat("d").format(item.getDeliveryDate().getTime()).trim()));
        // 2014
        nvp.add(new BasicNameValuePair("entry.1142116839_year", new SimpleDateFormat("y").format(item.getDeliveryDate().getTime()).trim()));

        // Item Notes
        nvp.add(new BasicNameValuePair("entry.508863227", item.getNote()));

        // just some extras
        nvp.add(new BasicNameValuePair("pageHistory", "0"));
        nvp.add(new BasicNameValuePair("fromEmail", "kcolussi@gmail.com"));
        nvp.add(new BasicNameValuePair("fbzx", "1608817002751623479"));

        // first try to post
        //TODO kevin -- Bug here!
        post.setEntity(new UrlEncodedFormEntity(nvp, HTTP.UTF_8));
        HttpResponse response = client.execute(post);
        Log.w(HubInit.logTag, "status line = " + response.getStatusLine());
        if (EntityUtils.toString(response.getEntity()).contains("Your response has been recorded.")) {
            Log.w(HubInit.logTag, "Your response has been recorded...");
        } else {
            Log.w(HubInit.logTag, "Record failed validation!");
            throw new Exception("Record failed data validation on the server side...");
        }
    }
}
