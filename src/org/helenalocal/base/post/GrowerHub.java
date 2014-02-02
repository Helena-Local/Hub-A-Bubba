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
import org.helenalocal.base.Item;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by abbie on 1/24/14.
 */
public class GrowerHub extends Hub {

    public GrowerHub() {
        logTag = "GrowerHub ";
        setFilename("HL-GrowerHub.csv");
        setDataUrl("https://docs.google.com/forms/d/14aZGVPlxgr6-9wH6OLyZfuSH-mF6vVJjzAbFxyRaqRc/formResponse");
    }

    @Override
    public List<Item> getProduct(Context context) throws IOException {
        return null;
    }

    @Override
    public void setProduct(Context context, Item item) throws Exception {

        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(getDataUrl());

        List<BasicNameValuePair> nvp = new ArrayList<BasicNameValuePair>();
        // Producer Name
        nvp.add(new BasicNameValuePair("entry.614024895", item.getProducer().getName()));
        // Producer/Response Email Address
        nvp.add(new BasicNameValuePair("entry.125556965", item.getProducer().getContactEmail()));
        // Item Category
        nvp.add(new BasicNameValuePair("entry.445217294", item.getCategory()));
        // Item Description
        nvp.add(new BasicNameValuePair("entry.1031627276", item.getProductDesc()));
        // Grower Agreement ID
        nvp.add(new BasicNameValuePair("entry.2052032218", item.getGrowerAgreementId()));
        // Unit Description
        nvp.add(new BasicNameValuePair("entry.365352229", item.getUnitDesc()));
        // Units Available
        nvp.add(new BasicNameValuePair("entry.1533229608", item.getUnitsAvailable().toString()));

        // Delivery Date

        // single digit
        nvp.add(new BasicNameValuePair("entry.1142116839_month", new SimpleDateFormat("M").format(item.getDeliveryDate().getTime()).trim()));
        // 1,2
        nvp.add(new BasicNameValuePair("entry.1142116839_day",new SimpleDateFormat("d").format(item.getDeliveryDate().getTime()).trim()));
        // 2014
        nvp.add(new BasicNameValuePair("entry.1142116839_year",new SimpleDateFormat("y").format(item.getDeliveryDate().getTime()).trim()));

        // Item Notes
        nvp.add(new BasicNameValuePair("entry.508863227", item.getNote()));

        // just some extras
        nvp.add(new BasicNameValuePair("pageHistory", "0"));
        nvp.add(new BasicNameValuePair("fromEmail", "kcolussi@gmail.com"));
        nvp.add(new BasicNameValuePair("fbzx", "1608817002751623479"));

        // first try to post
        post.setEntity(new UrlEncodedFormEntity(nvp, HTTP.UTF_8));
        HttpResponse response=client.execute(post);
        Log.w(Hub.logTag, "status line = " + response.getStatusLine());
        if (EntityUtils.toString(response.getEntity()).contains("Your response has been recorded.")) {
            Log.w(Hub.logTag,"Your response has been recorded...");
        } else {
            Log.w(Hub.logTag,"Record failed validation!");
            throw new Exception ("Record failed data validation on the server side...");
        }
    }
}
