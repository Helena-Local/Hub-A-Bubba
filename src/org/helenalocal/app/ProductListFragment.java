package org.helenalocal.app;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.helenalocal.Helena_Local_Hub.R;
import org.helenalocal.base.Hub;
import org.helenalocal.base.Item;

import java.util.ArrayList;
import java.util.List;


public class ProductListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<List<Item>> {
    private static String logTag = "ProductListFragment";
    private static final int LoaderId = 0;
    public static final String HUB_TYPE_KEY = "hubType";

    private List<Item> _itemList;
    private ProductItemAdapter _arrayAdapter;


    public ProductListFragment() {
        Log.w(logTag, "Constructor");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.w(logTag, "onCreateView");

        View v = inflater.inflate(R.layout.product_listview, container, true);
        return v;
//        return super.onCreateView(inflater, container, savedInstanceState);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.w(logTag, "onViewCreated");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.w(logTag, "onActivityCreated");

        _itemList = new ArrayList<Item>();
        _arrayAdapter = new ProductItemAdapter(getActivity(), R.layout.product_view, _itemList);
        setListAdapter(_arrayAdapter);

        Bundle b = new Bundle();
        b.putString(HUB_TYPE_KEY, Hub.HubType.ITEM.name());
        getActivity().getSupportLoaderManager().initLoader(LoaderId, b, this);
    }


//    @Override
//    public void onResume() {
//        super.onResume();
//
//        Log.w(Tag, "onResume");
//
//        Bundle b = new Bundle();
//        b.putString(HUB_TYPE_KEY, Hub.HubType.SALES.name());
//        getActivity().getSupportLoaderManager().restartLoader(LoaderId, b, this);
//    }


    // ***
    // LoaderManager callbacks
    // ***

    @Override
    public Loader<List<Item>> onCreateLoader(int i, Bundle bundle) {
        Hub.HubType hubType = Hub.HubType.valueOf(bundle.getString(HUB_TYPE_KEY));
        AsyncProductLoader loader = new AsyncProductLoader(getActivity(), hubType);
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