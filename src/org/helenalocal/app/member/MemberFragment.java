/*
 * Copyright (c) 2014. This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License for Helena Local Inc. All rights reseved.
 */

package org.helenalocal.app.member;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import org.helenalocal.Helena_Local_Hub.R;
import org.helenalocal.app.FragmentBase;
import org.helenalocal.app.HubApplication;
import org.helenalocal.app.ListItem;
import org.helenalocal.app.producer.ProducerDetailActivity;
import org.helenalocal.app.restaurant.RestaurantDetailActivity;
import org.helenalocal.base.*;
import org.helenalocal.base.get.OrderHub;
import org.helenalocal.utils.ActivityUtils;
import org.helenalocal.utils.ImageCache;

import java.text.SimpleDateFormat;
import java.util.*;

public class MemberFragment extends FragmentBase implements ProductItem.IActionItemClickedListener {

    private static final String LogTag = "MemberFragment";
    private Buyer _buyer;
    private List<ListItem> _itemList;
    private MemberItemAdapter _arrayAdapter;

    @Override
    public int getTitleId() {
        return R.string.member_fragment_title;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        initializeReceiver(HubInit.HubType.ITEM_HUB);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.member_fragment, container, false);

        Bundle args = getArguments();
        String buyerId = args.getString(CSADetailActivity.EXTRA_CSA_ID);
        _buyer = Hub.buyerMap.get(buyerId);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ImageCache cache = ((HubApplication)getActivity().getApplication()).getImageCache();

        _itemList = new ArrayList<ListItem>();
        _arrayAdapter = new MemberItemAdapter(getActivity(), _itemList, this);

        ListView listView = (ListView)getActivity().findViewById(R.id.memberListView);
        listView.addHeaderView(new View(getActivity()));
        listView.addFooterView(new View(getActivity()));
        listView.setAdapter(_arrayAdapter);
    }

    @Override
//    protected void onRefresh() {
    public void onResume() {
        super.onResume();

        // display all Helena Local bought for CSA!
        TreeMap<String, List<Item>> productMap = new TreeMap<String, List<Item>>();
//        for (Order order : OrderHub.getOrdersForBuyer(HubInit.HELENA_LOCAL_BUYER_ID)) {
        List<Order> orderList = OrderHub.getOrdersForBuyer(_buyer.getBID());
        for (Order order : orderList) {

            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            Log.w(LogTag, "Order Date: " + dateFormat.format(order.getDate().getTime()));

            Item item = Hub.itemMap.get(order.getItemID());

            List<Item> itemList = productMap.get(item.getCategory());
            if (itemList == null) {
                itemList = new ArrayList<Item>();
                productMap.put(item.getCategory(), itemList);
            }

            itemList.add(item);

        }

        _itemList.clear();
        ImageCache imageCache = ((HubApplication)getActivity().getApplication()).getImageCache();

        // insert the marquee item first
        _itemList.add(new MarqueeItem(getActivity()));

        // now that we have the data sorted by and organized by category, flatten it out into a list.
        for (Map.Entry<String, List<Item>> entry : productMap.entrySet()) {

            // add the category
            _itemList.add(new SectionItem(entry.getKey()));

            // sort the products
            List<Item> productList = entry.getValue();
            Collections.sort(productList, new Comparator<Item>() {
                public int compare(Item i1, Item i2) {
                    return i1.getProductDesc().compareTo(i2.getProductDesc());
                }
            });


            // add the products
            for (Item item : productList) {
                Log.w(LogTag, item.getProductDesc());
                _itemList.add(new ProductItem(item, imageCache, this));
            }
        }

        _arrayAdapter.notifyDataSetChanged();
    }

    @Override
    public void onProducerItemClicked(Item item) {

        Producer producer = null;
        for (Order order : OrderHub.getOrdersForBuyer(HubInit.HELENA_LOCAL_BUYER_ID)) {

            if (order.getItemID().equalsIgnoreCase(item.getIID())) {
                producer = OrderHub.producerMap.get(order.getProducerID());
                break;
            }
        }

        if (producer == null) {
            Log.w(LogTag, String.format("Producer for Item not found - Item Id: %s", item.getIID()));
        }
        else {
            Intent intent = new Intent(getActivity(), ProducerDetailActivity.class);
            intent.putExtra(ProducerDetailActivity.EXTRA_PRODUCER_ID, producer.getPID());
            startActivity(intent);
        }
    }

    @Override
    public void onAboutItemClicked(Item item) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(item.getProductUrl()));
        ActivityUtils.startImplicitActivity(getActivity(), intent, R.string.unable_to_start_activity, LogTag);
    }

    @Override
    public void onRecipeItemClicked(Item item) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(item.getRecipeUrl()));
        ActivityUtils.startImplicitActivity(getActivity(), intent, R.string.unable_to_start_activity, LogTag);
    }
}
