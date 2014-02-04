package org.helenalocal.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import org.helenalocal.Helena_Local_Hub.R;
import org.helenalocal.base.Item;

import java.util.ArrayList;
import java.util.List;

public class ProductTab extends Fragment implements LoaderManager.LoaderCallbacks<List<Item>> {

    private static String Tag = "ProductTab";
    private static final int LoaderId = 0;

    private List<Item> _itemList;
    private ProductItemAdapter _arrayAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.product_tab, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        _itemList = new ArrayList<Item>();
        _arrayAdapter = new ProductItemAdapter(getActivity(), R.layout.product_view, _itemList);

        ListView listView = (ListView)getActivity().findViewById(R.id.productListView);
        listView.setAdapter(_arrayAdapter);

        getActivity().getSupportLoaderManager().initLoader(LoaderId, null, this);
    }



    // ***
    // LoaderManager callbacks
    // ***

    @Override
    public Loader<List<Item>> onCreateLoader(int i, Bundle bundle) {
        AsyncProductLoader loader = new AsyncProductLoader(getActivity());
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<List<Item>> listLoader, List<Item> items) {
        _itemList.clear();
        _itemList.addAll(items);
        _arrayAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<List<Item>> listLoader) {

    }

}