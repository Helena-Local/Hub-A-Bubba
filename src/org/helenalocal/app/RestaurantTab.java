/*
 * Copyright (c) 2014. This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License for Helena Local Inc. All rights reseved.
 */

package org.helenalocal.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import org.helenalocal.Helena_Local_Hub.R;
import org.helenalocal.base.Buyer;
import org.helenalocal.base.Hub;
import org.helenalocal.base.HubInit;

import java.util.ArrayList;
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

        ListView listView = (ListView)getActivity().findViewById(R.id.restaurantListView);
        listView.setAdapter(_arrayAdapter);

        listView.setClickable(true);
        listView.setOnItemClickListener(this);
    }

    @Override
    protected void onRefresh() {
        super.onRefresh();

        _restaurantList.clear();
        _restaurantList.addAll(Hub.buyerMap.values());
        _arrayAdapter.notifyDataSetChanged();
    }

    /***
     * OnItemClickListener methods
     ***/
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ListView listView = (ListView)getActivity().findViewById(R.id.restaurantListView);
        Buyer buyer = (Buyer)listView.getItemAtPosition(position);

        Intent i = new Intent(getActivity(), RestaurantDetailActivity.class);
        i.putExtra(BUYER_ID_KEY, buyer.getBID());
        startActivity(i);
    }
}