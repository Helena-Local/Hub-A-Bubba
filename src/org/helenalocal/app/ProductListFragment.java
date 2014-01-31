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
import org.helenalocal.base.Product;

import java.util.ArrayList;
import java.util.List;


public class ProductListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<List<Product>> {

    private static final String Tag = "ProductListFragment";
    private static final int LoaderId = 0;
    public static final String HUB_TYPE_KEY = "hubType";

    private List<Product> _productList;
    private ProductItemAdapter _arrayAdapter;


    public ProductListFragment() {
        Log.w(Tag, "Constructor");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.w(Tag, "onCreateView");

        View v = inflater.inflate(R.layout.product_listview, container, true);
        return v;
//        return super.onCreateView(inflater, container, savedInstanceState);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.w(Tag, "onViewCreated");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.w(Tag, "onActivityCreated");

        _productList = new ArrayList<Product>();
        _arrayAdapter = new ProductItemAdapter(getActivity(), R.layout.product_view, _productList);
        setListAdapter(_arrayAdapter);

        Bundle b = new Bundle();
        b.putString(HUB_TYPE_KEY, Hub.Type.SALES.name());
        getActivity().getSupportLoaderManager().initLoader(LoaderId, b, this);
    }


//    @Override
//    public void onResume() {
//        super.onResume();
//
//        Log.w(Tag, "onResume");
//
//        Bundle b = new Bundle();
//        b.putString(HUB_TYPE_KEY, Hub.Type.SALES.name());
//        getActivity().getSupportLoaderManager().restartLoader(LoaderId, b, this);
//    }


    // ***
    // LoaderManager callbacks
    // ***

    @Override
    public Loader<List<Product>> onCreateLoader(int i, Bundle bundle) {
        Hub.Type type = Hub.Type.valueOf(bundle.getString(HUB_TYPE_KEY));
        AsyncProductLoader loader = new AsyncProductLoader(getActivity(), type);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<List<Product>> listLoader, List<Product> products) {
        _productList.clear();
        _productList.addAll(products);
        _arrayAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<List<Product>> listLoader) {

    }
}