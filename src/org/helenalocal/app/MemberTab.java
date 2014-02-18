/*
 * Copyright (c) 2014. This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License for Helena Local Inc. All rights reseved.
 */

package org.helenalocal.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import org.helenalocal.Helena_Local_Hub.R;
import org.helenalocal.base.Hub;
import org.helenalocal.base.HubInit;
import org.helenalocal.base.Item;
import org.helenalocal.base.Order;
import org.helenalocal.base.get.OrderHub;

import java.util.ArrayList;
import java.util.List;

public class MemberTab extends TabBase {

    private static final String Tag = "MemberTab";

    private List<Item> _itemList;
    private MemberItemAdapter _arrayAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeReceiver(HubInit.HubType.ITEM_HUB);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.member_tab, container, false);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        _itemList = new ArrayList<Item>();
        _arrayAdapter = new MemberItemAdapter(getActivity(), R.layout.member_product_listview_item, _itemList);

        ListView listView = (ListView)getActivity().findViewById(R.id.memberListView);
        listView.addHeaderView(new View(getActivity()));
        listView.addFooterView(new View(getActivity()));
        listView.setAdapter(_arrayAdapter);
    }

    @Override
    protected void onRefresh() {
        _itemList.clear();
        // display all Helena Local bought for CSA!
        for (Order order : OrderHub.getOrdersForBuyer(HubInit.HELENA_LOCAL_BUYER_ID)) {
            _itemList.add(Hub.itemMap.get(order.getItemID()));
        }
        _arrayAdapter.notifyDataSetChanged();
    }
}