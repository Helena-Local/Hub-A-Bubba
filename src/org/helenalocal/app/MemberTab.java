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
import org.helenalocal.base.Hub;
import org.helenalocal.base.Item;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MemberTab extends Fragment {

    private static final String Tag = "MemberTab";

    private List<Item> _itemList;
    private MemberItemAdapter _arrayAdapter;

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



//        Button b = (Button) getActivity().findViewById(R.id.growerButton);
//        b.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                // TODO - pass the name for now. Evenutally will pass grower id or something.
//                Intent i = new Intent(getActivity(), GrowerDetailActivity.class);
//                i.putExtra("growerNameKey", "Acme Farms");
//                startActivity(i);
//            }
//        });
    }

    @Override
    public void onResume() {
        super.onResume();

        _itemList.clear();
        Collection<Item> tempList = Hub.itemMap.values();
        for (Item i : tempList) {
            if (i.isInCsaThisWeek() == true) {
                _itemList.add(i);
            }
        }

        _arrayAdapter.notifyDataSetChanged();
    }

}