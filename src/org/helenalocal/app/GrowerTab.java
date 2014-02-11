package org.helenalocal.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import org.helenalocal.Helena_Local_Hub.R;
import org.helenalocal.base.Hub;
import org.helenalocal.base.Item;
import org.helenalocal.base.Producer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GrowerTab extends Fragment {

    private static String Tag = "GrowerTab";
    private List<Producer> _growerList;
    private GrowerListAdapter _arrayAdapter;

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
    public void onResume() {
        super.onResume();

        _growerList.clear();
        _growerList.addAll(Hub.producerMap.values());
        _arrayAdapter.notifyDataSetChanged();
    }
}
