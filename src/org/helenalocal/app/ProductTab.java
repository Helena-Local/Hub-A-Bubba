/*
 * Copyright (c) 2014. This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License for Helena Local Inc. All rights reseved.
 */

package org.helenalocal.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import org.helenalocal.Helena_Local_Hub.R;
import org.helenalocal.base.Hub;
import org.helenalocal.base.Item;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProductTab extends Fragment {

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

        TextView textView = (TextView)getActivity().findViewById(R.id.welcomeText);
        Linkify.addLinks(textView, Linkify.PHONE_NUMBERS);
        Linkify.addLinks(textView, Pattern.compile("HelenaLocal.org"), "http://", null, new Linkify.TransformFilter() {
            @Override
            public String transformUrl(Matcher match, String url) {
                return "www.helenalocal.org";
            }
        });

        _itemList = new ArrayList<Item>();
        _arrayAdapter = new ProductItemAdapter(getActivity(), R.layout.product_listview_item, _itemList);

        ListView listView = (ListView)getActivity().findViewById(R.id.productListView);
        listView.setAdapter(_arrayAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();

        _itemList.clear();
        Collection<Item> tempList = Hub.itemMap.values();
        for (Item i : tempList) {
            if (i.isInCsaThisWeek() == false) {
                _itemList.add(i);
            }
        }

        _arrayAdapter.notifyDataSetChanged();
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