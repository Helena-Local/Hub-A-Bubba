/*
 * Copyright (c) 2014. This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License for Helena Local Inc. All rights reseved.
 */

package org.helenalocal.app.restaurant;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import org.helenalocal.Helena_Local_Hub.R;
import org.helenalocal.app.*;
import org.helenalocal.base.Buyer;
import org.helenalocal.base.Hub;
import org.helenalocal.base.HubInit;
import org.helenalocal.base.get.BuyerHub;
import org.helenalocal.utils.ImageCache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RestaurantFragment extends FragmentBase implements AdapterView.OnItemClickListener, DismissableInfoHeaderItem.IInfoHeaderDismissedListener {

    private static final String LogTag = "RestaurantFragment";

    private List<ListItem> _restaurantList;
    private RestaurantItemAdapter _arrayAdapter;

    @Override
    public int getTitleId() {
        return R.string.restaurant_fragment_title;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeReceiver(HubInit.HubType.BUYER_HUB);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.restaurant_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        _restaurantList = new ArrayList<ListItem>();
        _arrayAdapter = new RestaurantItemAdapter(getActivity(), _restaurantList);

        ListView listView = (ListView) getActivity().findViewById(R.id.restaurantListView);
        listView.setAdapter(_arrayAdapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    protected void onRefresh() {
        super.onRefresh();

        _restaurantList.clear();

        addInfoHeader();

        ImageCache imageCache = ((HubApplication)(getActivity().getApplication())).getImageCache();

        // add buyers
        ArrayList<BuyerItem> premiumWithOrdersList = new ArrayList<BuyerItem>();
        ArrayList<BuyerItem> justOrdersList = new ArrayList<BuyerItem>();
        ArrayList<BuyerItem> premiumNoOrdersList = new ArrayList<BuyerItem>();

        ArrayList<Buyer> buyerArrayList = new ArrayList<Buyer>(Hub.buyerMap.values());
        for (int j = 0; j < buyerArrayList.size(); j++) {
            Buyer buyer = buyerArrayList.get(j);
            // now set the producer order count
            buyer.setOrderCnt(BuyerHub.getOrderItemCnt(buyer.getBID()));

            int serviceLevel = Integer.valueOf(buyer.getServiceLevel().trim());
            // Service Level (0-off, 1-on, 2-premium)
            if ((serviceLevel == 2) && (buyer.getOrderCnt() > 0)) {
                premiumWithOrdersList.add(new BuyerItem(buyer, imageCache));
            } else if ((serviceLevel != 0) && buyer.getOrderCnt() > 0) {
                justOrdersList.add(new BuyerItem(buyer, imageCache));
            } else if (serviceLevel == 2) {
                premiumNoOrdersList.add(new BuyerItem(buyer, imageCache));
            }
            Log.w(LogTag, "buyer.getName() = " + buyer.getName() + "; serviceLevel = " + serviceLevel + "; orderCnt = " + buyer.getOrderCnt());
        }
        // sort
        Collections.sort(premiumWithOrdersList, new Comparator<BuyerItem>() {
            public int compare(BuyerItem o1, BuyerItem o2) {
                return o2.getBuyer().getOrderCnt().compareTo(o1.getBuyer().getOrderCnt());
            }
        });

        // sort
        Collections.sort(justOrdersList, new Comparator<BuyerItem>() {
            public int compare(BuyerItem o1, BuyerItem o2) {
                return o2.getBuyer().getOrderCnt().compareTo(o1.getBuyer().getOrderCnt());
            }
        });

        // sort
        Collections.sort(premiumNoOrdersList, new Comparator<BuyerItem>() {
            public int compare(BuyerItem o1, BuyerItem o2) {
                return o2.getBuyer().getOrderCnt().compareTo(o1.getBuyer().getOrderCnt());
            }
        });

        // add them to display
        _restaurantList.addAll(premiumWithOrdersList);
        _restaurantList.addAll(justOrdersList);
        _restaurantList.addAll(premiumNoOrdersList);
        _arrayAdapter.notifyDataSetChanged();
    }

    private void addInfoHeader() {
        SharedPreferences prefs = getActivity().getSharedPreferences(Preferences.File, Context.MODE_PRIVATE);
        boolean infoDismissed= prefs.getBoolean(Preferences.RESTAURANT_INFO_HEADER_DISMISSED, false);
        if (infoDismissed == false) {
            _restaurantList.add(new DismissableInfoHeaderItem(this, R.string.restaurant_fragment_welcome));
        }
    }

    /**
     * OnItemClickListener methods
     * *
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ListView listView = (ListView) getActivity().findViewById(R.id.restaurantListView);
        BuyerItem item = (BuyerItem) listView.getItemAtPosition(position);

        Intent i = new Intent(getActivity(), RestaurantDetailActivity.class);
        i.putExtra(RestaurantDetailActivity.EXTRA_BUYER_ID, item.getBuyer().getBID());
        startActivity(i);
    }

    /**
     * DismissableInfoHeaderItem.IInfoHeaderDismissedListener
     */
    @Override
    public void onDismiss(DismissableInfoHeaderItem item) {
        SharedPreferences prefs = getActivity().getSharedPreferences(Preferences.File, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putBoolean(Preferences.RESTAURANT_INFO_HEADER_DISMISSED, true);
        edit.apply();

        _restaurantList.remove(item);
        _arrayAdapter.notifyDataSetChanged();
    }
}