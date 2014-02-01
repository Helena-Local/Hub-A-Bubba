package org.helenalocal.base.post;

import android.content.Context;
import android.util.Log;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.helenalocal.base.Hub;
import org.helenalocal.base.Product;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by abbie on 1/24/14.
 */
public class GrowerHub extends Hub {
    public GrowerHub() {
        setFilename("HL-GrowerHub.csv");
        setDataUrl("https://docs.google.com/forms/d/14aZGVPlxgr6-9wH6OLyZfuSH-mF6vVJjzAbFxyRaqRc/viewform");
    }

    @Override
    public List<Product> getProductList(Context context) throws IOException {
        return null;
    }

    @Override
    public void setProduct(Context context, Product product) {

        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(getDataUrl());

        List<BasicNameValuePair> results = new ArrayList<BasicNameValuePair>();
        // Farm Name
        results.add(new BasicNameValuePair("entry.614024895",product.getFarmName()));
        // Farm/Response Email Address
        results.add(new BasicNameValuePair("entry.125556965",product.getFarmEmailAddress()));
        // Product Category
        results.add(new BasicNameValuePair("entry.445217294",product.getCategory()));
        // Product Description
        results.add(new BasicNameValuePair("entry.1031627276",product.getProductDesc()));
        // Grower Agreement ID
        results.add(new BasicNameValuePair("entry.2052032218",product.getGrowerAgreementId()));
        // Unit Description
        results.add(new BasicNameValuePair("entry.365352229",product.getUnitDesc()));
        // Units Available
        results.add(new BasicNameValuePair("entry.1533229608",product.getUnitDesc()));


        // Delivery Date
        // January,July,
        results.add(new BasicNameValuePair("entry.1142116839_month",product.getUnitDesc()));
        // 1,2
        results.add(new BasicNameValuePair("entry.1142116839_day",product.getUnitDesc()));
        // 2014
        results.add(new BasicNameValuePair("entry.1142116839_year",product.getUnitDesc()));

        // Product Notes
        results.add(new BasicNameValuePair("entry.508863227",product.getNote()));

        // just some extras
        results.add(new BasicNameValuePair("pageHistory","0"));
        results.add(new BasicNameValuePair("fromEmail","kcolussi@gmail.com"));
        results.add(new BasicNameValuePair("fbzx","1608817002751623479"));

        try {
            // first try to post
            post.setEntity(new UrlEncodedFormEntity(results));
            HttpResponse response=client.execute(post);
            Log.w(Hub.BACKEND, "status line = " + response.getStatusLine());
        } catch (Exception e) {
            // email which will use smtp so no need to retry, clean up, etc.
            Log.w(Hub.BACKEND, "e = " + e.toString());
        }
    }

}
