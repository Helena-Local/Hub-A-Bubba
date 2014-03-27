/*
 * Copyright (c) 2014. This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License for Helena Local Inc. All rights reseved.
 */

package org.helenalocal.app.producer;

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
import org.helenalocal.base.Hub;
import org.helenalocal.base.HubInit;
import org.helenalocal.base.Producer;
import org.helenalocal.base.get.ProducerHub;
import org.helenalocal.utils.ImageCache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ProducerFragment extends FragmentBase implements AdapterView.OnItemClickListener, InfoHeaderItem.IInfoHeaderDismissedListener {

    private static String Tag = "ProducerFragment";
    private List<ListItem> _producerList;
    private ProducerListAdapter _arrayAdapter;

    @Override
    public int getTitleId() {
        return R.string.producer_fragment_title;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeReceiver(HubInit.HubType.PRODUCER_HUB);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.producer_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        _producerList = new ArrayList<ListItem>();
        _arrayAdapter = new ProducerListAdapter(getActivity(), _producerList);

        ListView listView = (ListView) getActivity().findViewById(R.id.producerListView);
        listView.setAdapter(_arrayAdapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onRefresh() {
        super.onRefresh();

        ImageCache cache = ((HubApplication)getActivity().getApplication()).getImageCache();

        _producerList.clear();

        addInfoHeader();

        // add producers who are at serviceLevel = 2 (local) or (serviceLevel > 0 and have at least one order)!
        ArrayList<ProducerItem> localList = new ArrayList<ProducerItem>();
        ArrayList<ProducerItem> regionalList = new ArrayList<ProducerItem>();
        ArrayList<Producer> producerArrayList = new ArrayList<Producer>(Hub.producerMap.values());
        for (int j = 0; j < producerArrayList.size(); j++) {
            Producer producer = producerArrayList.get(j);
            // now set the producer order count
            producer.setOrderCnt(ProducerHub.getOrderItemCnt(producer.getPID()));

            int serviceLevel = Integer.valueOf(producer.getServiceLevel().trim());
            // Service Level (0-off, 1-regional, 2-local)
            if ((serviceLevel == 2) || (producer.getOrderCnt() > 0)) {
                switch (serviceLevel) {
                    case 0:
                        break;
                    case 2:
                        localList.add(new ProducerItem(producer, cache));
                        break;
                    default:
                        regionalList.add(new ProducerItem(producer, cache));
                        break;
                }
            }
            Log.w(Tag, "producer.getName() = " + producer.getName() + "; serviceLevel = " + serviceLevel + "; orderCnt = " + producer.getOrderCnt());
        }
        // sort
        Collections.sort(localList, new Comparator<ProducerItem>() {
            public int compare(ProducerItem o1, ProducerItem o2) {
                return o2.getProducer().getOrderCnt().compareTo(o1.getProducer().getOrderCnt());
            }
        });

        Collections.sort(regionalList, new Comparator<ProducerItem>() {
            public int compare(ProducerItem o1, ProducerItem o2) {
                return o2.getProducer().getOrderCnt().compareTo(o1.getProducer().getOrderCnt());
            }
        });

        // add them in order
        _producerList.addAll(localList);
        _producerList.addAll(regionalList);
        _arrayAdapter.notifyDataSetChanged();
    }


    private void addInfoHeader() {
        SharedPreferences prefs = getActivity().getSharedPreferences(Preferences.File, Context.MODE_PRIVATE);
        boolean infoDismissed= prefs.getBoolean(Preferences.PRODUCER_INFO_HEADER_DISMISSED, false);
        if (infoDismissed == false) {
            InfoHeaderItem h = new InfoHeaderItem(R.string.producer_fragment_welcome);
            h.setOnDismissedListener(this);
            _producerList.add(h);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        ListItem item = (ListItem) ((ListView)parent).getItemAtPosition(position);
        Producer producer = ((ProducerItem)item).getProducer();

        Intent intent = new Intent(getActivity(), ProducerDetailActivity.class);
        intent.putExtra(ProducerDetailActivity.EXTRA_PRODUCER_ID, producer.getPID());
        startActivity(intent);
    }

    @Override
    public void onDismiss(InfoHeaderItem item) {
        SharedPreferences prefs = getActivity().getSharedPreferences(Preferences.File, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putBoolean(Preferences.PRODUCER_INFO_HEADER_DISMISSED, true);
        edit.apply();

        _producerList.remove(item);
        _arrayAdapter.notifyDataSetChanged();
    }
}
