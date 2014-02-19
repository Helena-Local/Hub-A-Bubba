/*
 * Copyright (c) 2014. This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License for Helena Local Inc. All rights reseved.
 */

package org.helenalocal.app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import org.helenalocal.Helena_Local_Hub.R;
import org.helenalocal.base.Buyer;
import org.helenalocal.base.Hub;
import org.helenalocal.base.HubInit;
import org.helenalocal.base.get.BuyerHub;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RestaurantTab extends TabBase implements AdapterView.OnItemClickListener {

    public static final String BUYER_ID_KEY = "buyerIdKey";

    private static final String LogTag = "RestaurantTab";

    private List<Buyer> _restaurantList;
    private RestaurantItemAdapter _arrayAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeReceiver(HubInit.HubType.BUYER_HUB);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.restaurant_tab, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        _restaurantList = new ArrayList<Buyer>();
        _arrayAdapter = new RestaurantItemAdapter(getActivity(), R.layout.restautant_listview_item, _restaurantList);

        ListView listView = (ListView) getActivity().findViewById(R.id.restaurantListView);
        listView.setAdapter(_arrayAdapter);

        listView.setClickable(true);
        listView.setOnItemClickListener(this);
    }

    @Override
    protected void onRefresh() {
        super.onRefresh();

        _restaurantList.clear();

        // add buyers
        ArrayList<Buyer> premiumWithOrdersList = new ArrayList<Buyer>();
        ArrayList<Buyer> justOrdersList = new ArrayList<Buyer>();
        ArrayList<Buyer> premiumNoOrdersList = new ArrayList<Buyer>();

        ArrayList<Buyer> buyerArrayList = new ArrayList<Buyer>(Hub.buyerMap.values());
        for (int j = 0; j < buyerArrayList.size(); j++) {
            Buyer buyer = buyerArrayList.get(j);
            // now set the producer order count
            buyer.setOrderCnt(BuyerHub.getOrderItemCnt(buyer.getBID()));

            int serviceLevel = Integer.valueOf(buyer.getServiceLevel().trim());
            // Service Level (0-off, 1-on, 2-premium)
            if ((serviceLevel == 2) && (buyer.getOrderCnt() > 0)) {
                premiumWithOrdersList.add(buyer);
            } else if ((serviceLevel != 0) && buyer.getOrderCnt() > 0) {
                justOrdersList.add(buyer);
            } else if (serviceLevel == 2) {
                premiumNoOrdersList.add(buyer);
            }
            Log.w(LogTag, "buyer.getName() = " + buyer.getName() + "; serviceLevel = " + serviceLevel + "; orderCnt = " + buyer.getOrderCnt());
        }
        // sort
        Collections.sort(premiumWithOrdersList, new Comparator<Buyer>() {
            public int compare(Buyer o1, Buyer o2) {
                return o2.getOrderCnt().compareTo(o1.getOrderCnt());
            }
        });

        // sort
        Collections.sort(justOrdersList, new Comparator<Buyer>() {
            public int compare(Buyer o1, Buyer o2) {
                return o2.getOrderCnt().compareTo(o1.getOrderCnt());
            }
        });

        // sort
        Collections.sort(premiumNoOrdersList, new Comparator<Buyer>() {
            public int compare(Buyer o1, Buyer o2) {
                return o2.getOrderCnt().compareTo(o1.getOrderCnt());
            }
        });

        // add them to display
        _restaurantList.addAll(premiumWithOrdersList);
        _restaurantList.addAll(justOrdersList);
        _restaurantList.addAll(premiumNoOrdersList);
        _arrayAdapter.notifyDataSetChanged();
    }

    /**
     * OnItemClickListener methods
     * *
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ListView listView = (ListView) getActivity().findViewById(R.id.restaurantListView);
        Buyer buyer = (Buyer) listView.getItemAtPosition(position);

        Intent i = new Intent(getActivity(), RestaurantDetailActivity.class);
        i.putExtra(BUYER_ID_KEY, buyer.getBID());
        startActivity(i);
    }
}