/*
 * Copyright (c) 2014. This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License for Helena Local Inc. All rights reseved.
 */

package org.helenalocal.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import org.helenalocal.Helena_Local_Hub.R;
import org.helenalocal.base.Buyer;
import org.helenalocal.base.Hub;

import java.util.ArrayList;
import java.util.List;

public class RestaurantTab extends Fragment {

    private static final String Tag = "RestaurantTab";

    private List<Buyer> _restaurantList;
    private RestaurantItemAdapter _arrayAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.restaurant_tab, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        _restaurantList = new ArrayList<Buyer>();
        _arrayAdapter = new RestaurantItemAdapter(getActivity(), R.layout.restautant_item_view, _restaurantList);

        ListView listView = (ListView)getActivity().findViewById(R.id.restaurantListView);
        listView.setAdapter(_arrayAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();

        _restaurantList.addAll(Hub.buyerMap.values());
        _arrayAdapter.notifyDataSetChanged();
    }
}