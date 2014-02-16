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
import org.helenalocal.base.Hub;
import org.helenalocal.base.HubInit;
import org.helenalocal.base.Producer;
import org.helenalocal.base.get.ProducerHub;

import java.util.ArrayList;
import java.util.List;

public class GrowerTab extends TabBase {

    private static String Tag = "GrowerTab";
    private List<Producer> _growerList;
    private GrowerListAdapter _arrayAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeReceiver(HubInit.HubType.PRODUCER_HUB);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.grower_tab, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        _growerList = new ArrayList<Producer>();
        _arrayAdapter = new GrowerListAdapter(getActivity(), R.layout.grower_listview_item, _growerList);

        final ListView lv = (ListView) getActivity().findViewById(R.id.growerListView);
        lv.setAdapter(_arrayAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Producer p = (Producer)lv.getItemAtPosition(position);

                Intent i = new Intent(getActivity(), GrowerDetailActivity.class);
                i.putExtra("growerNameKey", p.getName());
                startActivity(i);
            }
        });
    }

    @Override
    public void onRefresh() {
        super.onRefresh();

        _growerList.clear();

        // add producers who are are serviceLevel = 2 (local) or (serviceLevel > 0 and have at least one order)!
        ArrayList<Producer> producerArrayList = new ArrayList<Producer>(Hub.producerMap.values());
        for (int j = 0; j < producerArrayList.size(); j++) {
            Producer producer = producerArrayList.get(j);
            int serviceLevel = Integer.valueOf(producer.getServiceLevel().trim());
            int orderCnt = ProducerHub.getOrderItemCnt(producer.getPID());
            if ((serviceLevel == 2) || (serviceLevel > 0 && orderCnt > 0)) {
                Log.w(Tag, "producer.getName() = " + producer.getName() + "; serviceLevel = " + serviceLevel + "; orderCnt = " + orderCnt);
                _growerList.add(producer);
            }
        }
        _arrayAdapter.notifyDataSetChanged();
    }
}
