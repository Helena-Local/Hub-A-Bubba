package org.helenalocal.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import org.helenalocal.Helena_Local_Hub.R;
import org.helenalocal.base.*;
import org.helenalocal.base.get.BuyerHub;
import org.helenalocal.base.get.OrderHub;
import org.helenalocal.base.get.ProducerHub;
import org.helenalocal.base.post.GrowerHub;
import org.helenalocal.utils.ViewServer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends FragmentActivity implements LoaderManager.LoaderCallbacks<List<Item>> {
    private final String logTag = "MainActivity";
    public static final String HUB_TYPE_KEY = "hubType";

    private static final int LoaderId = 1;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity);

        Bundle b = new Bundle();
        b.putString(HUB_TYPE_KEY, "Item");
        getSupportLoaderManager().initLoader(LoaderId, b, this);

        ViewServer.get(this).addWindow(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ViewServer.get(this).removeWindow(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        ViewServer.get(this).setFocusedWindow(this);
    }


    @Override
    public Loader onCreateLoader(int i, Bundle bundle) {
        AsyncProductLoader loader = new AsyncProductLoader(this);

        //TODO shane -- This is a working example... Let me know if you have questions about it... :)
        // public Producer(String PID, String name, String contactEmail, String websiteUrl, String photoUrl, String location) {
        Producer producer = new Producer("P-2013-0","Western Montana Growers’ Cooperative","grower@wmgcoop.com","http://www.wmgcoop.com/","http://g.virbcdn.com/_f2/images/58/PageImage-524372-4680215-WMGC_WebBanner.jpg","Arlee, MT 59821");

        // public Item(String IID,Producer producer,boolean inCsaThisWeek, String category, String productDesc, String productUrl, String productImageUrl, Integer unitsAvailable,
        //        String unitDesc, Double unitPrice, Calendar deliveryDate, String note) {
        Item item = new Item("I-2014-2-2-3","P-2013-0", true, "Produce", "Leeks","http://en.wikipedia.org/wiki/Leek‎",
                "http://upload.wikimedia.org/wikipedia/commons/thumb/4/43/Leek.jpg/160px-Leek.jpg",20,"10 lbs",10.01,Calendar.getInstance(),"note 1");

        // growerAgreementId only needed from UI submit... Not part of the item object.
        String growerAgreementId = "N/A";
        GrowerHub growerHub = new GrowerHub();
        try {
            // test producerhub...
            ProducerHub producerHub = new ProducerHub();
            ArrayList<Producer> producerArrayList = new ArrayList<Producer>(producerHub.getProducerMap(this.getApplicationContext()).values());
            for (int j = 0; j < producerArrayList.size(); j++) {
                Producer producer2 = producerArrayList.get(j);
                Log.w(logTag, producer2.toString());
            }

            // get specific producer from the hash
            Producer p1 = producerHub.getProducerMap(this.getApplicationContext()).get("P-2013-1");
            Log.w(logTag,"**** Found Newman Farm? => " + p1);

            // test orderhub...
            OrderHub orderHub = new OrderHub();
            ArrayList<Order> orderArrayList = new ArrayList<Order>(orderHub.getOrderMap(this.getApplicationContext()).values());
            for (int j = 0; j < orderArrayList.size(); j++) {
                Order order = orderArrayList.get(j);
                Log.w(logTag, order.toString());
            }

            // get specific oder from the hash
            Order order = orderHub.getOrderMap(this.getApplicationContext()).get("O-2014-2-2-7");
            Log.w(logTag,"**** Found Order? => " + order);

            // test buyerhub...
            BuyerHub buyerHub = new BuyerHub();
            ArrayList<Buyer> buyerArrayList = new ArrayList<Buyer>(buyerHub.getBuyerMap(this.getApplicationContext()).values());
            for (int j = 0; j < buyerArrayList.size(); j++) {
                Buyer buyer = buyerArrayList.get(j);
                Log.w(logTag, buyer.toString());
            }

            // get specific oder from the hash
            Buyer buyer = buyerHub.getBuyerMap(this.getApplicationContext()).get("B-2014-02");
            Log.w(logTag,"**** Found Benny's Bistro Buyer? => " + buyer);

            // test growerhub...
            // succeed
            growerHub.setItem(this.getApplicationContext(), producer, item, growerAgreementId);
            // fail with invalid category...
            item.setCategory("Foo");
            growerHub.setItem(this.getApplicationContext(), producer, item, growerAgreementId);
        } catch (Exception e) {
            // use email which will use smtp, so no need to retry, clean up, etc.
            Log.w(logTag, "e = " + e.toString());

            Intent email = new Intent(Intent.ACTION_SEND);
            email.putExtra(Intent.EXTRA_EMAIL, new String[]{ Hub.HUB_EMAIL_TO });
            email.putExtra(Intent.EXTRA_SUBJECT, Hub.HUB_EMAIL_SUBJECT);
            email.putExtra(Intent.EXTRA_TEXT, "We found a problem submitting your data to the Helena Local Hub... Click the send button for this email and we'll send the request to Helena Local for you... Next time you have a good network " +
                    "connection synchronize your email, then check your sent folder to make sure it went out.  Call us @ 406-219-1414 if you have questions or concerns.  " +
                    "\n\nDetails follow: \n-----------------" + producer.toEmail() + "\n+++++++++" + item.toEmail(growerAgreementId) + "\n-----------------");


            //need this to prompts email client only
            email.setType("message/rfc822");
            startActivity(Intent.createChooser(email, "Choose an Email client :"));
        }
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<List<Item>> listLoader, List<Item> items) {
    }


    @Override
    public void onLoaderReset(Loader loader) {

    }
}