/*
 * Copyright (c) 2014. This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License for Helena Local Inc. All rights reseved.
 */

package org.helenalocal.app.test;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import org.helenalocal.app.MainActivity;
import org.helenalocal.base.*;
import org.helenalocal.base.get.CertificationHub;
import org.helenalocal.base.get.GrowerHub;
import org.helenalocal.base.get.InitHub;

import java.util.ArrayList;
import java.util.Calendar;

public class AsyncTesterTask extends AsyncTask<Void, Void, Intent> {

    private static final String Tag = "AsyncTesterTask";
    private static Context _context;

    private Intent bigTest() {
        Intent email = null;

        // public Producer(String PID, String name, String contactEmail, String websiteUrl, String photoUrl, String location) {
        Producer producer = new Producer("P-2013-0", "Western Montana Growers’ Cooperative", "grower@wmgcoop.com", "http://www.wmgcoop.com/", "http://g.virbcdn.com/_f2/images/58/PageImage-524372-4680215-WMGC_WebBanner.jpg", "Arlee, MT 59821", new ArrayList<Certification>(), "This day...");

        // public Item(String IID,Producer producer,boolean inCsaThisWeek, String category, String productDesc, String productUrl, String productImageUrl, Integer unitsAvailable,
        //        String unitDesc, Double unitPrice, Calendar deliveryDate, String note) {
        Item item = new Item("I-2014-2-2-3","P-2013-0", true, "Produce", "Leeks","http://en.wikipedia.org/wiki/Leek‎",
                "http://upload.wikimedia.org/wikipedia/commons/thumb/4/43/Leek.jpg/160px-Leek.jpg",20,"10 lbs",10.01, Calendar.getInstance(),"note 1");

        // growerAgreementId only needed from UI submit... Not part of the item object.
        String growerAgreementId = "N/A";
        try {
            HubInit.setInitHubDataUrl("https://docs.google.com/spreadsheet/pub?key=0AtzLFk-EifKHdF8yUzVSNHJMUzhnYV9ULW1xdDR2SUE&single=true&gid=5&output=csv");
            // after init hub runs all other hubs need to reload!
            new InitHub(_context).run();
            new CertificationHub(_context).run();
            MainActivity.stopHubThreads();
            MainActivity.startHubThreads(_context);

            // test buyerhub...
            ArrayList<Buyer> buyerArrayList = new ArrayList<Buyer>(Hub.buyerMap.values());
            for (int j = 0; j < buyerArrayList.size(); j++) {
                Log.w(Tag, buyerArrayList.get(j).toString());
                ArrayList<Certification> certificationsArrayList = new ArrayList<Certification>(buyerArrayList.get(j).getCertifications());
                Log.w(Tag, " \n     certifications -- ");
                for (int k = 0; k < certificationsArrayList.size(); k++) {
                    Certification certification = certificationsArrayList.get(k);
                    Log.w(Tag, " \n          " + certification.toString());
                }
            }

            // get specific oder from the hash
            Buyer buyer = Hub.buyerMap.get("B-2014-02");
            Log.w(Tag, "**** Found Benny's Bistro Buyer? => " + buyer);

            // test producerhub...
            ArrayList<Producer> producerArrayList = new ArrayList<Producer>(Hub.producerMap.values());
            for (int j = 0; j < producerArrayList.size(); j++) {
                Log.w(Tag, producerArrayList.get(j).toString());
                ArrayList<Certification> certificationsArrayList = new ArrayList<Certification>(producerArrayList.get(j).getCertifications());
                Log.w(Tag, " \n     certifications -- ");
                for (int k = 0; k < certificationsArrayList.size(); k++) {
                    Certification certification = certificationsArrayList.get(k);
                    Log.w(Tag, " \n          " + certification.toString());
                }
            }

            // get specific producer from the hash
            Producer p1 = Hub.producerMap.get("P-2013-1");
            Log.w(Tag,"**** Found Newman Farm? => " + p1);

            // test orderhub...
            ArrayList<Order> orderArrayList = new ArrayList<Order>(Hub.orderMap.values());
            for (int j = 0; j < orderArrayList.size(); j++) {
                Order order = orderArrayList.get(j);
                Log.w(Tag, order.toString());
            }

            // test orderhub...
            ArrayList<Certification> certificationArrayList = new ArrayList<Certification>(Hub.certificationMap.values());
            for (int j = 0; j < certificationArrayList.size(); j++) {
                Certification certification = certificationArrayList.get(j);
                Log.w(Tag, certification.toString());
            }

            // get specific oder from the hash
            Order order = Hub.orderMap.get("B-2014-02");
            Log.w(Tag,"**** Found Order? => " + order);


        } catch (Exception e) {
            // use email which will use smtp, so no need to retry, clean up, etc.
            Log.w(Tag, "e = " + e.toString());

            email = new Intent(Intent.ACTION_SEND);
            email.putExtra(Intent.EXTRA_EMAIL, new String[]{HubInit.getHubEmailTo()});
            email.putExtra(Intent.EXTRA_SUBJECT, HubInit.getHubEmailSubject());
            email.putExtra(Intent.EXTRA_TEXT, "We found a problem submitting your data to the Helena Local Hub... Click the send button for this email and we'll send the request to Helena Local for you... Next time you have a good network " +
                    "connection synchronize your email, then check your sent folder to make sure it went out.  Call us @ 406-219-1414 if you have questions or concerns.  " +
                    "\n\nDetails follow: \n-----------------" + producer.toEmail() + "\n+++++++++" + item.toEmail(growerAgreementId) + "\n-----------------");
            //need this to prompts email client only
            email.setType("message/rfc822");
        }
        return email;
    }

    public AsyncTesterTask(Context context) {
        _context = context;
    }

    @Override
    protected Intent doInBackground(Void... params) {
        /*Intent intent = new Intent(HubInit.HubType.PRODUCER_HUB.name());
        // intent.setType(HubInit.HubType.PRODUCER_HUB.name());
        _context.sendBroadcast(intent);
        Log.e(Tag, "broadcast sent");
        */

        // test growerhub...
        Intent email = new Intent(Intent.ACTION_SEND);
        Producer producer = new Producer("P-2013-0", "Western Montana Growers’ Cooperative", "grower@wmgcoop.com", "http://www.wmgcoop.com/", "http://g.virbcdn.com/_f2/images/58/PageImage-524372-4680215-WMGC_WebBanner.jpg", "Arlee, MT 59821", new ArrayList<Certification>(), "This day...");
        Item item = new Item("I-2014-2-2-3", "P-2013-0", true, "Produce", "Beets", "http://en.wikipedia.org/wiki/Leek‎", "http://upload.wikimedia.org/wikipedia/commons/thumb/4/43/Leek.jpg/160px-Leek.jpg", 20, "10 lbs", 10.01, Calendar.getInstance(), "note 1");
        String growerAgreementId = "N/A";
        try {
            // succeed
            new GrowerHub().setItem(_context, producer, item, growerAgreementId);
            // fail with invalid category...
            item.setCategory("Foo");
            new GrowerHub().setItem(_context, producer, item, growerAgreementId);
        } catch (Exception e) {
            // use email which will use smtp, so no need to retry, clean up, etc.
            Log.w(Tag, "e = " + e.toString());

            email = new Intent(Intent.ACTION_SEND);
            email.putExtra(Intent.EXTRA_EMAIL, new String[]{HubInit.getHubEmailTo()});
            email.putExtra(Intent.EXTRA_SUBJECT, HubInit.getHubEmailSubject());
            email.putExtra(Intent.EXTRA_TEXT, "We found a problem submitting your data to the Helena Local Hub... Click the send button for this email and we'll send the request to Helena Local for you... Next time you have a good network " +
                    "connection synchronize your email, then check your sent folder to make sure it went out.  Call us @ 406-219-1414 if you have questions or concerns.  " +
                    "\n\nDetails follow: \n-----------------" + producer.toEmail() + "\n+++++++++" + item.toEmail(growerAgreementId) + "\n-----------------");
            //need this to prompts email client only
            email.setType("message/rfc822");
        }
        return email;
    }


    @Override
    protected void onPostExecute(Intent intent) {
        super.onPostExecute(intent);

        if (intent != null) {
            _context.startActivity(Intent.createChooser(intent, "Choose an Email client :"));
        }
    }
}
