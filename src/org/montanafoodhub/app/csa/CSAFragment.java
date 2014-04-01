/*
 * Copyright (c) 2014. This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License for Helena Local Inc. All rights reseved.
 */

package org.montanafoodhub.app.csa;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import org.montanafoodhub.Helena_Local_Hub.R;
import org.montanafoodhub.app.FragmentBase;
import org.montanafoodhub.app.HubApplication;
import org.montanafoodhub.app.InfoHeaderItem;
import org.montanafoodhub.app.ListItem;
import org.montanafoodhub.base.Buyer;
import org.montanafoodhub.base.Hub;
import org.montanafoodhub.base.HubInit;
import org.montanafoodhub.utils.ImageCache;

import java.util.*;

public class CSAFragment extends FragmentBase implements AdapterView.OnItemClickListener {

    private List<ListItem> _csaList;
    private CSAItemAdapter _listAdapter;

    @Override
    public int getTitleId() {
        return R.string.csa_fragment_title;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeReceiver(HubInit.HubType.BUYER_HUB);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.csa_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        _csaList = new ArrayList<ListItem>();
        _listAdapter = new CSAItemAdapter(getActivity(), _csaList);

        ListView listview = (ListView)getActivity().findViewById(R.id.csaListView);
        listview.setAdapter(_listAdapter);
        listview.setOnItemClickListener(this);
    }

    @Override
    protected void onRefresh() {
        super.onRefresh();

        ImageCache imageCache = ((HubApplication)getActivity().getApplication()).getImageCache();
        ArrayList<CSAItem> buyerList = new ArrayList<CSAItem>();
        for (Map.Entry<String, Buyer> entry : Hub.buyerMap.entrySet()) {
            // Service Level (0-off, 1-restaurant-on, 2-restaurant-premium, 3-CSA)
            if (entry.getValue().getBuyerType() == 3) {
                buyerList.add(new CSAItem(entry.getValue(), imageCache));
            }
        }

        // sort alpha on csa name
        Collections.sort(buyerList, new Comparator<CSAItem>() {
            @Override
            public int compare(CSAItem lhs, CSAItem rhs) {
                return lhs.getCSA().getName().compareToIgnoreCase(rhs.getCSA().getName());
            }
        });

        _csaList.clear();
        _csaList.add(new InfoHeaderItem(R.string.csa_fragment_welcome));
        _csaList.addAll(buyerList);
        _listAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CSAItem item = (CSAItem)_csaList.get(position);

        Intent i = new Intent(getActivity(), CSADetailActivity.class);
        i.putExtra(CSADetailActivity.EXTRA_CSA_ID, item.getCSA().getBID());
        startActivity(i);
    }
}