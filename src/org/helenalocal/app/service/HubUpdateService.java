package org.helenalocal.app.service;

import android.app.IntentService;
import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import org.helenalocal.app.provider.ItemProvider;
import org.helenalocal.base.Buyer;
import org.helenalocal.base.Item;
import org.helenalocal.base.Producer;
import org.helenalocal.base.get.BuyerHub;
import org.helenalocal.base.get.ItemHub;
import org.helenalocal.base.get.ProducerHub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class HubUpdateService extends IntentService {

    private static final String Tag = "HubUpdateService";

    private HashMap<String, Item> _itemMap;
    private HashMap<String, Producer> _producerMap;
    private HashMap<String, Buyer> _buyerMap;


    public HubUpdateService() {
        super("HubUpdateService");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.w(Tag, "onHandleIntent");

        // for now start with an empty table
        ContentResolver cr = getContentResolver();
        cr.delete(ItemProvider.CONTENT_URI, null, null);

        Context context = getApplicationContext();
        LoadItemHub(context);
        LoadProducerHub(context);
        LoadBuyerHub(context);

        updateDB();
    }

    private void LoadItemHub(Context context) {

        Log.w(Tag, "**** Starting fetch for items....");

        try {
            ItemHub itemHub = new ItemHub(context);
            _itemMap = itemHub.getItemMap();
            List<Item> itemList = new ArrayList<Item>(_itemMap.values());

            Log.w(Tag, "Data last refreshed on " + itemHub.getLastRefreshTS().getTime());

            for (Item p : itemList) {
                Log.w(Tag, p.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.w(Tag,"**** Ending fetch for items");
    }

    private void LoadProducerHub(Context context) {

        Log.w(Tag, "**** Starting fetch for producers....");

        try {
            ProducerHub producerHub = new ProducerHub(context);
            _producerMap = producerHub.getProducerMap();
            List<Producer> producerList = new ArrayList<Producer>(_producerMap.values());

            Log.w(Tag, "Data last refreshed on " + producerHub.getLastRefreshTS().getTime());

            for (Producer p : producerList) {
                Log.w(Tag, p.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.w(Tag,"**** Ending fetch for producers");
    }

    private void LoadBuyerHub(Context context) {

        Log.w(Tag, "**** Starting fetch for buyers....");

        try {
            BuyerHub buyerHub = new BuyerHub(context);
            _buyerMap = buyerHub.getBuyerMap();
            ArrayList<Buyer> buyerList = new ArrayList<Buyer>(_buyerMap.values());

            for (Buyer b : buyerList) {
                Log.w(Tag, b.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.w(Tag,"**** Ending fetch for buyers");
    }

    private void updateDB() {
        Log.w(Tag, "updateDB");

        ContentResolver cr = getContentResolver();

        List<Item> itemList = new ArrayList<Item>(_itemMap.values());

        for (Item i : itemList) {
            ContentValues values = new ContentValues();

            values.put(ItemProvider.KEY_ITEM_ID, i.getIID());
            values.put(ItemProvider.KEY_PRODUCER_ID, i.getPID());
            values.put(ItemProvider.KEY_IN_CSA, i.isInCsaThisWeek() ? 1 : 0);
            values.put(ItemProvider.KEY_CATEGORY, i.getCategory());
            values.put(ItemProvider.KEY_PRODUCT_DESC, i.getProductDesc());
            values.put(ItemProvider.KEY_PRODUCT_URL, i.getProductUrl());
            values.put(ItemProvider.KEY_PRODUCT_IMAGE_URL, i.getProductImageUrl());
            values.put(ItemProvider.KEY_UNITS_AVAILABLE, i.getUnitsAvailable());
            values.put(ItemProvider.KEY_UNIT_DESC, i.getUnitDesc());
            values.put(ItemProvider.KEY_UNIT_PRICE, i.getUnitPrice());

            cr.insert(ItemProvider.CONTENT_URI, values);
        }
    }
}
