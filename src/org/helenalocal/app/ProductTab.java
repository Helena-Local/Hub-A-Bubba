package org.helenalocal.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
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

//public class ProductTab extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
//
//    private static String Tag = "ProductTab";
//    private static final int LoaderId = 0;
//
//    private List<Item> _itemList;
//    private ProductItemAdapter _arrayAdapter;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.product_tab, container, false);
//    }
//
//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//
//        _itemList = new ArrayList<Item>();
//        _arrayAdapter = new ProductItemAdapter(getActivity(), R.layout.product_view, _itemList);
//
//        ListView listView = (ListView)getActivity().findViewById(R.id.productListView);
//        listView.setAdapter(_arrayAdapter);
//
//        getActivity().getSupportLoaderManager().initLoader(LoaderId, null, this);
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        getActivity().getSupportLoaderManager().restartLoader(LoaderId, null, this);
//    }
//
//
//    // ***
//    // LoaderManager callbacks
//    // ***
//
//    @Override
//    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
//        CursorLoader loader = new CursorLoader(getActivity(), ItemProvider.CONTENT_URI, null, null, null, null);
//        return loader;
//    }
//
//    @Override
//    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
//        int itemIdIndex = cursor.getColumnIndexOrThrow(ItemProvider.KEY_ITEM_ID);
//        int producerIdIndex = cursor.getColumnIndexOrThrow(ItemProvider.KEY_PRODUCER_ID);
//        int inCSAIndex = cursor.getColumnIndex(ItemProvider.KEY_IN_CSA);
//        int productDescIndex = cursor.getColumnIndexOrThrow(ItemProvider.KEY_PRODUCT_DESC);
//        int unitsAvailableIndex = cursor.getColumnIndex(ItemProvider.KEY_UNITS_AVAILABLE);
//        int unitPriceIndex = cursor.getColumnIndex(ItemProvider.KEY_UNIT_PRICE);
//        int unitDescIndex = cursor.getColumnIndex(ItemProvider.KEY_UNIT_DESC);
//
//        _itemList.clear();
//        while (cursor.moveToNext() == true) {
//            Item newItem = new Item(cursor.getString(itemIdIndex),
//                                    cursor.getString(producerIdIndex),
//                                    (cursor.getInt(inCSAIndex) == 1) ? true : false,
//                                    null,
//                                    cursor.getString(productDescIndex),
//                                    null,
//                                    null,
//                                    Integer.valueOf(cursor.getInt(unitsAvailableIndex)),
//                                    cursor.getString(unitDescIndex),
//                                    cursor.getDouble(unitPriceIndex),
//                                    null,
//                                    null);
//
//            _itemList.add(newItem);
//        }
//
//        _arrayAdapter.notifyDataSetChanged();
//
//    }
//
//    @Override
//    public void onLoaderReset(Loader<Cursor> cursorLoader) {
//    }
//}